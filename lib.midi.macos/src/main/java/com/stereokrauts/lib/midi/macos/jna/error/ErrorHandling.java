package com.stereokrauts.lib.midi.macos.jna.error;

import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.sun.jna.Native;

public interface ErrorHandling {
	ErrorHandling INSTANCE = (ErrorHandling)
			Native.loadLibrary("CarbonCore", ErrorHandling.class);
	
	String GetMacOSStatusErrorString(OSStatus err);
	String GetMacOSStatusCommentString(OSStatus err);
}
