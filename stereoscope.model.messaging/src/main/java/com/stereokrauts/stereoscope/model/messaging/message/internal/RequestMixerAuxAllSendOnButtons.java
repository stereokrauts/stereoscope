/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

/**
 * This message should be triggered, when a system component
 * needs to know the status of all aux send-on buttons 
 * for one specific auxiliary at once, e.g. for stateful messaging.
 * @param int aux The auxiliary that the sends belong to.
 * @author jansen
 *
 */
public class RequestMixerAuxAllSendOnButtons extends AbstractInternalCountMessage<Boolean> {

	/**
	 * @param attachment should always be true.
	 */
	public RequestMixerAuxAllSendOnButtons(int aux, boolean attachment) {
		super(aux, attachment);
	}

}
