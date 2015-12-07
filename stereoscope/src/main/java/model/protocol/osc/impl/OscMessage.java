package model.protocol.osc.impl;

import java.util.ArrayList;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddress;
import model.protocol.osc.OscObject;

/**
 * This class represents a OSC message in stereoscope.
 * @author th
 *
 */
public final class OscMessage implements IOscMessage {
	private final ArrayList<OscObject> objects = new ArrayList<OscObject>();
	private final OscAddress address;

	public OscMessage(final OscAddress address) {
		this.address = address;
	}

	public int size() {
		return objects.size();
	}

	@Override
	public OscObject get(final int i) {
		return objects.get(i);
	}

	@Override
	public void add(final OscObject o) {
		objects.add(o);
	}

	@Override
	public OscAddress address() {
		return address;
	}

	@Override
	public void addAll(final IOscMessage otherMessage) {
		if (otherMessage instanceof OscMessage) {
			final OscMessage oscMessage = (OscMessage) otherMessage;
			objects.addAll(oscMessage.objects);
		} else {
			throw new IllegalArgumentException("Can only work with OscMessage objects.");
		}
	}

	@Override
	public String toString() {
		return String.format("[OscMessage: address %s, objects %s]", this.address, this.objects);
	}

}
