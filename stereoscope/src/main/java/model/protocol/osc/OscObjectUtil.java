package model.protocol.osc;

/**
 * This class defines helper functions to work with {@link OscObject}s.
 * @author th
 *
 */
public final class OscObjectUtil {
	private OscObjectUtil() { }

	public static OscObject createOscObject(final Object o) {
		return new OscObject(o);
	}

	public static Float toFloat(final OscObject o) {
		if (o.getInner() instanceof Float) {
			return (Float) o.getInner();
		}
		throw new IllegalArgumentException("Cannot convert " + o.getInner().getClass() + " to float.");
	}

	public static String toString(final OscObject o) {
		if (o.getInner() instanceof String) {
			return (String) o.getInner();
		}
		throw new IllegalArgumentException("Cannot convert " + o.getInner().getClass() + " to String.");
	}

	public static Boolean toBoolean(final OscObject o) {
		if (o.getInner() instanceof Boolean) {
			return (Boolean) o.getInner();
		} else if (o.getInner() instanceof Float) {
			return (Float) o.getInner() == 1.0f;
		}
		throw new IllegalArgumentException("Cannot convert " + o.getInner().getClass() + " to Boolean.");
	}

	public static Double toDouble(final OscObject o) {
		if (o.getInner() instanceof Double) {
			return (Double) o.getInner();
		}
		throw new IllegalArgumentException("Cannot convert " + o.getInner().getClass() + " to Double.");
	}

	public static Object toObject(final OscObject oscObject) {
		return oscObject.getInner();
	}
}
