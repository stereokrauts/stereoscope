package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAutoOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterFreq;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterQ;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaLeftSideChain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaPair;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class ChannelStripDynamicsMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/stateful/input/dynamics/(.*)",
	};
	
	public ChannelStripDynamicsMsgHandler(Webclient frontend) {
		super(frontend);
	}
	
	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg);
		final int selectedInput = this.getFrontend().getState().getCurrentInput();
		final String[] splitted = msg.getOscAddress().split("/");
		final int processorNr = Short.parseShort(splitted[5]) - 1;
		final float value = msg.getFloatValue();
		
		if (msg.getOscAddress().matches(".*/dynaOn$")) {
			this.getFrontend().fireChange(new MsgInputDynaOn(processorNr, selectedInput, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/autoOn$")) {
			this.getFrontend().fireChange(new MsgInputDynaAutoOn(processorNr, selectedInput, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/leftSideChain$")) {
			this.getFrontend().fireChange(new MsgInputDynaLeftSideChain(processorNr, selectedInput, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/filterOn$")) {
			this.getFrontend().fireChange(new MsgInputDynaFilterOn(processorNr, selectedInput, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/pair$")) {
			this.getFrontend().fireChange(new MsgInputDynaPair(processorNr, selectedInput, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/attack$")) {
			this.getFrontend().fireChange(new MsgInputDynaAttack(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/(decay|release)$")) {
			this.getFrontend().fireChange(new MsgInputDynaDecayRelease(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/gain$")) {
			this.getFrontend().fireChange(new MsgInputDynaGain(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/hold$")) {
			this.getFrontend().fireChange(new MsgInputDynaHold(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/knee$")) {
			this.getFrontend().fireChange(new MsgInputDynaKnee(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/range$")) {
			this.getFrontend().fireChange(new MsgInputDynaRange(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/ratio$")) {
			this.getFrontend().fireChange(new MsgInputDynaRatio(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/threshold$")) {
			this.getFrontend().fireChange(new MsgInputDynaThreshold(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/keyIn$")) {
			this.getFrontend().fireChange(new MsgInputDynaKeyIn(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/filterType$")) {
			this.getFrontend().fireChange(new MsgInputDynaFilterType(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/filterFreq$")) {
			this.getFrontend().fireChange(new MsgInputDynaFilterFreq(processorNr, selectedInput, value));
		} else if (msg.getOscAddress().matches(".*/filterQ$")) {
			this.getFrontend().fireChange(new MsgInputDynaFilterQ(processorNr, selectedInput, value));
		}
		
		return true;
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


}
