package model.surface;

import static org.junit.Assert.assertEquals;

import java.util.List;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscConstants;
import model.protocol.osc.OscObjectUtil;
import model.surface.stubs.OscSurfaceStub;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAutoOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterFreq;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterQ;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaLeftSideChain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaPair;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgAuxMasterLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgBusMasterLevelLabel;

/**
 * Test class for IOscMessageSender.
 * @author theide
 *
 */
public class OscMessageSenderTest {
	private static final float FLOAT_NUMBERS_TOLERANCE = 0.01f;
	private static OscMessageSender sender;
	private static OscSurfaceStub stub;

	@BeforeClass
	public static void init() {
		stub = new OscSurfaceStub();
		sender = new OscMessageSender(stub);
	}
	
	@Before
	public void clear() {
		stub.clearMessages();
	}

	@Test
	public final void testMsgChannelLevelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgChannelLevelChanged(0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/input/1/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgChannelLevelLabel() {
		sender.handleNotification(new MessageWithSender(sender, new MsgChannelLevelLabel(0, 1, "Test")));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/input/1/levelLabel", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", "Test", OscObjectUtil.toString(sent.get(0)));
	}

	@Test
	public final void testMsgChannelNameChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgChannelNameChanged(0, "Test")));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/input/1/label", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", "Test", OscObjectUtil.toString(sent.get(0)));
	}

	@Test
	public final void testMsgChannelOnChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgChannelOnChanged(0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/input/1/channelOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgAuxMasterDelayChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgAuxMasterDelayChanged(0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/output/aux/1/delay", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgAuxMasterLevelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgAuxMasterLevelChanged(0, 1.0f)));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();
		IOscMessage sent = msg.get(0);
		assertEquals("Address must be correct", "/stereoscope/output/aux/1/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
		sent = msg.get(1);
		assertEquals("Address must be correct", "/stereoscope/stateful/aux/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgAuxMasterLevelLabelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgAuxMasterLevelLabel(0, "test")));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();
		IOscMessage sent = msg.get(0);
		assertEquals("Address must be correct", "/stereoscope/output/aux/1/levelLabel", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", "test", OscObjectUtil.toString(sent.get(0)));
		sent = msg.get(1);
		assertEquals("Address must be correct", "/stereoscope/stateful/aux/levelLabel", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", "test", OscObjectUtil.toString(sent.get(0)));
	}

	@Test
	public final void testMsgAuxSendChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgAuxSendChanged(0, 0, 1.0f)));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();
		IOscMessage sent = msg.get(0);
		assertEquals("Address must be correct", "/stereoscope/input/1/toAux/1/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
		sent = msg.get(1);
		assertEquals("Address must be correct", "/stereoscope/stateful/aux/level/fromChannel/1", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgBusMasterDelayChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgBusMasterDelayChanged(0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/output/bus/1/delay", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgBusMasterLevelLabelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgBusMasterLevelLabel(0, "test")));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/output/bus/1/levelLabel", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", "test", OscObjectUtil.toString(sent.get(0)));
	}

	@Test
	public final void testMsgBusMasterLevelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgBusMasterLevelChanged(0, 1.0f)));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();
		final IOscMessage sent = msg.get(0);
		assertEquals("Address must be correct", "/stereoscope/output/bus/1/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testDcaLevelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgDcaLevelChanged(0, 1.0f)));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();
		final IOscMessage sent = msg.get(0);
		assertEquals("Address must be correct", "/stereoscope/global/dca/1/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgOutputDelayChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgOutputDelayChanged(0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/output/omni/1/delay", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgGeqBandLevelChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgGeqBandLevelChanged((short) 0, 0, false, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/band/1/left/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgGeqBandReset() {
		sender.handleNotification(new MessageWithSender(sender, new MsgGeqBandReset((short) 0, 0, false, null)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/band/1/left/level", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", OscConstants.CENTER_CONTROL_OFFSET, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgGeqFullReset() {
		sender.handleNotification(new MessageWithSender(sender, new MsgGeqFullReset((short) 0, null)));
		final List<IOscMessage> msg = stub.getAndClearSentMessages();

		for (int i = 0; i < OscSurface.GEQ_BANDS; i++) {
			final IOscMessage sent = msg.get(i);
			assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/band/" + (i + 1) + "/left/level", OscAddressUtil.stringify(sent.address()));
			assertEquals("Value must be correct", OscConstants.CENTER_CONTROL_OFFSET, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
		}
		for (int i = 0; i < OscSurface.GEQ_BANDS; i++) {
			final IOscMessage sent = msg.get(i + OscSurface.GEQ_BANDS);
			assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/band/" + (i + 1) + "/right/level", OscAddressUtil.stringify(sent.address()));
			assertEquals("Value must be correct", OscConstants.CENTER_CONTROL_OFFSET, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
		}
	}

	@Test
	public final void testMsgGeqTypeChangedToFlexEq() {
		sender.handleNotification(new MessageWithSender(sender, new MsgGeqTypeChanged((short) 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/isFlexEq15/visible", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 0.0f, OscObjectUtil.toDouble(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgGeqTypeChangedToNotFlexEq() {
		sender.handleNotification(new MessageWithSender(sender, new MsgGeqTypeChanged((short) 0, false)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/dsp/geq/isFlexEq15/visible", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toDouble(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaAttack() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaAttack(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/attack", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaAutoOn() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaAutoOn(0, 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/autoOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaDecayRelease() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaDecayRelease(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/decay", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}	

	@Test
	public final void testMsgInputDynaFilterFreq() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaFilterFreq(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/filterFreq", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaFilterOn() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaFilterOn(0, 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/filterOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputDynaFilterQ() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaFilterQ(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/filterQ", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaFilterType() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaFilterType(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/filterType", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaGain() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaGain(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/gain", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaHold() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaHold(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/hold", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaKeyIn() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaKeyIn(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/keyIn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputDynaKnee() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaKnee(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/knee", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaLeftSideChain() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaLeftSideChain(0, 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/leftSideChain", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputDynaOn() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaOn(0, 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/dynaOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputDynaPair() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaPair(0, 0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/pair", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputDynaRange() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaRange(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/range", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaRatio() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaRatio(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/ratio", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputDynaThreshold() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputDynaThreshold(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/dynamics/1/threshold", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}


	@Test
	public final void testMsgInputPeqF() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqF(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/band/1/f", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqG() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqG(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/band/1/g", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqQ() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqQ(0, 0, 1.0f)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/band/1/q", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqHPFChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqHPFChanged(0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/hpfOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqLPFChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqLPFChanged(0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/lpfOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqModeChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqModeChanged(0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/mode", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

	@Test
	public final void testMsgInputPeqOnChanged() {
		sender.handleNotification(new MessageWithSender(sender, new MsgInputPeqOnChanged(0, true)));
		final IOscMessage sent = stub.getAndClearSentMessages().get(0);
		assertEquals("Address must be correct", "/stereoscope/stateful/input/peq/peqOn", OscAddressUtil.stringify(sent.address()));
		assertEquals("Value must be correct", 1.0f, OscObjectUtil.toFloat(sent.get(0)), FLOAT_NUMBERS_TOLERANCE);
	}

}
