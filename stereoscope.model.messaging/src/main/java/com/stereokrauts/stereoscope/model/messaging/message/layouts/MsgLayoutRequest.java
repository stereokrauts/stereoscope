package com.stereokrauts.stereoscope.model.messaging.message.layouts;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/** This function is called, when a client
 * requests a layout.
 *  @author jansen
 *  @param  String layoutName The name of the requested layout (e.g. 'mylayout.touchosc')
 */

public class MsgLayoutRequest extends AbstractMessage<String> {

	public MsgLayoutRequest(String layoutName) {
		super(layoutName);
	}

}
