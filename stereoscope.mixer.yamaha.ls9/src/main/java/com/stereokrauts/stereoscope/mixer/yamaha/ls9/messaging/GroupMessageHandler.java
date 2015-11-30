package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputGroups;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputGroups;


public class GroupMessageHandler extends HandlerForLs9 implements
		IMessageHandler {

	public GroupMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all groups related messages
	 * from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public final boolean handleMessage(final IMessage msg) {
		final int groupType = ((AbstractGroupMessage<?>) msg).getGroupType();
		int group = ((AbstractGroupMessage<?>) msg).getGroup();
		byte element = 0;
		
		if (msg instanceof MsgInputGroups) {
			// set sysex parameter
			// this just works with input groups <= 8
			switch (groupType) {
			case 0: break;  // fader group
			case 1: element = Ls9SysexParameter.ELMT_INPUT_MUTE_GROUP;
					break;  // mute group
			case 2: break; //dynamics group
			case 3: break; // eq group
			default: break;
			}
			
			group -= 1;
			final MsgInputGroups msgInputGroups = (MsgInputGroups) msg;
			this.mixer.getModifier().changedInputGroup(element, group, msgInputGroups.getAttachment());
			return true;
		} else if (msg instanceof MsgOutputGroups) {
			
			// not implemented
			switch (groupType) {
			case 0: break;  // fader group
			case 1: break;  // mute group
			case 2: break;  // dynamics group
			case 3: break; // eq group
			default: break;
			}
			//MsgOutputGroups msgOutputGroups = (MsgOutputGroups) msg;
			//changedOutputGroup((byte) group, msgOutputGroups.getAttachment());
			return false;
		}
		return false;
	}

}
