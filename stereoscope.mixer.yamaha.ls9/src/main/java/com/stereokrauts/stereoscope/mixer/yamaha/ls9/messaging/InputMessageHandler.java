package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;


public class InputMessageHandler extends HandlerForLs9 implements IMessageHandler {
	public InputMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all basic channel strip related
	 * messages from the function handleNotification.
	 * @param message The message that should be handled by this function.
	 */
	@Override
	public final boolean handleMessage(final IMessage message) {
		final AbstractInputMessage<?> msg = (AbstractInputMessage<?>) message;
		final int chn = msg.getChannel();
		
		if (msg instanceof MsgInputPan) {
			final MsgInputPan msgInputPanning = (MsgInputPan) msg;
			if (this.mixer.getLabelMaker() != null) {
				final String label = this.mixer.getLabelMaker().getYamahaLabelPanning(msgInputPanning.getAttachment());
				this.mixer.getModifier().changedInputPan(chn, msgInputPanning.getAttachment());
				this.mixer.fireChange(new MsgInputPanLabel(chn, 0, label));
				return true;
			}
		} else if (msg instanceof MsgInputPeqOnChanged) {
			final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
			this.mixer.getModifier().changedPeqOn(chn, msgPeqOnChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgInputPeqModeChanged) {
			final MsgInputPeqModeChanged msgPeqModeChanged = (MsgInputPeqModeChanged) msg;
			this.mixer.getModifier().changedPeqMode(chn, msgPeqModeChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgInputPeqLPFChanged) {
			final MsgInputPeqLPFChanged msgPeqLPFChanged = (MsgInputPeqLPFChanged) msg;
			this.mixer.getModifier().changedPeqLPFOn(chn, msgPeqLPFChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgInputPeqHPFChanged) {
			final MsgInputPeqHPFChanged msgPeqHPFChanged = (MsgInputPeqHPFChanged) msg;
			this.mixer.getModifier().changedPeqHPFOn(chn, msgPeqHPFChanged.getAttachment());
			return true;
		}
		return false;
	}
}
