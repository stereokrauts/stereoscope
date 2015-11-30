package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusOutputHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusOutputHandler(final Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber();
		if (section == SECTION.AUX) {
			if (msg instanceof MsgAuxMasterLevelChanged) {
				this.setAuxMasterLevel(number, (Float) msg.getAttachment());
				this.setAuxMasterLevelStateful(number, (Float) msg.getAttachment());
			} else if (msg instanceof MsgAuxMasterDelayChanged) {
				this.setAuxDelayTime(number, (Float) msg.getAttachment());
			}
			return true;
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterLevelChanged) {
				this.setBusMasterLevel(number, (Float) msg.getAttachment());
			} else if (msg instanceof MsgBusMasterDelayChanged) {
				this.setBusDelayTime(number, (Float) msg.getAttachment());
			}
			return true;
		} else if (section == SECTION.OUTPUT) {
			if (msg instanceof MsgOutputDelayChanged) {
				this.setOutputDelayTime(number, (Float) msg.getAttachment());
			}
			return true;
		} else if (section == SECTION.MASTER && msg instanceof MsgMasterLevelChanged) {
			this.setMasterLevel((Float) msg.getAttachment());
			return true;
		}
		return false;
	}

	private void setAuxMasterLevel(int aux, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "output/aux/" + (aux + 1) + "/level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
	}
	
	private void setAuxMasterLevelStateful(int aux, Float attachment) {
		if ((aux) == this.frontend.getState().getCurrentAux()) {
			String oscAddress = Webclient.OSC_PREFIX
					+ "stateful/aux/level";
			this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		}
	}

	private void setAuxDelayTime(int aux, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "output/aux/" + (aux + 1) + "/delay";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setBusMasterLevel(int bus, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "output/bus/" + (bus + 1) + "/level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setBusDelayTime(int bus, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "output/bus/" + (bus + 1) + "/delay";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setOutputDelayTime(int output, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "output/omni/" + (output + 1) + "/delay";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setMasterLevel(Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "output/master/level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

}
