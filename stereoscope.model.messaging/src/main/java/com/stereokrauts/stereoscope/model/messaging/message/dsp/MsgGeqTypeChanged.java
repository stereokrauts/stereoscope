package com.stereokrauts.stereoscope.model.messaging.message.dsp;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;

/**
 * This function is called, when the mixer responded to our request for
 * the current graphical eq type.
 * @param isFlexEq15 Whether the current selected GEQ is a FlexEQ 15
 */
public class MsgGeqTypeChanged extends AbstractGeqMessage<Boolean> {

	public MsgGeqTypeChanged(final short geqNumber, final boolean isFlexEq15) {
		super(geqNumber, isFlexEq15);
	}

}
