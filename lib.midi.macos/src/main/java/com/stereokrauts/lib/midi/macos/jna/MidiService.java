package com.stereokrauts.lib.midi.macos.jna;

import com.stereokrauts.lib.midi.macos.jna.util.CFStringRef;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface MidiService extends Library {
	MidiService INSTANCE = (MidiService) Native.loadLibrary("CoreMIDI", MidiService.class);

	/* Object lookup functions */
	int MIDIGetNumberOfDevices();

	MIDIDeviceRef MIDIGetDevice(int deviceIndex0);

	int MIDIDeviceGetNumberOfEntities(MIDIDeviceRef deviceRef);

	MIDIEntityRef MIDIDeviceGetEntity(MIDIDeviceRef deviceRef, int entityIndex0);

	/* Endpoints */
	MIDIEndpointRef MIDIEntityGetSource(MIDIEntityRef entity, int sourceIndex0);

	MIDIEndpointRef MIDIEntityGetDestination(MIDIEntityRef entity, int destinationIndex0);

	int MIDIEntityGetNumberOfDestinations(MIDIEntityRef entity);

	int MIDIEntityGetNumberOfSources(MIDIEntityRef entity);

	/* Detail functions */
	OSStatus MIDIObjectGetStringProperty(MIDIObjectRef midiObjectReference, CFStringRef kMIDIPropertyDisplayName, Pointer pointer);

	OSStatus MIDIObjectGetIntegerProperty(MIDIObjectRef midiObjectReference, CFStringRef kMIDIPropertyDisplayName, Pointer pointer);

	/* Client functions */
	OSStatus MIDIClientCreate(CFStringRef name, MIDINotifyProc notifyProc, Void notifyRefCon, Pointer outClientRef);

	OSStatus MIDIInputPortCreate(final MIDIClientRef client, final CFStringRef portName, final MIDIReadProc readProc, final int refCon,
			Memory outPort);

	OSStatus MIDIOutputPortCreate(final MIDIClientRef client, final CFStringRef portName, Memory portRefMemory);

	/* Send/Receive */
	OSStatus MIDISend(MIDIPortRef port, MIDIEndpointRef dest, MIDIPacketList pktlist);

	OSStatus MIDISendSysex(MIDISysexSendRequest request);

	OSStatus MIDIDestinationCreate(final MIDIClientRef client, final CFStringRef name, final MIDIReadProc readProc, Pointer refCon,
			Memory /* MIDIEndpointRef * */outDest);

	OSStatus MIDIPortConnectSource(MIDIPortRef port, MIDIEndpointRef source, Pointer connRefCon);

	/* Callbacks */
	interface MIDINotifyProc extends Callback {
		void callback(MIDINotification message, Void refCon);
	}

	interface MIDIReadProc extends Callback {
		void callback(MIDIPacketList pktlist, Void readProcRefCon, Void srcConnRefCon);
	}

	MIDIEndpointRef MIDIGetSource(int i);

	OSStatus MIDIEndpointDispose(MIDIEndpointRef endpointRef);

	void MIDIPortDispose(MIDIPortRef midiPortRef);

	OSStatus MIDIReceived(MIDIEndpointRef sourceEndpointRef, MIDIPacketList pktlist);

	OSStatus MIDIClientDispose(MIDIClientRef clientRef);
}
