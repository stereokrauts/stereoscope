package com.stereokrauts.stereoscope.model.messaging.message.layouts;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/** This function is called, when a layout should be
 * delivered to the client after it was requested.
 * Note that the attachment is an (unzipped) string.
 *  @author jansen
 *  @param  String layout The layout description (xml or json)
 */

public class MsgLayoutResponse extends AbstractMessage<String> {

	public MsgLayoutResponse(final String layout) {
		super(layout);
	}

}
