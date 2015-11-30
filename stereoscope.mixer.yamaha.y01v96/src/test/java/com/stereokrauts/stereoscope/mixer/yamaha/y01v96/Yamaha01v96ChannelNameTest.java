package com.stereokrauts.stereoscope.mixer.yamaha.y01v96;

import static com.stereokrauts.lib.binary.ByteStringUtil.normalize;
import static com.stereokrauts.stereoscope.helper.tests.FieldsEqualAssertion.assertFieldsEqual;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.midi.util.DummyMidiSink;
import com.stereokrauts.stereoscope.helper.tests.DummyRememberingMixer;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;


public class Yamaha01v96ChannelNameTest {
	private DummyMidiSink sink;
	private Y01v96Mixer mixer;

	@Before
	public void initData()
	{
		this.sink = new DummyMidiSink();
		this.mixer = new Y01v96Mixer(null, this.sink);
	}

	@Test
	public void channelNameRequest()
	{
		final DummyRememberingMixer sink = new DummyRememberingMixer(this.mixer);
		final Y01v96MidiReceiver recv = new Y01v96MidiReceiver(this.mixer);
		AbstractMessage<?> lastMsg;
		MsgChannelNameChanged expected;

		recv.handleSysex(ByteStringConversion.toBytes(normalize("F0	43	10	3E	0B	02	04	00	00	00	00	00	41	F7")));
		lastMsg = sink.getLastMessage();
		expected = new MsgChannelNameChanged(0, "AH01");
		assertFieldsEqual("Channel names should match", expected, lastMsg);

		recv.handleSysex(ByteStringConversion.toBytes(normalize("F0	43	10	3E	0B	02	04	01	00	00	00	00	42	F7")));
		lastMsg = sink.getLastMessage();
		expected = new MsgChannelNameChanged(0, "AB01");
		assertFieldsEqual("Channel names should match", expected, lastMsg);

		recv.handleSysex(ByteStringConversion.toBytes(normalize("F0	43	10	3E	0B	02	04	02	00	00	00	00	43	F7")));
		lastMsg = sink.getLastMessage();
		expected = new MsgChannelNameChanged(0, "ABC1");
		assertFieldsEqual("Channel names should match", expected, lastMsg);

		recv.handleSysex(ByteStringConversion.toBytes(normalize("F0	43	10	3E	0B	02	04	03	00	00	00	00	44	F7")));
		lastMsg = sink.getLastMessage();
		expected = new MsgChannelNameChanged(0, "ABCD");
		assertFieldsEqual("Channel names should match", expected, lastMsg);

		/* Test Stereo returns */
		recv.handleSysex(ByteStringConversion.toBytes(normalize("F0	43	10	3E	06	02	17	00	00	00	00	00	41	F7")));
		lastMsg = sink.getLastMessage();
		expected = new MsgChannelNameChanged(32, "AH33");
		assertFieldsEqual("Channel names should match", expected, lastMsg);
	}
}
