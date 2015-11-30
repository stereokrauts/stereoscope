package com.stereokrauts.stereoscope.webgui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class FileHelper {
	public static final String PLUGIN_PATH = "platform:/plugin/stereoscope.webgui.server/";
	public static final String LAYOUT_FOLDER = "layouts";
	public static final String LAYOUT_PATH = LAYOUT_FOLDER + "/";

	public FileHelper() {

	}



	public void compressAndWriteLayoutFile(final String input, final String fileName) {
		/*
		final int BUFFER = 2048;

		OutputStream outFile = FileLocator.resolve(
				new URL(PLUGIN_PATH + LAYOUT_PATH + fileName)).


		 */
	}

	public String getTouchOscXmlDescription(final InputStream inputStream) throws Exception {
		final ZipInputStream zipFile = new ZipInputStream(inputStream);

		String output = null;
		int count = 0;

		ZipEntry entry = zipFile.getNextEntry();
		while(entry != null){
			if (entry.getName().matches("index.xml") && count == 0) {
				output = convertInputStreamToString(zipFile);
			} else {
				throw new Exception("Not a TouchOSC layout file: " + inputStream);
			}
			count++;
			entry = zipFile.getNextEntry();
		}
		zipFile.close();
		return output;
	}
	
	/*
	public Boolean writeToFile(final String data, final String fileName) {
		
		this.createFolderIfNotExisting(folderName);
		OutputStream outFile = FileLocator.resolve(
				new URL(PLUGIN_PATH + LAYOUT_PATH + fileName)).openConnection();
		
		
		return true;
	}
	*/

	public String getFileEntry(final String entry) throws IOException {

		final InputStream input = FileLocator.resolve(
				new URL(PLUGIN_PATH + LAYOUT_PATH + entry)).openStream();
		return convertInputStreamToString(input);

	}

	public String getFolderEntries(final String folderName) throws IOException {
		String output = "";
		createFolderIfNotExisting(folderName);
		final File folder = new File(folderName);
		final File[] listOfFiles = folder.listFiles();
		for (final File file : listOfFiles) {
			if (file.isFile()) {
				output += file.getName() + "\n";
			}
		}
		return output;
	}

	private String convertInputStreamToString(final InputStream stream) throws IOException {
		final StringWriter writer = new StringWriter();
		IOUtils.copy(stream, writer);
		return writer.toString();
	}

	private boolean createFolderIfNotExisting(final String folderName) {
		final File folder = new File(folderName);
		boolean success = false;
		if (folder.isDirectory()) {
			success = true;
		} else {
			success = folder.mkdir();
		}
		return success;
	}

	public String getOsgiFolderEntries(final String bundleId, final String path) throws IOException {
		String files = "";
		final Bundle bundle = Platform.getBundle(bundleId);
		final Enumeration<URL> urls = bundle.findEntries(path, "*", false);

		while (urls.hasMoreElements()) {
			final URL url = urls.nextElement();
			final String[] pathString = url.getFile().split("/");

			files += pathString[pathString.length - 1] + "\r\n";
		}
		return files;

	}


	private String getUrlContent(final URL url) throws IOException {
		final InputStream buffer = url.openStream();
		final String content = IOUtils.toString(buffer);
		return content;
	}

	public String removeLeadingSlash(final String input) {
		if (input.substring(0, 1).matches("/")) {
			return input.substring(1);
		} else {
			return input;
		}
	}

	public String getFileExtension(final String fileName) {
		final int i = fileName.lastIndexOf('.');
		if (i > 0) {
			return fileName.substring(i+1);
		} else {
			return fileName;
		}
	}

	public boolean isMacro(final String fileName) {
		Boolean isMacro = false;
		final String[] parts = fileName.split("\\.");
		if (parts.length > 1 && parts[parts.length - 2].matches("macro")) {
			isMacro = true;
		}
		return isMacro;
	}

}
