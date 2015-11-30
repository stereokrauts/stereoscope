package model.protocol.osc;

import static org.junit.Assert.assertEquals;
import model.protocol.osc.handler.OscDelayMsgHandler;
import model.protocol.osc.impl.OscMessage;
import model.surface.stubs.OscSurfaceStub;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;

public class OscDelayMsgHandlerTest {
	private OscSurfaceStub mySurface;
	private OscDelayMsgHandler hnd;

	@Before
	public void init()
	{
		this.mySurface = new OscSurfaceStub();
		this.hnd = new OscDelayMsgHandler();
		this.hnd.setSurface(this.mySurface);
	}

	@Test
	public void testAuxMasterDelay() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/aux/4/delay"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgAuxMasterDelayChanged(3, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testBusMasterDelay() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/bus/4/delay"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgBusMasterDelayChanged(3, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testOmniDelay() {
		final IOscMessage messageUnderTest = new OscMessage(OscAddressUtil.create("/stereoscope/output/omni/4/delay"));
		messageUnderTest.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(messageUnderTest);

		assertEquals(new MsgOutputDelayChanged(3, 1.0f), this.mySurface.getLastMessage());
	}
}
