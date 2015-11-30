package com.stereokrauts.stereoscope.model.messaging.message.dsp;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;

/** This function is called, when a full reset event for a
 *  specific geq is triggered on the oserved object.
 * @param geqNumber Number of the actual graphical EQ
 */

public class MsgGeqFullReset extends AbstractGeqMessage<Object> {

	public MsgGeqFullReset(final short geqNumber, final Object attachment) {
		super(geqNumber, attachment);
	}

}
