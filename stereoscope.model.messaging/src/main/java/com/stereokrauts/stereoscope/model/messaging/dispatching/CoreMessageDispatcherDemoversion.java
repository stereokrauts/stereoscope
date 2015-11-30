package com.stereokrauts.stereoscope.model.messaging.dispatching;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;


/**
 * This class limits the forwarded messages to a few channels,
 * auxes and graphical equalizers for the demonstration version.
 * @author theide
 *
 */
final class CoreMessageDispatcherDemoversion extends
		CoreMessageDispatcher {
	private static final int DEMOVERSION_MAXGEQ = 2;
	private static final int DEMOVERSION_MAXCHANNELS = 4;
	
	
	@Override
	public void handleNotification(final IMessageWithSender message) {
		if (message instanceof MessageWithSender) {
			final AbstractMessage<?> msg = ((MessageWithSender) message).getMessage();
			if (this.anyLimitationViolated(msg)) {
				return;
			}
			super.handleNotification(message);
		}
	}

	/**
	 * @param msg
	 * @return true if any of the demo limitations is violated by the
	 * message
	 */
	private boolean anyLimitationViolated(final AbstractMessage<?> msg) {
		return this.channelBoundsViolated(msg)
			|| this.geqBoundsViolated(msg)
			|| this.channelStripBoundsViolated(msg)
			|| this.dynamicsBoundsViolated(msg)
			|| this.peqBoundsViolated(msg)
			|| this.labelBoundsViolated(msg);
	}

	/**
	 * @param msg
	 * @return
	 */
	private boolean peqBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractInputPeqBandMessage<?> && ((AbstractInputPeqBandMessage<?>) msg).getChannel() >= DEMOVERSION_MAXCHANNELS;
	}

	/**
	 * @param msg
	 * @return
	 */
	private boolean dynamicsBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractInputDynamicsMessage<?> && ((AbstractInputDynamicsMessage<?>) msg).getChannel() >= DEMOVERSION_MAXCHANNELS;
	}

	/**
	 * @param msg
	 * @return
	 */
	private boolean channelStripBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractInputMessage<?> && ((AbstractInputMessage<?>) msg).getChannel() >= DEMOVERSION_MAXCHANNELS;
	}

	/**
	 * @param msg
	 * @return
	 */
	private boolean geqBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractGeqMessage<?> && ((AbstractGeqMessage<?>) msg).getGeqNumber() >= DEMOVERSION_MAXGEQ;
	}

	/**
	 * @param msg
	 * @return
	 */
	private boolean channelBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractChannelMessage<?> && ((AbstractChannelMessage<?>) msg).getChannel() >= DEMOVERSION_MAXCHANNELS;
	}
	
	/**
	 * @param msg
	 * @return
	 */
	private boolean labelBoundsViolated(final AbstractMessage<?> msg) {
		return msg instanceof AbstractLabelMessage<?> && ((AbstractLabelMessage<?>) msg).getChannel() >= DEMOVERSION_MAXCHANNELS;
	}
}
