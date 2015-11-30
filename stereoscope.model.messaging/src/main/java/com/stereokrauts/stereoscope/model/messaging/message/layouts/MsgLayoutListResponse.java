package com.stereokrauts.stereoscope.model.messaging.message.layouts;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/** This function is called, when a client
 * requested a list of available layouts and
 * the resulting list should be delivered back
 * to it.
 *  @author jansen
 *  @param  String[] layoutList An array of layout names.
 */

public class MsgLayoutListResponse extends AbstractMessage<String[]> {

	public MsgLayoutListResponse(String[] layoutList) {
		super(layoutList);
	}

}
