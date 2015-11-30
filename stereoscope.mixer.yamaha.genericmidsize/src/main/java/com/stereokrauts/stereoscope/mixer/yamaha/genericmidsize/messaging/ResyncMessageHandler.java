package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelNames;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelStrip;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncDelayTimes;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncEverything;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncGeqBandLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncOutputs;

public class ResyncMessageHandler extends HandlerForGenericMidsize implements IMessageHandler {

	public ResyncMessageHandler(final GenericMidsizeMixer mixer) {
		super(mixer);
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		if (msg instanceof ResyncChannelLevels) {
			mixer.getAllChannelLevels();
			return true;
		} else if (msg instanceof ResyncChannelNames) {
			mixer.getChannelNames();
			return true;
		} else if (msg instanceof ResyncChannelOnButtons) {
			mixer.getAllChannelOnButtons();
			return true;
		} else if (msg instanceof ResyncAuxSendLevels) {
			int aux = (int) msg.getAttachment();
			mixer.getAllAuxLevels(aux);
			return true;
		} else if (msg instanceof ResyncAuxSendOnButtons) {
			int aux = (int) msg.getAttachment();
			mixer.getAllAuxChannelOn(aux);
			return true;
		} else if (msg instanceof ResyncChannelStrip) {
			int chn = (int) msg.getAttachment();
			mixer.getAllInputValues(chn);
			return true;
		} else if (msg instanceof ResyncGeqBandLevels) {
			byte geq = (byte) msg.getAttachment();
			mixer.getAllGeqLevels(geq);
			return true;
		} else if (msg instanceof ResyncDelayTimes) {
			mixer.getAllDelayTimes();
			return true;
		} else if (msg instanceof ResyncOutputs) {
			int aux = ((ResyncOutputs) msg).getCurrentAux();
			this.getOutputs(aux);
			return true;
		} else if (msg instanceof ResyncEverything) {
			this.getAllMixerValues((ResyncEverything) msg);
			return true;
		}
		return false;
	}
	
	private void getOutputs(int aux) {
		mixer.getOutputMaster();
		mixer.getAuxMaster(aux);
	}

	private void getAllMixerValues(final ResyncEverything msg) {
		int currentAux = msg.getCurrentAux();
		int currentInput = msg.getCurrentInput();
		int currentGeq = msg.getCurrentGeq();
		mixer.getAllChannelLevels();
		mixer.getChannelNames();
		mixer.getAllChannelOnButtons();
		mixer.getAllDelayTimes();
		mixer.getAllAuxLevels(currentAux);
		mixer.getAllAuxChannelOn(currentAux);
		mixer.getAllInputValues(currentInput);
		mixer.getAllGeqLevels((byte) currentGeq);
	}
	
}
