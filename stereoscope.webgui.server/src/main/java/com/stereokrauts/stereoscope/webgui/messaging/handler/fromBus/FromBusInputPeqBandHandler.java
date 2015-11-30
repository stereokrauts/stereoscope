package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import java.util.ArrayList;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusInputPeqBandHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusInputPeqBandHandler(Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		final int chn = ((AbstractInputPeqBandMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		final int band = ((AbstractInputPeqBandMessage<?>) msg).getBand(); // NOPMD by th on 09.10.12 20:21
		if (chn == this.frontend.getState().getCurrentInput()) {
			if (msg instanceof MsgInputPeqQ) {
				final MsgInputPeqQ msgInputPeqQ = (MsgInputPeqQ) msg;
				this.setPeqBandQ(chn, band, msgInputPeqQ.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqF) {
				final MsgInputPeqF msgInputPeqF = (MsgInputPeqF) msg;
				this.setPeqBandF(chn, band, msgInputPeqF.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqG) {
				final MsgInputPeqG msgInputPeqG = (MsgInputPeqG) msg;
				this.setPeqBandG(chn, band, msgInputPeqG.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqFilterType) {
				final MsgInputPeqFilterType msgInputPeqFilterCharacteristics = 
						(MsgInputPeqFilterType) msg;
				this.setInputPeqFilterCharacteristics(chn, band, 
						msgInputPeqFilterCharacteristics.getAttachment());
				return true;
			}
		}
		return false;
	}

	private void setPeqBandQ(int chn, int band, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/band/" + (band + 1) + "/q";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
	}

	private void setPeqBandF(int chn, int band, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/band/" + (band + 1) + "/f";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
	}

	private void setPeqBandG(int chn, int band, Float attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/band/" + (band + 1) + "/g";
		final float gain = (attachment / Webclient.CENTER_CONTROL_SCALE_FACTOR)
				+ Webclient.CENTER_CONTROL_OFFSET;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, gain);
		
	}

	private void setInputPeqFilterCharacteristics(int chn, int band,
			Integer attachment) {
		final ArrayList<String> filterType = new ArrayList<String>();
		filterType.add("parametric");
		filterType.add("lowShelf");
		filterType.add("hiShelf");
		filterType.add("lpf");
		filterType.add("hpf");
		final String activeType = filterType.get(attachment); // NOPMD by th on 09.10.12 20:21
		filterType.remove(attachment);
		
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/band/" + (band + 1)
				+ "/type/" + activeType;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, 1);
		
		for (int i = 0; i < filterType.size(); i++) {
			final String inactiveType = filterType.get(i);
			oscAddress = Webclient.OSC_PREFIX
					+ "stateful/input/peq/band/" + (band + 1)
					+ "/type/" + inactiveType;
			this.frontend.getFrontendModifier().sendToFrontend(oscAddress, 0);
		}
		
	}
}
