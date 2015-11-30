package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxAllSendLevels;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxAllSendOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQAllBandLevel;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQIsFlexEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputAllLevels;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class StateMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/system/state/.*/changeTo/.*$"
	};
	
	public StateMsgHandler(Webclient frontend) {
		super(frontend);
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg.getOscAddress());
		if (msg.getOscAddress().matches(".*/selectedAux/.*")) {
			this.handleAuxSelectionMessage(msg);
		} else if (msg.getOscAddress().matches(".*/selectedGeq/.*")) {
			this.handleGeqSelectionMessage(msg);
		} else if (msg.getOscAddress().matches(".*/selectedInput/.*")) {
			this.handleInputSelectionMessage(msg);
		} else {
			return false;
		}
		return true;
	}
	
	private void handleAuxSelectionMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final byte auxNumber = Byte.parseByte(splitted[6]);
		this.getFrontend().getState().setCurrentAux((byte) (auxNumber - 1));
		this.updateClientAuxStateButtons(auxNumber - 1);
		this.requestAuxParameters(auxNumber - 1);
	}

	private void handleGeqSelectionMessage(final FrontendMessage msg) {
		if (this.getFrontend().getState().isMixerWithGraphicalEQ()) {
			final String[] splitted = msg.getOscAddress().split("/");
			final byte eqNumber = Byte.parseByte(splitted[6]);
			this.getFrontend().getState().setCurrentGEQ((byte) (eqNumber - 1));
			this.updateClientGeqStateButtons(eqNumber -1);
			this.requestGEQParameters(eqNumber - 1);
		}
	}

	private void handleInputSelectionMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final byte inputNumber = Byte.parseByte(splitted[6]);
		this.getFrontend().getState().setCurrentInput((byte) (inputNumber - 1));
		this.updateClientInputStateButtons(inputNumber - 1);
		this.requestInputParameters(inputNumber - 1);
	}

	private final synchronized void updateClientAuxStateButtons(int currentAux) {
		String labelText = "You are on AUX " + (currentAux + 1);;
		final String labelAddress = Webclient.OSC_PREFIX
				+ "system/state/selectedAux/label";
		
		for (int i = 0; i < this.getFrontend().getState().getAuxCount(); i++) {
			float switchButtonValue;
			final String oscAddress = Webclient.OSC_PREFIX
					+ "system/state/selectedAux/changeTo/" + (i + 1);
			if (i != currentAux) {
				switchButtonValue = 0;
			} else {
				switchButtonValue = 1;
			}
			this.getFrontend().getFrontendModifier().sendToFrontend(oscAddress, switchButtonValue);
		}
		this.getFrontend().getFrontendModifier().sendToFrontend(labelAddress, labelText);
	}
	
	private final synchronized void requestAuxParameters(int currentAux) {
		this.getFrontend().fireChange(new RequestMixerAuxAllSendLevels(currentAux, true));
		this.getFrontend().fireChange(new RequestMixerAuxAllSendOnButtons(currentAux, true));
		
	}
	
	private final synchronized void updateClientGeqStateButtons(int currentGeq) {
		for (int i = 0; i < this.getFrontend().getState().getGeqCount(); i++) {
			float switchButtonValue;
			final String oscAddress = Webclient.OSC_PREFIX 
					+ "system/state/selectedGeq/changeTo/" + (i + 1);
			
			if (i != currentGeq) {
				switchButtonValue = 0;
			} else {
				switchButtonValue = 1;
			}
			this.getFrontend().getFrontendModifier().sendToFrontend(oscAddress, switchButtonValue);
		}
	}
	
	private final synchronized void requestGEQParameters(int currentGeq) {
		this.getFrontend().fireChange(new RequestMixerGEQIsFlexEQ(currentGeq, true));
		this.getFrontend().fireChange(new RequestMixerGEQAllBandLevel(currentGeq, true));
	}
	
	private final synchronized void updateClientInputStateButtons(int input) {
		final String labelText = "You are on input " + (input + 1);
		final String labelAddress = Webclient.OSC_PREFIX
				+ "system/state/selectedInput/label";
		
		for (int i = 0; i < this.getFrontend().getState().getInputCount(); i++) {
			float switchButtonValue;
			final String oscAddress = Webclient.OSC_PREFIX
					+ "system/state/selectedInput/changeTo/" + (i + 1);
			if (i != input) {
				switchButtonValue = 0;
			} else {
				switchButtonValue = 1;
			}
			this.getFrontend().getFrontendModifier().sendToFrontend(oscAddress, switchButtonValue);
		}
		this.getFrontend().getFrontendModifier().sendToFrontend(labelAddress, labelText);
	}

	private final synchronized void requestInputParameters(int i) {
		this.getFrontend().fireChange(new RequestMixerInputAllLevels(true));
	}

}
