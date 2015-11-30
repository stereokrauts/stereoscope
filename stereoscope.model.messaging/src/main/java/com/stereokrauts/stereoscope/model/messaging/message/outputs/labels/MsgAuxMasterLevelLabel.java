package com.stereokrauts.stereoscope.model.messaging.message.outputs.labels;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;

/** This function is called, when the value of an
 * element has been changed and therefore a new
 * value for the associated label has been sent.
 */
public class MsgAuxMasterLevelLabel extends AbstractLabelMessage<String> {
	public MsgAuxMasterLevelLabel(final int auxSend, final String label) {
		super(auxSend, 0, label);
	}
}
