package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when a channel-mute button has been
 * changed on the observed object.
 * @param channel The changed channel (Beginning at 0)
 * @param value The state of the button which can be 0 (off) or 1 (on)
 */
public class MsgChannelOnChanged extends AbstractChannelMessage<Boolean> {

	public MsgChannelOnChanged(final int channel, final boolean value) {
		super(channel, value);
		this.setPriority(PriorizationValue.HIGH);
	}

}
