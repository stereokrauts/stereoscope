package model.protocol.osc.touchosc;

import model.protocol.osc.DelayedOscMessageSender;
import model.protocol.osc.IOscMessage;
import model.surface.OscMessageSender;
import model.surface.touchosc.TouchOscSurface;

/**
 * This class implements TouchOSC specific behaviour when sending
 * messages.
 */
public final class TouchOscMessageSender extends OscMessageSender {
	private static final int OSC_MESSAGE_DELAY = 5;

	private final TouchOscSurface surface;
	private final DelayedOscMessageSender messageSender;

	public TouchOscMessageSender(final TouchOscSurface srf) {
		super(srf);
		this.surface = srf;
		this.messageSender = new DelayedOscMessageSender(this.surface, OSC_MESSAGE_DELAY);
	}

	@Override
	protected void setChannelLevel(final int chn, final float level) {
		super.setChannelLevel(chn, level);
		this.surface.getInputHandler().setCurrentChannelFaderValues(chn, level);
	}

	@Override
	protected void setChannelAuxLevel(final int chn, final int aux, final float level) {
		super.setChannelAuxLevel(chn, aux, level);
		this.surface.getOutputAuxHandler().setCurrentAuxFaderValues(aux, chn, level);
	}

	@Override
	protected void send(final IOscMessage msg) {
		this.messageSender.sendMessage(msg);
	}

	@Override
	protected void shutdown() {
		this.messageSender.shutdown();
	}
}
