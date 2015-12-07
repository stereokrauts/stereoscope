package model.controllogic;

import model.beans.BusAttendeeBean;
import model.bus.Bus;
import model.bus.BusAttendee;
import model.bus.BusAttendeeType;
import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;
import model.properties.IPropertyProvider;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgResyncEverything;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgResyncStateful;

/**
 * This class implements the resync behaviour of stereoscope.
 * 
 * @author theide
 */
public final class Resync implements IMessageReceiver, BusAttendee {
	private static final SLogger LOG = StereoscopeLogManager.getLogger("control-logic-resync");
	private IAmMixer mixer;
	private Bus bus;

	@Override
	public void handleNotification(final IMessageWithSender msg) {
		if (msg.getMessage() instanceof MsgResyncEverything) {
			LOG.info("complete resync requested by " + msg.getSender());

			this.resyncEverything();
		} else if (msg.getMessage() instanceof MsgResyncStateful) {
			LOG.info("stateful resync requested by " + msg.getSender());
			final MsgResyncStateful state = (MsgResyncStateful) msg.getMessage();

			this.resyncStateful(state);
		}
	}

	/**
	 * @param state
	 */
	private void resyncStateful(final MsgResyncStateful state) {
		this.getDefaultInformations();
		this.getAuxInformation(state.getSelectedAux());
		this.getChannelStripInformation(state.getAttachment());
		this.getGeqInformation(state.getSelectedGeq());
	}

	/**
	 * 
	 */
	private void resyncEverything() {
		this.getDefaultInformations();
		this.getAllAuxInformations();
		this.getAllBusInformations();
		this.getAllChannelStripInformations();
		this.getAllGeqInformations();
	}

	/**
	 * Requests update of all informations concerning a certain
	 * graphical equalizer.
	 * @param geqNumber
	 */
	private void getGeqInformation(final byte geqNumber) {
		if (this.mixer instanceof IMixerWithGraphicalEq) {
			final IMixerWithGraphicalEq mixer2 = (IMixerWithGraphicalEq) this.mixer;
			mixer2.getAllGeqLevels(geqNumber);
		}
	}

	/**
	 * Requests update of all informations concerning a certain
	 * channel strip.
	 * @param channel
	 */
	private void getChannelStripInformation(final int channel) {
		this.mixer.getAllInputValues(channel);
	}

	/**
	 * Requests update of all informations concerning a certain
	 * aux send.
	 * @param aux The aux send to request informations from
	 */
	private void getAuxInformation(final int aux) {
		this.mixer.getAllAuxLevels(aux);
		this.mixer.getAuxMaster(aux);
	}

	/**
	 * Requests update on all GEQ informations.
	 */
	private void getAllGeqInformations() {
		if (this.mixer instanceof IMixerWithGraphicalEq) {
			for (byte i = 0; i < this.mixer.getGeqCount(); i++) {
				this.getGeqInformation(i);
			}
		}
	}

	/**
	 * Requests update of all channel strip informations.
	 */
	private void getAllChannelStripInformations() {
		for (int i = 0; i < this.mixer.getChannelCount(); i++) {
			this.getChannelStripInformation(i);
		}
	}

	/**
	 * Requests update of all aux parameters on all aux sends.
	 */
	private void getAllAuxInformations() {
		for (int i = 0; i < this.mixer.getAuxCount(); i++) {
			this.getAuxInformation(i);
		}
	}


	private void getAllBusInformations() {
		for (int i = 0; i < this.mixer.getBusCount(); i++) {
			this.getBusInformation(i);
		}
	}

	private void getBusInformation(final int i) {
		mixer.getBusMaster(i);
	}

	/**
	 * Requests update on the default informations of a mixer.
	 */
	private void getDefaultInformations() {
		this.mixer.getAllChannelOnButtons();
		this.mixer.getAllChannelLevels();
		this.mixer.getAllDelayTimes();
		this.mixer.getAllGroupsStatus();

		this.getAllChannelNames();
	}

	/**
	 * Requests update on all channel names of the mixer.
	 */
	private void getAllChannelNames() {
		if (this.mixer instanceof IProvideChannelNames) {
			final IProvideChannelNames mixer2 = (IProvideChannelNames) this.mixer;
			mixer2.getChannelNames();
		}
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return new IMessageReceiver() {
			@Override
			public void handleNotification(final IMessageWithSender msg) {
				/* All messages are ignored by this attendee */
			}
		};
	}

	@Override
	public String getName() {
		return "Resync control logic";
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.CONTROL_LOGIC;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return null;
	}

	@Override
	public BusAttendeeBean getBean() {
		return null;
	}

	@Override
	public Bus getBus() {
		return this.bus;
	}

	@Override
	public void setBus(final Bus bus) {
		this.bus = bus;
		this.mixer = bus.getTheMixer();
	}

	@Override
	public void startup() {
	}

	@Override
	public void shutdown() {
	}

	@Override
	public String getIdentifier() {
		return "Resync";
	}

}
