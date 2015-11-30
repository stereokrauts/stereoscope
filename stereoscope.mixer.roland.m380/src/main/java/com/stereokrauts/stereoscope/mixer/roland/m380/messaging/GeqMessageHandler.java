package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;


public class GeqMessageHandler extends HandlerForM380 implements IMessageHandler {

	public GeqMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all graphical EQ related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */

	@Override
	public boolean handleMessage(final IMessage msg) {
		final short geqNumber = ((AbstractGeqMessage<?>) msg).getGeqNumber();
		if (msg instanceof MsgGeqFullReset) {
			this.mixer.getModifier().changedGeqFullReset(geqNumber);
			return true;
		} else if (msg instanceof MsgGeqBandLevelChanged) {
			final MsgGeqBandLevelChanged msgGeqBandLevelChanged = (MsgGeqBandLevelChanged) msg;
			final boolean rightChannel = msgGeqBandLevelChanged.isRightChannel();
			final int band = msgGeqBandLevelChanged.getBand();
			final float floatValue = msgGeqBandLevelChanged.getAttachment();
			this.mixer.getModifier().changedGeqBandLevel(geqNumber, rightChannel, band, floatValue);
			return true;
		} else if (msg instanceof MsgGeqBandReset) {
			final MsgGeqBandReset msgGeqBandReset = (MsgGeqBandReset) msg;
			final boolean rightChannel = msgGeqBandReset.isRightChannel();
			final int band = msgGeqBandReset.getBand();
			this.mixer.getModifier().changedGeqBandReset(geqNumber, rightChannel, band);
			return true;
		}
		return false;
	}

}
