package model.protocol.osc;

/**
 * This is a generic marker class for classes/objects that
 * can be sent across an OSC network.
 * @author th
 *
 */
public final class OscObject {
	private final Object innerObject;

	OscObject(final Object innerObject) {
		this.innerObject = innerObject;
	}

	Object getInner() {
		return innerObject;
	}

	@Override
	public String toString() {
		return String.format("[OscObject: innerObject=%s]", innerObject);
	}

}
