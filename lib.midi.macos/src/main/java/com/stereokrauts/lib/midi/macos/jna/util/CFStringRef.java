package com.stereokrauts.lib.midi.macos.jna.util;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;


public class CFStringRef extends PointerByReference {

    public CFStringRef() {

    }

    public CFStringRef(Pointer p) {
        setPointer(p);
    }

    public static CFStringRef CFSTR(String str) {
        final char[] chars = str.toCharArray();
        return CoreFoundation.INSTANCE.CFStringCreateWithCharacters(null, chars, chars.length);
    }

    public String toString() {
        int len = CoreFoundation.INSTANCE.CFStringGetLength(this);
        Memory m = new Memory(len * 2);

        CFRange.ByValue range = new CFRange.ByValue();
        range.len = len;
        range.loc = 0;

        CoreFoundation.INSTANCE.CFStringGetCharacters(this, range, m);

        // return m.getString(0, true);
        return hack(m);
    }

    // https://github.com/twall/jna/issues/53
    public String hack(Memory m) {
        String str = "";

        for (int i = 0; i < m.size(); i += 2) {
            byte b1 = m.getByte(i + 0);
            byte b2 = m.getByte(i + 1);

            String s;
            try {
                s = new String(new byte[] { -2, -1, b2, b1 }, "UTF-16");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            str += s;
        }

        return str;
    }

}