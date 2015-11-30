package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/** This function is called, when the master level has been
 * changed on the observed object.
 * @param value The new level (Range: 0...1)
 */

public class MsgMasterLevelChanged extends AbstractMasterMessage<Float> {

	public MsgMasterLevelChanged(final float value) {
		super(AbstractMasterMessage.SECTION.MASTER, 0, value);
	}

}
