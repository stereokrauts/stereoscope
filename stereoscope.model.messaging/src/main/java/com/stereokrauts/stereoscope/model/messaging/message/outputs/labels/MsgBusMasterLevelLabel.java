package com.stereokrauts.stereoscope.model.messaging.message.outputs.labels;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;

/** This function is called, when the value of an
 * element has been changed and therefore a new
 * value for the associated label has been sent.
 */
public class MsgBusMasterLevelLabel extends AbstractLabelMessage<String> {
	public MsgBusMasterLevelLabel(final int busNumber, final String label) {
		super(busNumber, 0, label);
	}
}
