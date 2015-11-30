package tests.simulation;

import static org.junit.Assert.assertEquals;
import model.protocol.osc.DummySender;
import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddress;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.impl.OscMessage;
import model.surface.touchosc.TouchOscSurface;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import tests.mixer.MixerWithState;

import com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcher;
import com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcherFactory;


public class OscSurfaceChangeSimulationTest {
	/**
	 * the maximum expected message delay in milliseconds
	 */
	private static final int MAXIMUM_MESSAGE_DELAY_MS = 60;

	private static final int MAXIMUM_ERROR_PERCENTAGE = 1;

	private static int errorsOccured = 0;

	private static final float FLOAT_MAX_DELTA = 0.01f;
	private static final int HOWMANY_CHANNELS = 72;
	private static final int HOWMANY_AUXS = 24;
	private static final int HOWMANY_BUSSES = 24;
	private static final int HOWMANY_OUTPUTS = 24;
	private static final int HOWMANY_GEQS = 8;
	private static final String FILENAME = "simulation.txt";

	static TouchOscSurface simulatedMessageSink;
	static CoreMessageDispatcher updateManager;
	static MixerWithState mixer;
	private static DummySender simulatedMessageSource;

	private static SimulationDataAggregator aggregator;

	@Ignore
	public static void init() throws Exception {
		aggregator = new SimulationDataAggregator();

		simulatedMessageSource = new DummySender();

		simulatedMessageSink = new TouchOscSurface(false);
		mixer = new MixerWithState(HOWMANY_CHANNELS, HOWMANY_AUXS, HOWMANY_BUSSES, HOWMANY_OUTPUTS, HOWMANY_GEQS);

		updateManager = CoreMessageDispatcherFactory.getInstance("standard-dispatcher");

		updateManager.registerObserver(mixer);
		mixer.registerObserver(updateManager);

		updateManager.registerObserver(simulatedMessageSink.getOscMessageSender());
		simulatedMessageSink.registerObserver(updateManager);

		simulatedMessageSink.connect();

		final SimulationDataReader data = new SimulationDataReader(FILENAME);
		SimulationMessage msg;

		System.out.println("Processing " + FILENAME + "...");
		int messageCounter = 0;

		while ((msg = data.getNextMessage()) != null) {		
			final String oscAddress = msg.convertToOscAddress();

			if (messageCounter % 1000 == 0) {
				System.out.println("Simulation will send message " + msg.convertToOscAddress());
			}

			if (msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_AUXMASTER ||
					msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL) {
				final IOscMessage m = new OscMessage(new OscAddress("/stereoscope/system/state/selectedAux/changeTo/" + (msg.getAuxNumber() + 1)));
				simulatedMessageSource.sendMessage(m);
			} else if (msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_GEQBAND ||
					msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_GEQBANDRESET ||
					msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_GEQFULLRESET) {
				final IOscMessage m = new OscMessage(new OscAddress("/stereoscope/system/state/selectedGEQ/changeTo/" + (msg.getGeqNumber() + 1)));
				simulatedMessageSource.sendMessage(m);
			}

			if (msg.getMessageType() == SimulationMessage.MESSAGE_TYPE_GEQFULLRESET) {
				/* message has to be sent twice in (doubletouch neccesary) */
				final String addr = "/stereoscope/stateful/dsp/geq/resetGeq";

				IOscMessage m = new OscMessage(new OscAddress(addr));
				m.add(OscObjectUtil.createOscObject(1.0f));
				simulatedMessageSource.sendMessage(m);

				m = new OscMessage(new OscAddress(addr));
				m.add(OscObjectUtil.createOscObject(0.0f));
				simulatedMessageSource.sendMessage(m);

				m = new OscMessage(new OscAddress(addr));
				m.add(OscObjectUtil.createOscObject(1.0f));
				simulatedMessageSource.sendMessage(m);

				m = new OscMessage(new OscAddress(addr));
				m.add(OscObjectUtil.createOscObject(0.0f));
				simulatedMessageSource.sendMessage(m);
			} else {
				final IOscMessage m = new OscMessage(new OscAddress(oscAddress));
				m.add(OscObjectUtil.createOscObject(msg.getLevel()));
				simulatedMessageSource.sendMessage(m);
			}
			aggregator.addMessage(msg);

			if (messageCounter % 1000 == 0) {
				System.out.println("Message in line: " + (messageCounter + 1) + " - type: " + msg.getMessageType());
			}
			messageCounter++;

			//mixer.waitForMessage(msg.getMessageType());
			Thread.sleep(MAXIMUM_MESSAGE_DELAY_MS);
			switch(msg.getMessageType()) {
			case SimulationMessage.MESSAGE_TYPE_CHLEVEL:
				if (Math.abs(msg.getLevel() - mixer.getMixerState().getChannelLevel(msg.getChannelNumber())) > FLOAT_MAX_DELTA) {
					foundError();
				}
				break;
			}
		}

		Assert.assertTrue("Error rate within percentage", ((errorsOccured/messageCounter)*100) < MAXIMUM_ERROR_PERCENTAGE);
	}

	private static void foundError()
	{
		errorsOccured++;
	}

	@Test
	public final void testChannelLevel() {




		//		assertEquals("ChannelOn message", msg.isChannelOn(), mixer.getMixerState().getChannelOnButtons(msg.getChannelNumber()));
		//
		//		assertEquals("GeqBand message", msg.getLevel(), mixer.getMixerState().getGeqBandLevels(msg.getGeqNumber(), msg.isRightGeq(),
		//				msg.getBandNumber()), FLOAT_MAX_DELTA);
		//
		//		assertEquals("GeqBandReset message", 0.0f, mixer.getMixerState().getGeqBandLevels(msg.getGeqNumber(), msg.isRightGeq(),
		//				msg.getBandNumber()), FLOAT_MAX_DELTA);
		//
		//		for (int i = 0; i < mixer.getGeqCount(); i++) {
		//			for (int j = 0; j < MixerState.GEQ_NUMBER_OF_BANDS; j++) {
		//				assertEquals("GeqFullReset message (left)", 0.0f, mixer.getMixerState().getGeqBandLevels(i, false, j), FLOAT_MAX_DELTA);
		//				assertEquals("GeqFullReset message (right)", 0.0f, mixer.getMixerState().getGeqBandLevels(i, true, j), FLOAT_MAX_DELTA);
		//			}
		//		}
	}

	@Ignore
	public final void testChannelAuxLevel() {
		SimulationMessage msg;
		msg = aggregator.getLastMessageOfType(SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL);
		assertEquals("ChannelAuxLevel message", msg.getLevel(), mixer.getMixerState().getChannelAuxSend(msg.getAuxNumber(), msg.getChannelNumber()), FLOAT_MAX_DELTA);
	}

	@Ignore
	public final void testMasterLevel() {
		SimulationMessage msg;
		msg = aggregator.getLastMessageOfType(SimulationMessage.MESSAGE_TYPE_MASTER);
		assertEquals("MasterLevel message", msg.getLevel(), mixer.getMixerState().getMasterLevel(), FLOAT_MAX_DELTA);
	}

	@Ignore
	public final void testAuxMasterLevel() {
		SimulationMessage msg;
		msg = aggregator.getLastMessageOfType(SimulationMessage.MESSAGE_TYPE_AUXMASTER);
		assertEquals("AuxMasterLevel message", msg.getLevel(), mixer.getMixerState().getAuxMasters(msg.getAuxNumber()), FLOAT_MAX_DELTA);
	}

	public void oscEvent(final IOscMessage theIOscMessage) {
		// What is being sent to the surface? normaly, just ignore it...
		//System.out.println("The simulation message source has received a message: " + theIOscMessage);
	}
}
