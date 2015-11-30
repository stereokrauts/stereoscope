package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the pairing of two dynamics
 * processors of different channels is turned on/off on the 
 * observed object. This is called 'link' in generic midsize
 * and PM5D.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param boolean value Pairing/Linkin on/off
 */
public class MsgInputDynaPair extends AbstractInputDynamicsMessage<Boolean> {
	public MsgInputDynaPair(final int processor, final int channel, final boolean value) {
		super(processor, channel, value);
	}
}
