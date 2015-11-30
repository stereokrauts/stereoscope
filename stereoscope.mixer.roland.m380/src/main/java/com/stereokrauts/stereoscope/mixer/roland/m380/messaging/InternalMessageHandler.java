package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import model.mixer.interfaces.IMixerWithGraphicalEq;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxAllSendLevels;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxAllSendOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerBusAllStatus;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerDelayAllTimeValues;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQAllBandLevel;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQIsFlexEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGroupsAllStatus;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputAllChannelNames;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputAllChannelOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputAllLevels;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputAllStripValues;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerIsMixerWithGraphicalEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerOutputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerIsMixerWithGraphicalEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerOutputCount;

public class InternalMessageHandler extends HandlerForM380 implements IMessageHandler {

	public InternalMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		if (msg instanceof RequestMixerAuxAllSendLevels) {
			mixer.getAllAuxLevels(
					((RequestMixerAuxAllSendLevels) msg).getCount());
			return true;
		} else if (msg instanceof RequestMixerAuxAllSendOnButtons) {
			//Todo: not implemented yet.
		} else if (msg instanceof RequestMixerAuxCount) {
			this.mixer.fireChange(
					new ResponseMixerAuxCount(mixer.getAuxCount(), true));
			return true;
		} else if (msg instanceof RequestMixerGEQAllBandLevel) {
			this.mixer.getAllGeqLevels(
					(byte) ((RequestMixerGEQAllBandLevel) msg).getCount());
			return true;
		} else if (msg instanceof RequestMixerGEQCount) {
			this.mixer.fireChange(
					new ResponseMixerGEQCount(mixer.getGeqCount(), true));
			return true;
		} else if (msg instanceof RequestMixerGEQIsFlexEQ) {
			this.mixer.isFlexEQ(
					(short) ((RequestMixerGEQIsFlexEQ) msg).getCount());
			return true;
		} else if (msg instanceof RequestMixerInputAllChannelNames) {
			//TODO: not implemented yet.
			return true;
		} else if (msg instanceof RequestMixerInputAllChannelOnButtons) {
			this.mixer.getAllChannelOnButtons();
			return true;
		} else if (msg instanceof RequestMixerInputAllLevels) {
			this.mixer.getAllChannelLevels();
			return true;
		} else if (msg instanceof RequestMixerInputAllStripValues) {
			this.mixer.getAllInputValues(((RequestMixerInputAllStripValues) msg).getCount());
			return true;
		} else if (msg instanceof RequestMixerInputCount) {
			this.mixer.fireChange(
					new ResponseMixerInputCount(mixer.getChannelCount(), true));
			return true;
		} else if (msg instanceof RequestMixerIsMixerWithGraphicalEQ) {
			boolean request = this.isMixerWithGraphicalEQ();
			this.mixer.fireChange(new ResponseMixerIsMixerWithGraphicalEQ(request));
			return true;
		} else if (msg instanceof RequestMixerBusAllStatus) {
			this.mixer.getAllBusStatus();
			return true;
		} else if (msg instanceof RequestMixerGroupsAllStatus) {
			this.mixer.getAllGroupsStatus();
			return true;
		} else if (msg instanceof RequestMixerDelayAllTimeValues) {
			this.mixer.getAllDelayTimes();
			return true;
		} else if (msg instanceof RequestMixerInputCount) {
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

	private boolean isMixerWithGraphicalEQ() {
		if (this.mixer instanceof IMixerWithGraphicalEq) {
			return true;
		} else {
			return false;
		}
	}

}
