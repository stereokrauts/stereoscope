package com.stereokrauts.stereoscope.model.messaging.message.control;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class MsgSelectedAuxChanged extends AbstractMessage<Integer> {

	public MsgSelectedAuxChanged(final int selectedAux) {
		super(selectedAux);
	}

}
