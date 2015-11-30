package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;


public class MasterMessageHandler extends HandlerForM380 implements
		IMessageHandler {

	public MasterMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all master section related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public boolean handleMessage(final IMessage msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber();
		if (section == SECTION.AUX) {
			if (msg instanceof MsgAuxMasterLevelChanged) {
				this.mixer.getModifier().changedAuxMaster(number, (Float) msg.getAttachment());
				return true;
			}
		} else if (section == SECTION.BUS) {
			/********** NOT YET IMPLEMENTED **********
			if (msg instanceof MsgBusMasterLevelChanged) {
				changedBusMaster(number, (Float) msg.getAttachment());
			} */
		} else if (section == SECTION.OUTPUT) {
			/********** NOT YET IMPLEMENTED **********
			if (msg instanceof MsgOutputLevelChanged) {
				changedOutputMaster(number, (Float) msg.getAttachment());
			} */
		} else if (section == SECTION.MASTER) {
			if (msg instanceof MsgMasterLevelChanged) {
				this.mixer.getModifier().changedMasterLevel((Float) msg.getAttachment());
				return true;
			}
		}		
		return false;
	}

}
