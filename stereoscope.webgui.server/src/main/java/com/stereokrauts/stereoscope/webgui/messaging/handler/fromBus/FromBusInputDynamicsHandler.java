package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
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

public class FromBusInputDynamicsHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusInputDynamicsHandler(Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		final int chn = ((AbstractInputDynamicsMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor(); // NOPMD by th on 09.10.12 20:21
		if (chn == this.frontend.getState().getCurrentInput()) {
			if (msg instanceof MsgInputDynaAttack) {
				final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
				this.setInputDynaAttack(chn, dyna, msgInputDynaAttack.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaAutoOn) {
				final MsgInputDynaAutoOn msgInputDynaAutoOn = (MsgInputDynaAutoOn) msg;
				this.setInputDynaAutoOn(chn, dyna, msgInputDynaAutoOn.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaDecayRelease) {
				final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
				this.setInputDynaDecayRelease(chn, dyna,
						msgInputDynaDecayRelease.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaFilterFreq) {
				final MsgInputDynaFilterFreq msgInputDynaFilterFreq = (MsgInputDynaFilterFreq) msg;
				this.setInputDynaFilterFreq(chn, dyna,
						msgInputDynaFilterFreq.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaFilterOn) {
				final MsgInputDynaFilterOn msgInputDynaFilterOn = (MsgInputDynaFilterOn) msg;
				this.setInputDynaFilterOn(chn, dyna,
						msgInputDynaFilterOn.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaFilterQ) {
				final MsgInputDynaFilterQ msgInputDynaFilterQ = (MsgInputDynaFilterQ) msg;
				this.setInputDynaFilterQ(chn, dyna, msgInputDynaFilterQ.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaFilterType) {
				final MsgInputDynaFilterType msgInputDynaFilterType = (MsgInputDynaFilterType) msg;
				this.setInputDynaFilterType(chn, dyna,
						msgInputDynaFilterType.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaGain) {
				final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
				this.setInputDynaGain(chn, dyna, msgInputDynaGain.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaHold) {
				final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
				this.setInputDynaHold(chn, dyna, msgInputDynaHold.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaKeyIn) {
				final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
				this.setInputDynaKeyIn(chn, dyna, msgInputDynaKeyIn.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaKnee) {
				final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
				this.setInputDynaKnee(chn, dyna, msgInputDynaKnee.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaLeftSideChain) {
				final MsgInputDynaLeftSideChain msgInputDynaLeftSideChain = (MsgInputDynaLeftSideChain) msg;
				this.setInputDynaLeftSideChain(chn, dyna,
						msgInputDynaLeftSideChain.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaOn) {
				final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
				this.setInputDynaOn(chn, dyna, msgInputDynaOn.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaPair) {
				// on GenericMidsize and Pm5d this is called DynaLink
				final MsgInputDynaPair msgInputDynaPair = (MsgInputDynaPair) msg;
				this.setInputDynaPair(chn, dyna, msgInputDynaPair.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaRange) {
				final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
				this.setInputDynaRange(chn, dyna, msgInputDynaRange.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaRatio) {
				final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
				this.setInputDynaRatio(chn, dyna, msgInputDynaRatio.getAttachment());
				return true;
			} else if (msg instanceof MsgInputDynaThreshold) {
				final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
				this.setInputDynaThreshold(chn, dyna,
						msgInputDynaThreshold.getAttachment());
				return true;
			}
		}
		return false;
	}

	private void setInputDynaAttack(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/attack";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaAutoOn(int chn, int dyna, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/autoOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setInputDynaDecayRelease(int chn, int dyna, Float attachment) {
		String oscAddress = "";
		if (dyna == 0) {
			// decay
			oscAddress = Webclient.OSC_PREFIX 
					+ "stateful/input/dynamics/" + (dyna + 1) + "/decay";
		} else if (dyna == 1) {
			// release
			oscAddress = Webclient.OSC_PREFIX 
					+ "stateful/input/dynamics/" + (dyna + 1) + "/autoOn";
			
		}
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaFilterFreq(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/filterFreq";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaFilterOn(int chn, int dyna, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/filterOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setInputDynaFilterQ(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/filterQ";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaFilterType(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/filterType";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaGain(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/gain";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaHold(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/hold";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaKeyIn(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/keyIn";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaKnee(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/knee";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaLeftSideChain(int chn, int dyna, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/leftSideChain";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setInputDynaOn(int chn, int dyna, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/dynaOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setInputDynaPair(int chn, int dyna, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/pair";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setInputDynaRange(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/range";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaRatio(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/ratio";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setInputDynaThreshold(int chn, int dyna, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "stateful/input/dynamics/" + (dyna + 1) + "/threshold";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

}
