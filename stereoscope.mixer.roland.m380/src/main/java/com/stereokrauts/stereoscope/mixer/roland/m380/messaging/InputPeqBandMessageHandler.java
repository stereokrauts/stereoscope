package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;


public class InputPeqBandMessageHandler extends HandlerForM380 implements
		IMessageHandler {

	public InputPeqBandMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all channel parametric eq related
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public boolean handleMessage(final IMessage msg) {
		final int chn = ((AbstractInputPeqBandMessage<?>) msg).getChannel();

		if (msg instanceof MsgInputPeqQ) {
			final MsgInputPeqQ msgInputPeqQ = (MsgInputPeqQ) msg;
			final int band = msgInputPeqQ.getBand();
			this.mixer.getModifier().changedPeqBandQ(chn, band, msgInputPeqQ.getAttachment());
			return true;
		} else if (msg instanceof MsgInputPeqF) {
			final MsgInputPeqF msgInputPeqF = (MsgInputPeqF) msg;
			final int band = msgInputPeqF.getBand();
			this.mixer.getModifier().changedPeqBandF(chn, band, msgInputPeqF.getAttachment());
			return true;
		} else if (msg instanceof MsgInputPeqG) {
			final MsgInputPeqG msgInputPeqG = (MsgInputPeqG) msg;
			final int band = msgInputPeqG.getBand();
			this.mixer.getModifier().changedPeqBandG(chn, band, msgInputPeqG.getAttachment());
			return true;
		}	
		return false;
	}

}
