package com.stereokrauts.lib.midi.macos.jna;

import com.sun.jna.IntegerType;

public final class OSStatus extends IntegerType {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2847296126711248093L;

	public OSStatus() {
		super(4);
	}
	
	public ECoreMidiErrorCodes toMidiErrorCode() {
		return ECoreMidiErrorCodes.byErrorCode(this.intValue());
	}
}
