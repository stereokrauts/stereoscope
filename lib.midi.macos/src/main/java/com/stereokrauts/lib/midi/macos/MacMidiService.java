package com.stereokrauts.lib.midi.macos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.macos.jna.ECoreMidiErrorCodes;
import com.stereokrauts.lib.midi.macos.jna.MIDIDeviceRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEntityRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIObjectRef;
import com.stereokrauts.lib.midi.macos.jna.MidiService;
import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.stereokrauts.lib.midi.macos.jna.util.CFStringRef;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;

public final class MacMidiService {
	private final Set<MacMidiDevice> devices = new HashSet<>();
	private final HashMap<MacMidiDevice, Set<MacMidiEntity>> entities = new HashMap<>();

	private final MidiService service;
	private final NativeLibrary library;

	public MacMidiService() {
		service = MidiService.INSTANCE;
		library = NativeLibrary.getInstance("CoreMIDI");

		initDevices();
		initEntities();

		initMidiClient();
	}

	private void initMidiClient() {

	}

	private void initDevices() {
		for (int i = 0; i < service.MIDIGetNumberOfDevices(); i++) {
			final MIDIDeviceRef deviceRef = service.MIDIGetDevice(i);
			final MacMidiDevice macMidiDevice = new MacMidiDevice(this, deviceRef);
			devices.add(macMidiDevice);
		}
	}

	private void initEntities() {
		for (final MacMidiDevice device : devices) {
			final MIDIDeviceRef deviceRef = device.getReference();
			entities.put(device, new HashSet<MacMidiEntity>());
			for (int i = 0; i < service.MIDIDeviceGetNumberOfEntities(deviceRef); i++) {
				final MIDIEntityRef entityRef = service.MIDIDeviceGetEntity(deviceRef, i);
				final MacMidiEntity macMidiEntity = new MacMidiEntity(this, device, entityRef);
				entities.get(device).add(macMidiEntity);
			}
		}
	}

	public Set<MacMidiDevice> getDevices() {
		return devices;
	}

	public Set<MacMidiEntity> getEntities(final MacMidiDevice device) {
		return entities.get(device);
	}

	public String getName(final MIDIObjectRef objectRef) {
		final CFStringRef kMIDIPropertyDisplayName = new CFStringRef(library.getGlobalVariableAddress("kMIDIPropertyName").getPointer(0));

		final Memory m = new Memory(500);
		final OSStatus status = service.MIDIObjectGetStringProperty(objectRef, kMIDIPropertyDisplayName, m);
		if (status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kMIDIUnknownProperty)) {
			return "<unknown>";
		}

		final CFStringRef value = new CFStringRef(m.getPointer(0));

		return value.toString();
	}

	MidiService getService() {
		return service;
	}

	public boolean isOffline(final MIDIDeviceRef deviceRef) {
		final CFStringRef kMIDIPropertyOffline = new CFStringRef(library.getGlobalVariableAddress("kMIDIPropertyOffline").getPointer(0));

		final Memory m = new Memory(500);
		final OSStatus status = service.MIDIObjectGetIntegerProperty(deviceRef, kMIDIPropertyOffline, m);
		if (status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kMIDIUnknownProperty)) {
			StereoscopeLogManager.getLogger(getClass()).error("Unknown property on device " + deviceRef);
			return true;
		}

		return m.getInt(0) == 1;

	}
}
