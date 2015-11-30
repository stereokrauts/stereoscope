package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgAuxMasterLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgBusMasterLevelLabel;

public final class MasterMessageHandler extends HandlerForGenericMidsize implements IMessageHandler {
	public MasterMessageHandler(final GenericMidsizeMixer mixer) {
		super(mixer);
	}

	@Override
	public boolean handleMessage(final IMessage msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber();
		if (section == SECTION.AUX) {
			if (ammsg instanceof MsgAuxMasterLevelChanged) {
				this.mixer.getModifier().changedAuxMaster(number, (Float) ammsg.getAttachment());
				final String label = this.mixer.getLabelMaker().getYamahaLabelLevel10Db((Float) msg.getAttachment());
				this.mixer.fireChange(new MsgAuxMasterLevelLabel(number, label));
				return true;
			} else if (ammsg instanceof MsgAuxMasterDelayChanged) {
				this.mixer.getModifier().changedAuxDelayTime(number, (Float) ammsg.getAttachment());
				return true;
			}
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterDelayChanged) {
				this.mixer.getModifier().changedBusDelayTime(number, (Float) msg.getAttachment());
				return true;
			} else if (msg instanceof MsgBusMasterLevelChanged) {
				this.mixer.getModifier().changedBusMaster(number, (Float) msg.getAttachment());
				final String label = this.mixer.getLabelMaker().getYamahaLabelLevel10Db((Float) msg.getAttachment());
				this.mixer.fireChange(new MsgBusMasterLevelLabel(number, label));
				return true;
			}
		} else if (section == SECTION.OUTPUT) {
			// not available in generic midsize
			return false;
		} else if (section == SECTION.MASTER) {
			if (msg instanceof MsgMasterLevelChanged) {
				this.mixer.getModifier().changedMasterLevel((Float) msg.getAttachment());
				return true;
			}
		}
		return false;
	}

}
