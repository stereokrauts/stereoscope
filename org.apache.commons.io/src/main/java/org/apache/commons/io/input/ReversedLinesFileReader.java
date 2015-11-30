/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.io.Charsets;

/**
 * Reads lines in a file reversely (similar to a BufferedReader, but starting at
 * the last line). Useful for e.g. searching in log files.
 *
 * @since 2.2
 */
public class ReversedLinesFileReader implements Closeable {

    private final int blockSize;
    private final Charset encoding;

    private final RandomAccessFile randomAccessFile;

    private final long totalByteLength;
    private final long totalBlockCount;

    private final byte[][] newLineSequences;
    private final int avoidNewlineSplitBufferSize;
    private final int byteDecrement;

    private FilePart currentFilePart;

    private boolean trailingNewlineOfFileSkipped = false;

    /**
     * Creates a ReversedLinesFileReader with default block size of 4KB and the
     * platform's default encoding.
     *
     * @param file
     *            the file to be read
     * @throws IOException  if an I/O error occurs
     */
    public ReversedLinesFileReader(final File file) throws IOException {
        this(file, 4096, Charset.defaultCharset().toString());
    }

    /**
     * Creates a ReversedLinesFileReader with the given block size and encoding.
     *
     * @param file
     *            the file to be read
     * @param blockSize
     *            size of the internal buffer (for ideal performance this should
     *            match with the block size of the underlying file system).
     * @param encoding
     *            the encoding of the file
     * @throws IOException  if an I/O error occurs
     * @since 2.3
     */
    public ReversedLinesFileReader(final File file, final int blockSize, final Charset encoding) throws IOException {
        this.blockSize = blockSize;
        this.encoding = encoding;

        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        int lastBlockLength = (int) (this.totalByteLength % blockSize);
        if (lastBlockLength > 0) {
            this.totalBlockCount = this.totalByteLength / blockSize + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / blockSize;
            if (this.totalByteLength > 0) {
                lastBlockLength = blockSize;
            }
        }
        this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, null);

        // --- check & prepare encoding ---
        final Charset charset = Charsets.toCharset(encoding);
        final CharsetEncoder charsetEncoder = charset.newEncoder();
        final float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
        if(maxBytesPerChar==1f) {
            // all one byte encodings are no problem
            this.byteDecrement = 1;
        } else if(charset == Charset.forName("UTF-8")) {
            // UTF-8 works fine out of the box, for multibyte sequences a second UTF-8 byte can never be a newline byte
            // http://en.wikipedia.org/wiki/UTF-8
            this.byteDecrement = 1;
        } else if(charset == Charset.forName("Shift_JIS")) {
            // Same as for UTF-8
            // http://www.herongyang.com/Unicode/JIS-Shift-JIS-Encoding.html
            this.byteDecrement = 1;
        } else if(charset == Charset.forName("UTF-16BE") || charset == Charset.forName("UTF-16LE")) {
            // UTF-16 new line sequences are not allowed as second tuple of four byte sequences,
            // however byte order has to be specified
            this.byteDecrement = 2;
        } else if(charset == Charset.forName("UTF-16")) {
            throw new UnsupportedEncodingException(
                    "For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
        } else {
            throw new UnsupportedEncodingException(
                    "Encoding "+encoding+" is not supported yet (feel free to submit a patch)");
        }
        // NOTE: The new line sequences are matched in the order given, so it is important that \r\n is BEFORE \n
        this.newLineSequences = new byte[][] { "\r\n".getBytes(encoding), "\n".getBytes(encoding), "\r".getBytes(encoding) };

        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
    }

    /**
     * Creates a ReversedLinesFileReader with the given block size and encoding.
     *
     * @param file
     *            the file to be read
     * @param blockSize
     *            size of the internal buffer (for ideal performance this should
     *            match with the block size of the underlying file system).
     * @param encoding
     *            the encoding of the file
     * @throws IOException  if an I/O error occurs
     * @throws UnsupportedCharsetException
     *             thrown instead of {@link UnsupportedEncodingException} in version 2.2 if the encoding is not
     *             supported.
     */
    public ReversedLinesFileReader(final File file, final int blockSize, final String encoding) throws IOException {
        this(file, blockSize, Charsets.toCharset(encoding));
    }

    /**
     * Returns the lines of the file from bottom to top.
     *
     * @return the next line or null if the start of the file is reached
     * @throws IOException  if an I/O error occurs
     */
    public String readLine() throws IOException {

        String line = this.currentFilePart.readLine();
        while (line == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart != null) {
                line = this.currentFilePart.readLine();
            } else {
                // no more fileparts: we're done, leave line set to null
                break;
            }
        }

        // aligned behaviour wiht BufferedReader that doesn't return a last, emtpy line
        if("".equals(line) && !this.trailingNewlineOfFileSkipped) {
            this.trailingNewlineOfFileSkipped = true;
            line = this.readLine();
        }

        return line;
    }

    /**
     * Closes underlying resources.
     *
     * @throws IOException  if an I/O error occurs
     */
    @Override
	public void close() throws IOException {
        this.randomAccessFile.close();
    }

    private class FilePart {
        private final long no;

        private final byte[] data;

        private byte[] leftOver;

        private int currentLastBytePos;

        /**
         * ctor
         * @param no the part number
         * @param length its length
         * @param leftOverOfLastFilePart remainder
         * @throws IOException if there is a problem reading the file
         */
        private FilePart(final long no, final int length, final byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            final int dataLength = length + (leftOverOfLastFilePart != null ? leftOverOfLastFilePart.length : 0);
            this.data = new byte[dataLength];
            final long off = (no - 1) * ReversedLinesFileReader.this.blockSize;

            // read data
            if (no > 0 /* file not empty */) {
                ReversedLinesFileReader.this.randomAccessFile.seek(off);
                final int countRead = ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, length);
                if (countRead != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            // copy left over part into data arr
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        /**
         * Handles block rollover
         * 
         * @return the new FilePart or null
         * @throws IOException if there was a problem reading the file
         */
        private FilePart rollOver() throws IOException {

            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... "
                        + "last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            }

            if (this.no > 1) {
                return new FilePart(this.no - 1, ReversedLinesFileReader.this.blockSize, this.leftOver);
            } else {
                // NO 1 was the last FilePart, we're finished
                if (this.leftOver != null) {
                    throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart="
                            + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
                }
                return null;
            }
        }

        /**
         * Reads a line.
         * 
         * @return the line or null
         * @throws IOException if there is an error reading from the file
         */
        private String readLine() throws IOException {

            String line = null;
            int newLineMatchByteCount;

            final boolean isLastFilePart = this.no == 1;

            int i = this.currentLastBytePos;
            while (i > -1) {

                if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                    // avoidNewlineSplitBuffer: for all except the last file part we
                    // take a few bytes to the next file part to avoid splitting of newlines
                    this.createLeftOver();
                    break; // skip last few bytes and leave it to the next file part
                }

                // --- check for newline ---
                if ((newLineMatchByteCount = this.getNewLineMatchByteCount(this.data, i)) > 0 /* found newline */) {
                    final int lineStart = i + 1;
                    final int lineLengthBytes = this.currentLastBytePos - lineStart + 1;

                    if (lineLengthBytes < 0) {
                        throw new IllegalStateException("Unexpected negative line length="+lineLengthBytes);
                    }
                    final byte[] lineData = new byte[lineLengthBytes];
                    System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);

                    line = new String(lineData, ReversedLinesFileReader.this.encoding);

                    this.currentLastBytePos = i - newLineMatchByteCount;
                    break; // found line
                }

                // --- move cursor ---
                i -= ReversedLinesFileReader.this.byteDecrement;

                // --- end of file part handling ---
                if (i < 0) {
                    this.createLeftOver();
                    break; // end of file part
                }
            }

            // --- last file part handling ---
            if (isLastFilePart && this.leftOver != null) {
                // there will be no line break anymore, this is the first line of the file
                line = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
                this.leftOver = null;
            }

            return line;
        }

        /**
         * Creates the buffer containing any left over bytes.
         */
        private void createLeftOver() {
            final int lineLengthBytes = this.currentLastBytePos + 1;
            if (lineLengthBytes > 0) {
                // create left over for next block
                this.leftOver = new byte[lineLengthBytes];
                System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        /**
         * Finds the new-line sequence and return its length.
         * 
         * @param data buffer to scan
         * @param i start offset in buffer
         * @return length of newline sequence or 0 if none found
         */
        private int getNewLineMatchByteCount(final byte[] data, final int i) {
            for (final byte[] newLineSequence : ReversedLinesFileReader.this.newLineSequences) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; j--) {
                    final int k = i + j - (newLineSequence.length - 1);
                    match &= k >= 0 && data[k] == newLineSequence[j];
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
        }
    }

}