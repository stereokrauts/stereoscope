package model.controllogic;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import model.bus.Bus;
import model.bus.ClassicBus;

import org.junit.Ignore;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;


public class SceneRecallTest {
	private static class MsgBuffer implements IMessageReceiver, IMessageSender {
		ArrayList<IMessageWithSender> messages = new ArrayList<IMessageWithSender>();
		
		
		@Override
		public void handleNotification(final IMessageWithSender msg) {
			this.messages.add(msg);
		}
	}
	
	@Ignore
	public void testSceneRecall() throws Exception
	{
		final MsgBuffer testBuffer = new MsgBuffer();
		final MsgSceneChange msg = new MsgSceneChange(1, "sceneChange");
		final Bus bus = new ClassicBus();
		
		final SceneRecall hnd = new SceneRecall();
		hnd.setBus(bus);
		
		bus.dispatchMessage(new MessageWithSender(testBuffer, msg));
		
		Thread.sleep(300);
		assertTrue("SceneRecallHandler has sent messages", testBuffer.messages.size() > 0);
		
	}
}
