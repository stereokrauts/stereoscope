package com.stereokrauts.stereoscope.mixer.roland.m380;

import static com.stereokrauts.stereoscope.helper.tests.FieldsEqualAssertion.assertFieldsEqual;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.midi.api.MidiException;
import com.stereokrauts.lib.midi.util.DummyMidiSink;
import com.stereokrauts.stereoscope.helper.tests.DummyRememberingMixer;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380MidiReceiver;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;


public class RolandM380DataConversionTest {
	TestData currentData = new TestData();
	private DummyMidiSink sink;
	private M380Mixer mixer;

	@Before
	public void initData() throws MidiException
	{
		this.sink = new DummyMidiSink();
		this.mixer = new M380Mixer(null, this.sink);
	}

	@Test
	public void testStereoscopeToMixer()
	{
		for (int i = 0; i < this.currentData.testData.length; i++) {
			this.mixer.handleNotification(new MessageWithSender(null, this.currentData.testData[i].getMessage()));
			assertEquals("Testdata item " + (i+1) + " (" + this.currentData.testData[i].getDescription() + ")", this.currentData.testData[i].getSysex(), this.sink.getLastSentSysex());
			this.sink.clear();
		}
	}

	@Test
	public void testMixerToStereoscope()
	{
		final DummyRememberingMixer sink = new DummyRememberingMixer(this.mixer);
		final M380MidiReceiver recv = new M380MidiReceiver(this.mixer);

		for (int i = 0; i < this.currentData.testData.length; i++) {
			recv.handleSysex(ByteStringConversion.toBytes(this.currentData.testData[i].getSysex()));
			assertFieldsEqual("Testdata item " + (i+1) + " (" + this.currentData.testData[i].getDescription() + ")", this.currentData.testData[i].getMessage(), sink.getLastMessage());
			this.sink.clear();
		}
	}
}
