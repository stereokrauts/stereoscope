package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/** This function is called, when the aux master has been changed
 * on the observed object.
 * @param aux The aux master (Beginning at 0)
 * @param value The new level (Range: 0...1)
 */

public class MsgAuxMasterLevelChanged extends AbstractMasterMessage<Float> {

	public MsgAuxMasterLevelChanged(final int aux, final float value) {
		super(AbstractMasterMessage.SECTION.AUX, aux, value);
	}

}
