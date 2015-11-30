package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;


public class InputMessageHandler extends HandlerForM380 implements IMessageHandler {
	public InputMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all basic channel strip related
	 * messages from the function handleNotification.
	 * @param message The message that should be handled by this function.
	 */
	@Override
	public boolean handleMessage(final IMessage message) {
		final AbstractInputMessage<?> msg = (AbstractInputMessage<?>) message;
		final int chn = msg.getChannel();
		
		if (msg instanceof MsgInputPan) {
			final MsgInputPan msgInputPanning = (MsgInputPan) msg;
			if (this.mixer.getLabelMaker() != null) {
				final String label = this.mixer.getLabelMaker().getPanningLabel(msgInputPanning.getAttachment());
				this.mixer.getModifier().changedInputPan(chn, msgInputPanning.getAttachment());
				this.mixer.fireChange(new MsgInputPanLabel(chn, 0, label));
				return true;
			}
		} else if (msg instanceof MsgInputPeqOnChanged) {
			final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
			this.mixer.getModifier().changedPeqOn(chn, msgPeqOnChanged.getAttachment());
			return true;
		
		}
		return false;
	}
}
