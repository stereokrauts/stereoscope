package model.protocol.osc;

import static org.junit.Assert.assertEquals;
import model.protocol.osc.handler.OscInputChannelStripMsgHandler;
import model.protocol.osc.impl.OscMessage;
import model.surface.stubs.OscSurfaceStub;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;

public class OscInputChannelStripMsgHandlerTest {
	private OscSurfaceStub mySurface;
	private OscInputChannelStripMsgHandler hnd;

	@Before
	public void init()
	{
		this.mySurface = new OscSurfaceStub();
		this.hnd = new OscInputChannelStripMsgHandler();
		this.hnd.setSurface(this.mySurface);
	}

	@Test
	public void testParametricEqFullGainMessage() {
		final IOscMessage fullGain = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/g"));
		fullGain.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(fullGain);

		assertEquals(new MsgInputPeqG(0, 0, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testParametricEqLowestGainMessage() {
		final IOscMessage fullGain = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/g"));
		fullGain.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(fullGain);

		assertEquals(new MsgInputPeqG(0, 0, -1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testParametricEqHighQMessage() {
		final IOscMessage fullQ = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/q"));
		fullQ.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(fullQ);

		assertEquals(new MsgInputPeqQ(0, 0, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testParametricEqLowestQMessage() {
		final IOscMessage fullQ = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/q"));
		fullQ.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(fullQ);

		assertEquals(new MsgInputPeqQ(0, 0, 0.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testParametricEqHighFreqMessage() {
		final IOscMessage fullFreq = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/f"));
		fullFreq.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(fullFreq);

		assertEquals(new MsgInputPeqF(0, 0, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testParametricEqLowestFreqMessage() {
		final IOscMessage fullFreq = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/peq/band/1/f"));
		fullFreq.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(fullFreq);

		assertEquals(new MsgInputPeqF(0, 0, 0.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testPanRightValueMessage() {
		final IOscMessage message = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/misc/pan"));
		message.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(message);

		assertEquals(new MsgInputPan(0, 1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testPanLeftValueMessage() {
		final IOscMessage message = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/misc/pan"));
		message.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(message);

		assertEquals(new MsgInputPan(0, -1.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testLevelZeroValueMessage() {
		final IOscMessage message = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/misc/level"));
		message.add(OscObjectUtil.createOscObject(0.0f));
		this.hnd.handleOscMessage(message);

		assertEquals(new MsgChannelLevelChanged(0, 0.0f), this.mySurface.getLastMessage());
	}

	@Test
	public void testLevelFullValueMessage() {
		final IOscMessage message = new OscMessage(OscAddressUtil.create("/stereoscope/stateful/input/misc/level"));
		message.add(OscObjectUtil.createOscObject(1.0f));
		this.hnd.handleOscMessage(message);

		assertEquals(new MsgChannelLevelChanged(0, 1.0f), this.mySurface.getLastMessage());
	}
}
