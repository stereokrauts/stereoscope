package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when the ratio of a comp./exp./etc.
 * dynamics processor has been changed on the observed object.
 * The group-parameter can be one of:
 * 0 - Fader group
 * 1 - Mute group
 * 2 - Dynamics group
 * 3 - EQ group
 * @param int type The type of group (0-3)
 * @param int group The number of the group
 * @param boolean status Group can be on/off (true/false)
 */
public class MsgInputGroups extends AbstractGroupMessage<Boolean> {
	public MsgInputGroups(final int type, final int group, final boolean status) {
		super(type, group, status);
		this.setPriority(PriorizationValue.LOW);
	}
}
