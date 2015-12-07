package model.protocol.osc;

import static org.junit.Assert.assertEquals;
import model.protocol.osc.handler.OscOutputMsgHandler;
import model.protocol.osc.impl.OscMessage;
import model.surface.stubs.OscSurfaceStub;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;

public class OscOutputMsgHandlerTest {
	private OscSurfaceStub mySurface;
	private OscOutputMsgHandler hnd;

	@Before
	public void init()
	{
		this.mySurface = new OscSurfaceStub();
		this.hnd = new OscOutputMsgHandler();
		this.hnd.setSurface(this.mySurface);
	}


	@Test
	public void testMasterLevel() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/master/level"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgMasterLevelChanged(1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testAuxMasterLevel() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/aux/4/level"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgAuxMasterLevelChanged(3, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testBusMasterLevel() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/bus/4/level"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgBusMasterLevelChanged(3, 1.0f), this.mySurface.getLastMessage());
	}
}
