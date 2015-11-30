	package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerOutputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerOutputCount;


public class InternalMessageHandler extends HandlerForLs9 implements IMessageHandler {
	public InternalMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	@Override
	public final boolean handleMessage(final IMessage msg) {
		if (msg instanceof RequestMixerInputCount) {
			this.mixer.fireChange(new ResponseMixerInputCount(this.mixer.getChannelCount(), true));
			return true;
		} else if (msg instanceof RequestMixerAuxCount) {
			this.mixer.fireChange(new ResponseMixerAuxCount(this.mixer.getAuxCount(), true));
			return true;
		} else if (msg instanceof RequestMixerGEQCount) {
			this.mixer.fireChange(new ResponseMixerGEQCount(this.mixer.getGeqCount(), true));
			return true;
		} else if (msg instanceof RequestMixerBusCount) {
			this.mixer.fireChange(new ResponseMixerBusCount(this.mixer.getBusCount(), true));
			return true;
		} else if (msg instanceof RequestMixerMatrixBusCount) {
			this.mixer.fireChange(new ResponseMixerMatrixBusCount(this.mixer.getMatrixCount(), true));
			return true;
		} else if (msg instanceof RequestMixerOutputCount) {
			this.mixer.fireChange(new ResponseMixerOutputCount(this.mixer.getOutputCount(), true));
			return true;
		} else if (msg instanceof RequestMixerName) {
			this.mixer.fireChange(new ResponseMixerName(this.mixer.getPluginName(), true));
		}
		return false;
	}
}
