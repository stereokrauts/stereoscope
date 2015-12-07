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
public final class OscP5Adapter {
	private OscP5Adapter() { }

	public static OscMessage adaptMessage(final IOscMessage m) {
		final OscMessage packet = new OscMessage(OscAddressUtil.stringify(m.address()));
		packet.add(new Object[] { OscObjectUtil.toObject(m.get(0)) });
		return packet;
	}
}
