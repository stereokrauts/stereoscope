package com.stereokrauts.stereoscope.model.messaging.message.inputs.labels;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;

/** This function is called, when the value of an
 * element has been changed and therefore a new
 * value for the associated label has been sent.
 * @param int channel The input channel (Beginning at 0)
 * @param int identifier The aux send (Beginning at 0)
 * @param String label New label string
 */
public class MsgChannelAuxOnLabel extends AbstractLabelMessage<String> {
	public MsgChannelAuxOnLabel(final int channel, final int identifier, final String label) {
		super(identifier, channel, label);
	}
}
