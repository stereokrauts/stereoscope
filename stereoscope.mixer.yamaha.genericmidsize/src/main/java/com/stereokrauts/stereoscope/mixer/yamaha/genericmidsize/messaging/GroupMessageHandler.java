package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputGroups;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputGroups;

public class GroupMessageHandler extends HandlerForGenericMidsize implements IMessageHandler {
	public GroupMessageHandler(final GenericMidsizeMixer mixer) {
		super(mixer);
	}
	
	@Override
	public boolean handleMessage(final IMessage msg) {
		final int groupType = ((AbstractGroupMessage<?>) msg).getGroupType();
		int group = ((AbstractGroupMessage<?>) msg).getGroup();
		
		// this is possibly stupid
		final byte[] sysexParameter = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
				0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
				0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15,
				0x16, 0x17, 0x18, 0x19, 0x1a
		};
		
		if (msg instanceof MsgInputGroups) {
			// set sysex parameter
			// this just works with input groups <= 8
			switch (groupType) {
			case 0: group -= 1;	break;  // fader group
			case 1: group += 7;	break;  // mute group
			case 2:
				if (group < 5) {
					group += 15; //dynamics group
				}
				break;
			case 3:
				if (group < 5) {
					group += 19; // eq group
				}
				break;
			default: break;
			}
			final MsgInputGroups msgInputGroups = (MsgInputGroups) msg;
			this.mixer.getModifier().changedInputGroup(sysexParameter[group], msgInputGroups.getAttachment());
			return true;
		} else if (msg instanceof MsgOutputGroups) {
			// set sysex parameter
			// this just works with output group <= 4
			switch (groupType) {
			case 0: group -= 1; break;  // fader group
			case 1: group += 4; break;  // mute group
			case 2: group += 7; break;  // dynamics group
			case 3: group += 11; break; // eq group
			default: break;
			}
			final MsgOutputGroups msgOutputGroups = (MsgOutputGroups) msg;
			this.mixer.getModifier().changedOutputGroup((byte) group, msgOutputGroups.getAttachment());
			return true;
		}
		return false;
	}
}
