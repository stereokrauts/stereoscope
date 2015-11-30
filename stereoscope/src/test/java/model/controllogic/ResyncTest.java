package model.controllogic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.bus.Bus;
import model.bus.BusAttendeeMixer;
import model.bus.ClassicBus;
import model.mixer.interfaces.IAmMixer;

import org.junit.Ignore;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgResyncEverything;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;


public class ResyncTest {
	private static class MixerDummy extends IAmMixer {

		boolean auxMasterCalled;
		boolean outputMasterCalled;
		boolean busMasterCalled;
		boolean channelLevelCalled;
		boolean channelAuxCalled;
		boolean auxChannelOnButtonCalled;
		boolean channelOnButtonCalled;
		boolean allAuxLevelsCalled;
		boolean allAuxChannelOnCalled;
		boolean allBusLevelsCalled;
		boolean channelLevelsCalled;
		boolean inputValuesCalled;
		boolean allChannelOnButtonsCalled;
		boolean groupStatusCalled;
		boolean getAllDelayTimesCalled;


		@Override
		public void handleNotification(final IMessageWithSender msg) {}


		@Override
		public void registerObserver(final IMessageReceiver observer) {}


		@Override
		public void getAuxMaster(final int aux) {
			this.auxMasterCalled = true;
		}

		@Override
		public void getOutputMaster() {
			//rj: added this but don't know for what usecase.	
			this.outputMasterCalled = true;
		}
		
		@Override
		public void getChannelLevel(final int channel) {
			this.channelLevelCalled = true;
		}


		@Override
		public void getChannelAux(final int channel, final int aux) {
			this.channelAuxCalled = true;
		}

		@Override
		public void getAuxChannelOnButton(final int channel, final int aux) {
			this.auxChannelOnButtonCalled = true;
		}


		@Override
		public void getChannelOnButton(final int channel) {
			this.channelOnButtonCalled = true;
		}


		@Override
		public void getAllAuxLevels(final int aux) {
			this.allAuxLevelsCalled = true;
		}

		@Override
		public void getAllAuxChannelOn(final int aux) {
			this.allAuxChannelOnCalled = true;
		}


		@Override
		public void getAllChannelLevels() {
			this.channelLevelsCalled = true;
		}


		@Override
		public void getAllInputValues(final int inputChn) {
			this.inputValuesCalled = true;
		}


		@Override
		public void getAllChannelOnButtons() {
			this.allChannelOnButtonsCalled = true;
		}


		@Override
		public void getAllGroupsStatus() {
			this.groupStatusCalled = true;
		}


		@Override
		public void getAllDelayTimes() {
			this.getAllDelayTimesCalled = true;
		}


		@Override
		public int getChannelCount() {
			return 1;
		}


		@Override
		public int getAuxCount() {
			return 1;
		}


		@Override
		public int getBusCount() {
			return 1;
		}


		@Override
		public int getOutputCount() {
			return 1;
		}


		@Override
		public int getMatrixCount() {
			return 1;
		}


		@Override
		public int getGeqCount() {
			return 1;
		}


		@Override
		public void getBusMaster(final int bus) {
			busMasterCalled = true;
		}


		@Override
		public void getAllBusStatus() {
			allBusLevelsCalled = true;
		}


		@Override
		public String getPluginName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}

	@Ignore
	public void testResync() throws Exception
	{
		final MixerDummy dummy = new MixerDummy();
		final Bus bus = new ClassicBus();

		bus.addAttendee(new BusAttendeeMixer(new IMixerPlugin() {
			@Override
			public String getPluginName() {
				return "dummy-mixer";
			}

			@Override
			public IPersistentPluginConfiguration getPersistableConfiguration() {
				return null;
			}

			@Override
			public IAmMixer getMixer() {
				return dummy;
			}

			@Override
			public IMessageReceiver getMessageEndpoint() {
				return new IMessageReceiver() {
					@Override
					public void handleNotification(final IMessageWithSender msg) {
						dummy.handleNotification(msg);
					}
				};
			}

			@Override
			public void shutdown() {
			}

			@Override
			public void registerCommunicationAware(final ICommunicationAware attendee) {
			}

			@Override
			public String getDisplayName() {
				return "";
			}
		}), 0);

		final MsgResyncEverything msg = new MsgResyncEverything(null);

		final Resync hnd = new Resync();
		hnd.setBus(bus);

		bus.dispatchMessage(new MessageWithSender(dummy, msg));

		Thread.sleep(300);

		assertTrue("Aux Levels requested", dummy.allAuxLevelsCalled);
		assertTrue("All Channel on buttons requested", dummy.allChannelOnButtonsCalled);
		assertTrue("Aux masters requested", dummy.auxMasterCalled);
		assertTrue("Bus masters requested", dummy.busMasterCalled);
		assertTrue("All Bus masters requested", dummy.allBusLevelsCalled);
		assertTrue("All channel levels requested", dummy.channelLevelsCalled);
		assertTrue("Delay times requested", dummy.getAllDelayTimesCalled);
		assertTrue("Group status requested", dummy.groupStatusCalled);
		assertTrue("Input values requested", dummy.inputValuesCalled);

		assertFalse("Channel aux requested", dummy.channelAuxCalled);
		assertFalse("Specific Channel level requested", dummy.channelLevelCalled);
		assertFalse("Specific Channel on button requested", dummy.channelOnButtonCalled);
	}
}
