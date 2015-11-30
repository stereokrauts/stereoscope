package com.stereokrauts.lib.midi.macos.jna.util;

import com.sun.jna.*;

public interface CarbonAPI extends Library {
    CarbonAPI INSTANCE = (CarbonAPI) Native.loadLibrary("Carbon", CarbonAPI.class);

    CFStringRef CFStringCreateWithCharacters(
            Void alloc, //  always pass NULL
            char[] chars,
            CFIndex numChars
    );


}