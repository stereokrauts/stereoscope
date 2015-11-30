package com.stereokrauts.lib.midi.macos.jna;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stereokrauts.lib.midi.macos.jna.ECoreMidiErrorCodes;
import com.stereokrauts.lib.midi.macos.jna.MIDIPacket;
import com.stereokrauts.lib.midi.macos.jna.MIDIPacketList;
import com.stereokrauts.lib.midi.macos.jna.MIDIClientRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIDeviceRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEndpointRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEntityRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIPortRef;
import com.stereokrauts.lib.midi.macos.jna.MidiService;
import com.stereokrauts.lib.midi.macos.jna.MidiService.MIDIReadProc;
import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.stereokrauts.lib.midi.macos.jna.util.CFStringRef;
import com.sun.jna.Memory;

public final class MidiServiceReceiveTest {

	@BeforeClass
	public static void assertMacPlatform() {
		Assume.assumeTrue(System.getProperty("os.name").toLowerCase().contains("mac"));
	}

	@Test
	public void testClientMessageSend() throws Exception {
		final MidiService service = MidiService.INSTANCE;

		final MIDIDeviceRef midiGetDevice = service.MIDIGetDevice(0);
		final MIDIEntityRef midiDeviceGetEntity = service.MIDIDeviceGetEntity(midiGetDevice, 0);
		final MIDIEndpointRef source = service.MIDIEntityGetSource(midiDeviceGetEntity, 0);

		final Memory clientRefMemory = new Memory(4);
		OSStatus status = service.MIDIClientCreate(CFStringRef.CFSTR("Stereokrauts lib.midi.macos Unit Test"), null, null, clientRefMemory);
		checkStatus(status);
		final MIDIClientRef clientRef = new MIDIClientRef();
		clientRef.setValue(clientRefMemory.getInt(0));

		System.out.println("222222222222222");

		final MIDIReadProc midiInputReadProc = new MIDIReadProc() {
			@Override
			public void callback(final MIDIPacketList pktlist, final Void readProcRefCon, final Void srcConnRefCon) {
				System.out.println("Something received...!");
				System.out.println(String.format("Number of packets....: %d", pktlist.numPackets));
				for (int i = 0; i < pktlist.numPackets; i++) {
					System.out.println("Dumping packet " + i);
					System.out.println("===============================");
					dumpPacket(pktlist.packet[i]);
					System.out.println("===============================");
				}
			}

			private void dumpPacket(final MIDIPacket midiPacket) {
				System.out.println(String.format("Time stamp.....: %d", midiPacket.timeStamp));
				System.out.println(String.format("Packet length..: %d", midiPacket.length));
				System.out.println(String.format("Packet contents: %s", new String(Hex.encodeHex(midiPacket.data))));
			}
		};

		final Memory endPointRefMemory = new Memory(4);
		status = service.MIDIDestinationCreate(clientRef, CFStringRef.CFSTR("Virtual MIDI Destination"), midiInputReadProc, null,
				endPointRefMemory);
		checkStatus(status);

		final Memory mPortRef = new Memory(4);
		status = service.MIDIInputPortCreate(clientRef, CFStringRef.CFSTR("Input"), midiInputReadProc, 0, mPortRef);
		checkStatus(status);

		final MIDIPortRef MIDIPortRef = new MIDIPortRef();
		MIDIPortRef.setValue(mPortRef.getInt(0));

		status = service.MIDIPortConnectSource(MIDIPortRef, source, null);
		checkStatus(status);

		Thread.sleep(1000);
	}

	private void checkStatus(final OSStatus status) {
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			//fail("Returned " + status + ": " + status.toMidiErrorCode().name());
		}
	}

}
