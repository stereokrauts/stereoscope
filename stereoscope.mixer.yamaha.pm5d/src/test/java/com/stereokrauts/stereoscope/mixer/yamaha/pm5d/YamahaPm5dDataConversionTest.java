package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import static com.stereokrauts.stereoscope.helper.tests.FieldsEqualAssertion.assertFieldsEqual;
import static org.junit.Assert.assertEquals;

import javax.sound.midi.MidiUnavailableException;

import org.junit.Before;
import org.junit.Test;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.midi.util.DummyMidiSink;
import com.stereokrauts.stereoscope.helper.tests.DummyRememberingMixer;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;


public class YamahaPm5dDataConversionTest {
	TestData currentData = new TestData();
	private DummyMidiSink sink;
	private Pm5dMixer mixer;

	@Before
	public void initData() throws MidiUnavailableException
	{
		this.sink = new DummyMidiSink();
		this.mixer = new Pm5dMixer(null, this.sink);
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
		final Pm5dMidiReceiver recv = new Pm5dMidiReceiver(this.mixer);

		for (int i = 0; i < this.currentData.testData.length; i++) {
			recv.handleSysex(ByteStringConversion.toBytes(this.currentData.testData[i].getSysex()));
			assertFieldsEqual("Testdata item " + (i+1) + " (" + this.currentData.testData[i].getDescription() + ")", this.currentData.testData[i].getMessage(), sink.getLastMessage());
			this.sink.clear();
		}
	}
}
