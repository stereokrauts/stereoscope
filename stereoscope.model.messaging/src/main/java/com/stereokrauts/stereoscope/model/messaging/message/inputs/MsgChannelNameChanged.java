package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when the channel name has been changed.
 * @param channel The changed channel (Beginning at 0)
 * @param name The new name.
 */
public class MsgChannelNameChanged extends AbstractChannelMessage<String> {

	public MsgChannelNameChanged(final int channel, final String name) {
		super(channel, name);
		this.setPriority(PriorizationValue.LOW);
	}

}
