package model.protocol.osc;

import static org.junit.Assert.assertEquals;
import model.protocol.osc.handler.OscInputChannelMsgHandler;
import model.protocol.osc.impl.OscMessage;
import model.surface.stubs.OscSurfaceStub;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;

public class OscInputStatelessMsgHandlerTest {
	private OscSurfaceStub mySurface;
	private OscInputChannelMsgHandler hnd;

	@Before
	public void init()
	{
		this.mySurface = new OscSurfaceStub();
		this.hnd = new OscInputChannelMsgHandler();
		this.hnd.setSurface(this.mySurface);
	}


	@Test
	public void testInputLevel() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/level"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgChannelLevelChanged(14, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testInputChannelOn() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/channelOn"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgChannelOnChanged(14, true), this.mySurface.getLastMessage());
	}

	@Test
	public void testInputChannelOff() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/channelOn"));
		messageUnderTest.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgChannelOnChanged(14, false), this.mySurface.getLastMessage());
	}

	@Test
	public void testInputToAux() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/toAux/3/level"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgAuxSendChanged(14, 2, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testInputToAuxOn() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/toAux/3/channelOn"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgAuxSendOnChanged(14, 2, true), this.mySurface.getLastMessage());
	}

	@Test
	public void testInputToAuxOff() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/input/15/toAux/3/channelOn"));
		messageUnderTest.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgAuxSendOnChanged(14, 2, false), this.mySurface.getLastMessage());
	}
}
