package com.stereokrauts.stereoscope.model.messaging.message.layouts;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/** This function is called, when a client
 * requests a list of available layouts.
 *  @author jansen
 *  @param  String[] layoutFilter An array of layout types. If empty, all layouts should be returned.
 */

public class MsgLayoutListRequest extends AbstractMessage<String[]>{
	
	public MsgLayoutListRequest(final String[] layoutFilter) {
		super(layoutFilter);
	}

}
