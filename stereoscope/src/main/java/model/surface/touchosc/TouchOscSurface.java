package model.surface.touchosc;

import model.beans.OscSurfaceBean;
import model.properties.IPropertyProvider;
import model.properties.PropertiesException;
import model.properties.PropertyCollection;
import model.properties.beans.PropertyHandler;
import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.handler.OscDetectOldFrontends;
import model.protocol.osc.impl.OscMessage;
import model.protocol.osc.touchosc.TouchOscInputChannelMsgHandler;
import model.protocol.osc.touchosc.TouchOscMessageSender;
import model.protocol.osc.touchosc.TouchOscOutputAuxMsgHandler;
import model.surface.OscSurface;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class implements specialties of TouchOSC clients.
 * @author theide
 *
 */
public final class TouchOscSurface extends OscSurface implements IPropertyProvider {
	public static final int MAX_CHANNELS = 256;
	public static final int LAYOUT_64CHANNELS = 64;
	public static final int LAYOUT_96CHANNELS = 96;
	private static final SLogger LOG = StereoscopeLogManager
			.getLogger("touchosc-surface");
	public static final int MAX_AUX = 32;

	private final OscDetectOldFrontends detectOldFrontendsHandler;
	private TouchOscHeartbeatDisplay heartbeat;
	private final TouchOscOutputAuxMsgHandler auxHandler;
	private final TouchOscInputChannelMsgHandler inputHandler;

	public TouchOscSurface(final Boolean isSnapFaderActive) {
		super();
		LOG.info("TouchOscSurface: injecting own message sender into OscSurface");
		this.sender = new TouchOscMessageSender(this);

		LOG.info("TouchOscSurface: injecting own message handlers into OscRelay");
		this.detectOldFrontendsHandler = new OscDetectOldFrontends();
		this.oscRelay.registerIOscMessageHandler(this.detectOldFrontendsHandler);

		/* replace two handlers by our own */
		this.oscRelay.unregisterIOscMessageHandler(super.getInputHandler());
		inputHandler = new TouchOscInputChannelMsgHandler(isSnapFaderActive);
		super.setInputHandler(inputHandler);
		this.getInputHandler().setSurface(this);
		this.oscRelay.registerIOscMessageHandler(this.getInputHandler());

		this.oscRelay.unregisterIOscMessageHandler(super.getOutputAuxHandler());
		auxHandler = new TouchOscOutputAuxMsgHandler(isSnapFaderActive);
		super.setOutputAuxHandler(auxHandler);
		this.getOutputAuxHandler().setSurface(this);
		this.oscRelay.registerIOscMessageHandler(this.getOutputAuxHandler());
	}

	public TouchOscSurface(final OscSurfaceBean b) {
		this(b.getClientConfig().getSnapFaders());
		this.setBean(b);
		b.getObserverManager().addObserver(this);
	}

	@Override
	public synchronized void connect() {
		super.connect();
		this.heartbeat = new TouchOscHeartbeatDisplay(this);
		new Thread(this.heartbeat).start();
		try {
			this.hideTouchOscElements();
		} catch (final Exception e) {
			e.printStackTrace();
			LOG.info("TouchOscSurface: Hiding of unused elements failed.");
		}
	}


	@Override
	public void disconnect() {
		this.heartbeat.stop();
		super.disconnect();
	}

	/**
	 * @deprecated This should be made prettier and maybe even
	 * moved to control logic.
	 * 
	 * @throws Exception
	 */
	@Deprecated
	void hideTouchOscElements() throws Exception {
		if (this.connection.isConnected()) {

			final int channels = this.getBus().getTheMixer().getChannelCount();
			// first take care that everything is on the screen
			// in case someone switched to a mixer with more channels
			for (int i = 0; i <= LAYOUT_64CHANNELS; i++) {
				// statefull channel switch buttons
				final String chButton = "system/state/selectedInput/changeTo/"
						+ (i + 1) + "/";
				final String chButtonLabel = chButton + "label";
				// to-stereo-view elements
				final String chFader = "input/" + (i + 1) + "/level";
				final String chFaderLabel = "input/" + (i + 1) + "/levelLabel";
				final String chOnButton = "input/" + (i + 1) + "/channelOn";
				final String chLabel = "input/" + (i + 1) + "/label";
				// aux elements
				final String auxFader = "stateful/aux/level/fromChannel/" + (i + 1);

				this.sender.setControlElementVisibility(i, chButton, 1);
				this.sender.setControlElementVisibility(i, chButtonLabel, 1);
				this.sender.setControlElementVisibility(i, chFader, 1);
				this.sender.setControlElementVisibility(i, chFaderLabel, 1);
				this.sender.setControlElementVisibility(i, chOnButton, 1);
				this.sender.setControlElementVisibility(i, chLabel, 1);
				this.sender.setControlElementVisibility(i, auxFader, 1);
			}

			if (channels > LAYOUT_64CHANNELS) {
				// if mixer has more than 64 channels it possibly
				// uses the 96-layout
				for (int i = 0; i <= LAYOUT_96CHANNELS; i++) {
					// stateful channel switch buttons
					final String chButton = "system/state/selectedInput/changeTo/"
							+ (i + 1) + "/";
					final String chButtonLabel = chButton + "label";
					// to-stereo-view elements
					final String chFader = "input/" + (i + 1) + "/level";
					final String chFaderLabel = "input/" + (i + 1) + "/levelLabel";
					final String chOnButton = "input/" + (i + 1) + "/channelOn";
					final String chLabel = "input/" + (i + 1) + "/label";
					// aux elements
					final String auxFader = "stateful/aux/level/fromChannel/"
							+ (i + 1);

					this.sender.setControlElementVisibility(i, chButton, 1);
					this.sender.setControlElementVisibility(i, chButtonLabel, 1);
					this.sender.setControlElementVisibility(i, chFader, 1);
					this.sender.setControlElementVisibility(i, chFaderLabel, 1);
					this.sender.setControlElementVisibility(i, chOnButton, 1);
					this.sender.setControlElementVisibility(i, chLabel, 1);
					this.sender.setControlElementVisibility(i, auxFader, 1);
				}
				// hide unused things
				for (int i = LAYOUT_96CHANNELS; i >= channels; i--) {
					// stateful channel switch buttons
					final String chButton = "system/state/selectedInput/changeTo/"
							+ (i + 1) + "/";
					final String chButtonLabel = chButton + "label";
					// to-stereo-view elements
					final String chFader = "input/" + (i + 1) + "/level";
					final String chFaderLabel = "input/" + (i + 1) + "/levelLabel";
					final String chOnButton = "input/" + (i + 1) + "/channelOn";
					final String chLabel = "input/" + (i + 1) + "/label";
					// aux elements
					final String auxFader = "stateful/aux/level/fromChannel/"
							+ (i + 1);

					this.sender.setControlElementVisibility(i, chButton, 0);
					this.sender.setControlElementVisibility(i, chButtonLabel, 0);
					this.sender.setControlElementVisibility(i, chFader, 0);
					this.sender.setControlElementVisibility(i, chFaderLabel, 0);
					this.sender.setControlElementVisibility(i, chOnButton, 0);
					this.sender.setControlElementVisibility(i, chLabel, 0);
					this.sender.setControlElementVisibility(i, auxFader, 0);
				}

			} else {
				// hide unused things for mixers with <=64ch
				for (int i = LAYOUT_64CHANNELS; i >= channels; i--) {
					// statefull channel switch buttons
					final String chButton = "system/state/selectedInput/changeTo/"
							+ (i + 1) + "/";
					final String chButtonLabel = chButton + "label";
					// to-stereo-view elements
					final String chFader = "input/" + (i + 1) + "/level";
					final String chFaderLabel = "input/" + (i + 1) + "/levelLabel";
					final String chOnButton = "input/" + (i + 1) + "/channelOn";
					final String chLabel = "input/" + (i + 1) + "/label";
					// aux elements
					final String auxFader = "stateful/aux/level/fromChannel/"
							+ (i + 1);

					this.sender.setControlElementVisibility(i, chButton, 0);
					this.sender.setControlElementVisibility(i, chButtonLabel, 0);
					this.sender.setControlElementVisibility(i, chFader, 0);
					this.sender.setControlElementVisibility(i, chFaderLabel, 0);
					this.sender.setControlElementVisibility(i, chOnButton, 0);
					this.sender.setControlElementVisibility(i, chLabel, 0);
					this.sender.setControlElementVisibility(i, auxFader, 0);
				}
			}
		}
	}

	public synchronized void sendChangeColor(final String address, final String color) {
		final IOscMessage newColor = new OscMessage(OscAddressUtil.create(address));
		newColor.add(OscObjectUtil.createOscObject(color));
		this.sendMessage(newColor);
	}

	@Override
	public TouchOscInputChannelMsgHandler getInputHandler() {
		return (TouchOscInputChannelMsgHandler) super.getInputHandler();
	}

	@Override
	public TouchOscOutputAuxMsgHandler getOutputAuxHandler() {
		return (TouchOscOutputAuxMsgHandler) super.getOutputAuxHandler();
	}

	@Override
	public final void valueChangedEvent(final Object sender, final String fieldName, final Object oldValue, final Object newValue) {
		super.valueChangedEvent(sender, fieldName, oldValue, newValue);
	}

	@Override
	protected synchronized void setSnapFaders(final boolean selected) {
		inputHandler.setSnapActive(selected);
		auxHandler.setSnapActive(selected);
	}

	@Override
	public synchronized void sendMessage(final IOscMessage m) {
		super.sendMessage(m);
	}

	@Override
	public String toString() {
		return "Surface at ip " + this.getConnection();
	}

	public IPropertyProvider getPropertyProvider() {
		return this;
	}

	@Override
	public PropertyCollection getPropertyCollection() throws PropertiesException {
		return PropertyHandler.extractProperties(this.getBean());
	}

	@Override
	public void oscEvent(final IOscMessage adapt) {
		super.oscEvent(adapt);
	}
}
