package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when a channel level has been changed
 * on the observed object.
 * @param channel The changed channel (Beginning at 0)
 * @param value The new level (Range: 0...1)
 */
public class MsgChannelLevelChanged extends AbstractChannelMessage<Float> {
	public MsgChannelLevelChanged(final int channel, final float value) {
		super(channel, value);
		this.setPriority(PriorizationValue.HIGH);
	}
}
