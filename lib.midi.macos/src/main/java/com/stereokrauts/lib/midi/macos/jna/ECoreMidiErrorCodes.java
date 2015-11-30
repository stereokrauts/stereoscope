package com.stereokrauts.lib.midi.macos.jna;

import com.stereokrauts.lib.midi.macos.MacMidiSubsystemException;

public enum ECoreMidiErrorCodes {
	kNoError(0),

	kMIDIInvalidClient(-10830), kMIDIInvalidPort(-10831), kMIDIWrongEndpointType(
			-10832), kMIDINoConnection(-10833), kMIDIUnknownEndpoint(-10834), kMIDIUnknownProperty(
			-10835), kMIDIWrongPropertyType(-10836), kMIDINoCurrentSetup(-10837), kMIDIMessageSendErr(
			-10838), kMIDIServerStartErr(-10839), kMIDISetupFormatErr(-10840), kMIDIWrongThread(
			-10841), kMIDIObjectNotFound(-10842), kMIDIIDNotUnique(-10843);

	private final int errorCode;

	ECoreMidiErrorCodes(final int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public static ECoreMidiErrorCodes byErrorCode(final int intValue) {
		for (final ECoreMidiErrorCodes code : ECoreMidiErrorCodes.values()) {
			if (code.getErrorCode() == intValue) {
				return code;
			}
		}
		throw new MacMidiSubsystemException("No such error code: " + intValue);
	}
}
