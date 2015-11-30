package model.protocol.osc.bridge.oscp5;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscObjectUtil;
import oscP5.OscMessage;

/**
 * This adapter translates between oscP5 and stereoscope OSC.
 * @author th
 *
 */
public final class OscMessageUtil {
	private OscMessageUtil() { }

	public static IOscMessage adapt(final OscMessage msg) {
		final model.protocol.osc.impl.OscMessage returnValue = new model.protocol.osc.impl.OscMessage(OscAddressUtil.create(msg.addrPattern()));
		for (final Object argument : msg.arguments()) {
			returnValue.add(OscObjectUtil.createOscObject(argument));
		}
		return returnValue;
	}

}
