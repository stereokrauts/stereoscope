package model.protocol.osc.touchosc;

import java.util.ArrayList;
import java.util.List;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.handler.MessageHandleStatus;
import model.protocol.osc.handler.OscInputChannelMsgHandler;
import model.surface.touchosc.TouchOscSurface;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;

/**
 * This handler is for Touch OSC specific messages that chang
 * the channel controls and implements the snap fader feature.
 */
public class TouchOscInputChannelMsgHandler extends OscInputChannelMsgHandler {
	private TouchOscSurface surface;

	public final void setSurface(final TouchOscSurface surface2) {
		this.surface = surface2;
	}

	private final SnappingFaderManager snappingFaderManager;

	public TouchOscInputChannelMsgHandler(final boolean snapFaderActive) {
		final List<Integer> channelIdentifiers = createChannelIdentifiers();
		snappingFaderManager = new SnappingFaderManager(snapFaderActive, channelIdentifiers);
	}

	private List<Integer> createChannelIdentifiers() {
		final List<Integer> ids = new ArrayList<>();
		for (int i = 0; i < TouchOscSurface.MAX_CHANNELS; i++) {
			ids.add(i);
		}
		return ids;
	}

	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		if (OscAddressUtil.stringify(msg.address()).matches(".*/input/[0-9]+/level(/z)?$")) {
			this.handleLevelMessage(msg);
			return MessageHandleStatus.MESSAGE_HANDLED;
		} else {
			return super.handleOscMessage(msg);
		}
	}

	private void handleLevelMessage(final IOscMessage msg) {
		final boolean isZmessage = this.isZMessage(msg);

		final String[] spliter = OscAddressUtil.stringify(msg.address()).split("/");
		final int chn = Integer.parseInt(spliter[3]) - 1;
		final float receivedValue = OscObjectUtil.toFloat(msg.get(0));

		if (this.isZMessagePushed(msg)) {
			snappingFaderManager.updateStrategy_ForceWhenHit(chn, receivedValue);
		} else if (this.isZMessageReleased(msg)) {
			snappingFaderManager.updateStrategy_Try(chn);
		}

		if (!isZmessage) {
			snappingFaderManager.tryUpdate(chn, receivedValue, new ISnappingFaderEventHandler() {
				@Override
				public void snapSucceeded() {
					surface.fireChanged(new MsgChannelLevelChanged(chn, receivedValue));
					makeFaderRed(chn);
				}

				@Override
				public void snapFailed() {
					makeFaderYellow(chn);
				}
			});
		}
	}

	private void makeFaderRed(final int chn) {
		this.surface.sendChangeColor(OscMessageRelay.OSC_PREFIX + "/input/" + (chn + 1)
				+ "/level/color", "red");
	}

	private void makeFaderYellow(final int chn) {
		this.surface.sendChangeColor(OscMessageRelay.OSC_PREFIX + "/input/" + (chn + 1)
				+ "/level/color", "yellow");
	}

	public void setCurrentChannelFaderValues(final int chn, final float level) {
		snappingFaderManager.forceUpdate(chn, level);
	}

	public void setSnapActive(final boolean selected) {
		snappingFaderManager.setSnapFaderActive(selected);
	}
}
