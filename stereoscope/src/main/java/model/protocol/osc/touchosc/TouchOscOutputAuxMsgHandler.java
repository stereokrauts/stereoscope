package model.protocol.osc.touchosc;

import java.util.ArrayList;
import java.util.List;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.handler.MessageHandleStatus;
import model.protocol.osc.handler.OscOutputAuxMsgHandler;
import model.surface.touchosc.TouchOscSurface;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;

/**
 * This class is a special implementation for aux OSC messages, as 
 * TouchOSC surfaces handle user inputs with special care (snap fader feature).
 * @author theide
 *
 */
public final class TouchOscOutputAuxMsgHandler extends OscOutputAuxMsgHandler {
	public class AuxIdentifier {
		private final int aux;
		private final int channel;

		AuxIdentifier(final int aux, final int channel) {
			this.aux = aux;
			this.channel = channel;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + aux;
			result = prime * result + channel;
			return result;
		}
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final AuxIdentifier other = (AuxIdentifier) obj;
			if (aux != other.aux) {
				return false;
			}
			if (channel != other.channel) {
				return false;
			}
			return true;
		}

	}

	private TouchOscSurface surface;

	public TouchOscOutputAuxMsgHandler(final boolean snapFaderActive, final TouchOscSurface srf) {
		this(snapFaderActive);
		this.surface = srf;
	}

	public void setSurface(final TouchOscSurface surface2) {
		this.surface = surface2;
	}

	private final SnappingFaderManager snappingFaderManager;

	public TouchOscOutputAuxMsgHandler(final boolean snapFaderActive) {
		final List<AuxIdentifier> channelIdentifiers = createAuxIdentifiers();
		snappingFaderManager = new SnappingFaderManager(snapFaderActive, channelIdentifiers);
	}

	private List<AuxIdentifier> createAuxIdentifiers() {
		final List<AuxIdentifier> ids = new ArrayList<>();
		for (int channel = 0; channel < TouchOscSurface.MAX_CHANNELS; channel++) {
			for (int aux = 0; aux < TouchOscSurface.MAX_AUX; aux++) {
				ids.add(new AuxIdentifier(aux, channel));
			}
		}
		return ids;
	}

	@Override
	public MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/.*/level/fromChannel/[0-9]+(/z)?$")) {
			this.handleLevelMessage(msg);
			return MessageHandleStatus.MESSAGE_HANDLED;
		} else {
			return super.handleOscMessage(msg);
		}
	}


	private void handleLevelMessage(final IOscMessage msg) {
		LOG.info("Level message recognized: " + msg);
		final boolean isZmessage = this.isZMessage(msg);

		final String[] spliter = OscAddressUtil.stringify(msg.address()).split("/");
		final int chn = Integer.parseInt(spliter[6]) - 1;

		final int aux = this.surface.getState().getCurrentAux();
		final float value = OscObjectUtil.toFloat(msg.get(0));

		if (!isZmessage) {
			final float receivedValue = OscObjectUtil.toFloat(msg.get(0));
			final AuxIdentifier auxIdentifier = new AuxIdentifier(aux, chn);
			snappingFaderManager.tryUpdate(auxIdentifier, receivedValue, new ISnappingFaderEventHandler() {
				@Override
				public void snapSucceeded() {
					surface.fireChanged(new MsgAuxSendChanged(chn, aux, value));
					makeFaderGreen(chn);					
				}

				@Override
				public void snapFailed() {
					makeFaderYellow(chn);
				}
			});
		}
	}

	private void makeFaderGreen(final int chn) {
		this.surface.sendChangeColor(OscMessageRelay.OSC_PREFIX + "/stateful/aux/level/fromChannel/"
				+ (chn + 1)	+ "/color", "green");
	}

	private void makeFaderYellow(final int chn) {
		this.surface.sendChangeColor(OscMessageRelay.OSC_PREFIX + "/stateful/aux/level/fromChannel/"
				+ (chn + 1)	+ "/color", "yellow");
	}

	public void setCurrentAuxFaderValues(final int aux, final int channel, final float value) {
		final AuxIdentifier auxIdentifier = new AuxIdentifier(aux, channel);
		this.snappingFaderManager.forceUpdate(auxIdentifier, value);
	}

	public void setSnapActive(final boolean selected) {
		snappingFaderManager.setSnapFaderActive(selected);
	}
}
