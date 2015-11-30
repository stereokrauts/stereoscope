package com.stereokrauts.lib.midi.macos.jna;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stereokrauts.lib.midi.macos.jna.ECoreMidiErrorCodes;
import com.stereokrauts.lib.midi.macos.jna.MIDINotification;
import com.stereokrauts.lib.midi.macos.jna.MIDIPacket;
import com.stereokrauts.lib.midi.macos.jna.MIDISysexSendRequest;
import com.stereokrauts.lib.midi.macos.jna.MIDISysexSendRequest.MIDICompletionProc;
import com.stereokrauts.lib.midi.macos.jna.MIDIPacketList;
import com.stereokrauts.lib.midi.macos.jna.MIDIClientRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIDeviceRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEndpointRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEntityRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIObjectRef;
import com.stereokrauts.lib.midi.macos.jna.MidiService;
import com.stereokrauts.lib.midi.macos.jna.MidiService.MIDINotifyProc;
import com.stereokrauts.lib.midi.macos.jna.MidiService.MIDIReadProc;
import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.stereokrauts.lib.midi.macos.jna.util.CFStringRef;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

public final class MidiServiceTest {

	@BeforeClass
	public static void assertMacPlatform() {
		Assume.assumeTrue(System.getProperty("os.name").toLowerCase().contains("mac"));
	}

	@Test
	public void testDevicesExists() {
		final MidiService service = MidiService.INSTANCE;
		final int result = service.MIDIGetNumberOfDevices();
		assertNotNull(result);
	}

	@Test
	public void testDeviceZeroExists() {
		final MidiService service = MidiService.INSTANCE;
		final MIDIDeviceRef result = service.MIDIGetDevice(0);
		assertNotNull(result);
	}

	@Test
	public void testDeviceZeroHasEntities() {
		final MidiService service = MidiService.INSTANCE;
		final MIDIDeviceRef deviceRef = service.MIDIGetDevice(0);
		final int entityCount = service.MIDIDeviceGetNumberOfEntities(deviceRef);
		assertNotNull(entityCount);
	}

	@Test
	public void testDeviceZeroHasName() {
		final MidiService service = MidiService.INSTANCE;

		for (int i = 0; i < service.MIDIGetNumberOfDevices(); i++) {
			final MIDIDeviceRef deviceRef = service.MIDIGetDevice(i);
			printName("Device: ", service, deviceRef);
			for (int j = 0; j < service.MIDIDeviceGetNumberOfEntities(deviceRef); j++) {
				final MIDIEntityRef entityRef = service.MIDIDeviceGetEntity(deviceRef, j);
				printName("\tEntity:", service, entityRef);
			}
		}
	}

	@Test
	public void testClientCreate() {
		final MidiService service = MidiService.INSTANCE;
		final CFStringRef name = CFStringRef.CFSTR("Stereokrauts lib.midi.macos Unit Test");

		final Void notifyRefCon = null;
		final Memory memory = new Memory(4);
		service.MIDIClientCreate(name, null, notifyRefCon, memory);
		final MIDIClientRef clientRef = new MIDIClientRef();
		clientRef.setValue(memory.getInt(0));
		assertNotNull(clientRef.intValue());
	}

	private int receivedPacketCounter = 0;

	@Test
	public void testClientMessageSend() throws Exception {
		final MidiService service = MidiService.INSTANCE;
		final CFStringRef name = CFStringRef.CFSTR("Stereokrauts lib.midi.macos Unit Test");
		final MIDINotifyProc notifyProc = new MIDINotifyProc() {
			@Override
			public void callback(final MIDINotification message, final Void refCon) {
				System.out.println("Callback called!");
			}
		};
		final Void notifyRefCon = null;
		final Memory clientRefMemory = new Memory(4);
		service.MIDIClientCreate(name, notifyProc, notifyRefCon, clientRefMemory);
		final MIDIClientRef clientRef = new MIDIClientRef();
		clientRef.setValue(clientRefMemory.getInt(0));

		final CFStringRef portName = CFStringRef.CFSTR("Test Port");
		final Memory mPortRef = new Memory(500);
		final MIDIReadProc midiInputReadProc = new MIDIReadProc() {
			@Override
			public void callback(final MIDIPacketList pktlist, final Void readProcRefCon, final Void srcConnRefCon) {
				System.out.println("Something received...!");
				System.out.println(String.format("Number of packets....: %d", pktlist.numPackets));
				for (int i = 0; i < pktlist.numPackets; i++) {
					receivedPacketCounter++;
					System.out.println("Dumping packet " + i);
					System.out.println("===============================");
					dumpPacket(pktlist.packet[i]);
					System.out.println("===============================");
				}
			}

			private void dumpPacket(final MIDIPacket midiPacket) {
				System.out.println(String.format("Packet length..: %d", midiPacket.length));
				System.out.println(String.format("Packet contents: %s", new String(Hex.encodeHex(Arrays.copyOf(midiPacket.data, midiPacket.length)))));
			}
		};
		OSStatus status = service.MIDIInputPortCreate(clientRef, portName, midiInputReadProc, 0, mPortRef);
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			fail("Returned " + status + ": " + status.toMidiErrorCode().name());
		}

		final Memory mEndpointRef = new Memory(500);
		status = service.MIDIDestinationCreate(clientRef, portName, midiInputReadProc, new Pointer(0), mEndpointRef);
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			fail("Returned " + status + ": " + status.toMidiErrorCode().name());
		}
		final MIDIEndpointRef endpointRef = new MIDIEndpointRef();
		endpointRef.setValue(mEndpointRef.getInt(0));

		final MIDISysexSendRequest request = createTestSysExMessage(endpointRef);

		status = service.MIDISendSysex(request);
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			fail("Returned " + status + ": " + status.toMidiErrorCode().name());
		}

		assertTrue(clientRef.intValue() != 0);
		assertTrue(endpointRef.intValue() != 0);

		int sendCounter = 100;
		for (int i = 0; i < sendCounter  - 1; i++) {
			status = service.MIDISendSysex(createTestSysExMessage(endpointRef));
			Thread.sleep(100);
		}
		
		Thread.sleep(5000);
		assertEquals(sendCounter, receivedPacketCounter);
		Thread.sleep(5000);
	}

	private MIDISysexSendRequest createTestSysExMessage(final MIDIEndpointRef endpointRef) {
		final MIDISysexSendRequest request = new MIDISysexSendRequest();
		request.destination = endpointRef;
		final byte[] sysexData = new byte[] { (byte) 0xf7, 0x0a, 0x10 };
		final Pointer sysexDataMemory = new Memory(sysexData.length);
		sysexDataMemory.write(0, sysexData, 0, sysexData.length);
		request.data = sysexDataMemory;
		request.bytesToSend = sysexData.length;
		request.complete = false;
		request.completionProc = new MIDICompletionProc() {
			@Override
			public void callback(final MIDISysexSendRequest request) {
				System.out.println("Request sent!");
			}
		};
		return request;
	}

	private void printName(final String prefix, final MidiService service, final MIDIObjectRef objectRef) {
		final NativeLibrary library = NativeLibrary.getInstance("CoreMIDI");
		final CFStringRef kMIDIPropertyDisplayName = new CFStringRef(library.getGlobalVariableAddress("kMIDIPropertyName").getPointer(0));

		final Memory m = new Memory(500);
		final OSStatus status = service.MIDIObjectGetStringProperty(objectRef, kMIDIPropertyDisplayName, m);
		if (status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kMIDIUnknownProperty)) {
			return;
		}

		final CFStringRef value = new CFStringRef(m.getPointer(0));

		System.out.println(prefix + "(" + objectRef.intValue() + ") " + value);
	}

}
