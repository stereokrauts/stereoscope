package model.protocol.osc.handler;

import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscConstants;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;

/**
 * This handler is for OSC messages that change the graphical equalizer controls.
 */
public class OscGraphicalEqMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/dsp/geq/(.*)",
		"^" + OscMessageRelay.OSC_PREFIX + "/stateful/dsp/geq/(.*)",
	};

	// for doubletouch
	private long firstClickTime = 0;     // time of 1st message
	private long secondClickTime = 0;     // time of 2nd message
	private static final int DOUBLETOUCH_SPEED = 250; // max touch delay (ms)
	private boolean isSecondClick = false; // binary counter (1st or 2nd touch)


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);

		if (this.isZMessage(msg)) {
			LOG.info("Message ignored - was z-Message");
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}

		if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/.*/band/[0-9]+/.*/level$")) {
			this.handleBandLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/.*/band/[0-9]+/.*/reset$")) {
			this.handleBandResetMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/.*/resetGeq$")) {
			this.handleGeqResetMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleGeqResetMessage(final IOscMessage msg) {
		if (OscObjectUtil.toFloat(msg.get(0)) == 1) {
			if (!this.isSecondClick) {
				this.firstClickTime = System.currentTimeMillis();			
				this.isSecondClick = true;
			} else {
				this.secondClickTime = System.currentTimeMillis();
				final long tDiff = this.secondClickTime - this.firstClickTime;
				if (tDiff < DOUBLETOUCH_SPEED) {
					this.getSurface().fireChanged(new MsgGeqFullReset(this.getSurface().getState().getCurrentGEQ(), null));
					if (this.getSurface().getBus().getTheMixer() instanceof IMixerWithGraphicalEq) {
						((IMixerWithGraphicalEq) this.getSurface().getBus().getTheMixer()).getAllGeqLevels(this.getSurface().getState().getCurrentGEQ());
					}
				}
				this.isSecondClick = false;
			}
		}
	}

	private void handleBandResetMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final boolean isRight = splitted[7].equals("right");
		final int bandNumber = Integer.parseInt(splitted[6]);

		this.getSurface().fireChanged(new MsgGeqBandReset(this.getSurface().getState().getCurrentGEQ(), bandNumber - 1, isRight, null));
		if (this.getSurface().getBus().getTheMixer() instanceof IMixerWithGraphicalEq) {
			((IMixerWithGraphicalEq) this.getSurface().getBus().getTheMixer()).getGeqBandLevel(this.getSurface().getState().getCurrentGEQ(), isRight, (bandNumber - 1));
		}
	}

	private void handleBandLevelMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final boolean isRight = splitted[7].equals("right");
		final int bandNumber = Integer.parseInt(splitted[6]);

		final float level = (OscObjectUtil.toFloat(msg.get(0)) - OscConstants.CENTER_CONTROL_OFFSET) * OscConstants.CENTER_CONTROL_SCALE_FACTOR;
		this.getSurface().fireChanged(new MsgGeqBandLevelChanged(this.getSurface().getState().getCurrentGEQ(), bandNumber - 1, isRight, level));
	}

}
