package com.stereokrauts.lib.midi.api;

public final class MidiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6148960950465960423L;
	
	public MidiException(final Exception nested) {
		super(nested);
	}
}
