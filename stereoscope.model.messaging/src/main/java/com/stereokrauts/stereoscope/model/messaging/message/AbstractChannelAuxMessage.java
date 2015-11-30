package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;

/**
 * This class represents a base class for all channel
 * auxiliary send messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractChannelAuxMessage<T> extends AbstractMessage<T> {
	private final int aux;
	private int channel;
	
	public AbstractChannelAuxMessage(final int myChannel, final int myAux, final T attachment) {
		super(attachment);
		this.aux = myAux;
		this.setChannel(myChannel);
	}
	
	@AddressComponent
	public final int getAux() {
		return this.aux;
	}

	@AddressComponent
	private void setChannel(final int myChannel) {
		this.channel = myChannel;
	}

	@AddressComponent
	public final int getChannel() {
		return this.channel;
	}
	
	
	
	

}
