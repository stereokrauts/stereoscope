package com.stereokrauts.stereoscope.model.messaging.message.control;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class MsgResyncStateful extends AbstractMessage<Integer> {

	private final Integer selectedAux;
	private final byte selectedGeq;

	public MsgResyncStateful(final Integer selectedChannel, final Integer selectedAux, final Byte selectedGeq) {
		super(selectedChannel);
		this.selectedAux = selectedAux;
		this.selectedGeq = selectedGeq;
	}

	public Integer getSelectedAux() {
		return this.selectedAux;
	}

	public byte getSelectedGeq() {
		return this.selectedGeq;
	}
	
}
