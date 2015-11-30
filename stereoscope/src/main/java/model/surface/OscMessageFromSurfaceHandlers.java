package model.surface;

import model.protocol.osc.handler.OscAllMessagesHandler;
import model.protocol.osc.handler.OscDelayMsgHandler;
import model.protocol.osc.handler.OscGenericMsgHandler;
import model.protocol.osc.handler.OscGraphicalEqMsgHandler;
import model.protocol.osc.handler.OscGroupsMsgHandler;
import model.protocol.osc.handler.OscInputChannelMsgHandler;
import model.protocol.osc.handler.OscInputChannelStripDynamicsMsgHandler;
import model.protocol.osc.handler.OscInputChannelStripMsgHandler;
import model.protocol.osc.handler.OscMixerGlobalMsgHandler;
import model.protocol.osc.handler.OscOutputAuxMsgHandler;
import model.protocol.osc.handler.OscOutputMsgHandler;
import model.protocol.osc.handler.OscStateMsgHandler;

/**
 * This class consolidates all OSC message handlers.
 * @author theide
 *
 */
final class IOscMessageFromSurfaceHandlers {
	private OscInputChannelMsgHandler inputHandler;
	private OscOutputAuxMsgHandler outputAuxHandler;
	private OscAllMessagesHandler allHandler;
	private OscDelayMsgHandler delayHandler;
	private OscGenericMsgHandler genericHandler;
	private OscGraphicalEqMsgHandler geqHandler;
	private OscGroupsMsgHandler groupsHandler;
	private OscOutputMsgHandler outputHandler;
	private OscStateMsgHandler stateHandler;
	private OscInputChannelStripMsgHandler inputStripHandler;
	private OscInputChannelStripDynamicsMsgHandler dynamicsHandler;
	private OscMixerGlobalMsgHandler oscMixerGlobalMsgHandler;

	IOscMessageFromSurfaceHandlers() {
	}

	public OscInputChannelMsgHandler getInputHandler() {
		return this.inputHandler;
	}

	public void setInputHandler(final OscInputChannelMsgHandler myInputHandler) {
		this.inputHandler = myInputHandler;
	}

	public OscOutputAuxMsgHandler getOutputAuxHandler() {
		return this.outputAuxHandler;
	}

	public void setOutputAuxHandler(final OscOutputAuxMsgHandler myOutputAuxHandler) {
		this.outputAuxHandler = myOutputAuxHandler;
	}

	public OscAllMessagesHandler getAllHandler() {
		return this.allHandler;
	}

	public void setAllHandler(final OscAllMessagesHandler myAllHandler) {
		this.allHandler = myAllHandler;
	}

	public OscDelayMsgHandler getDelayHandler() {
		return this.delayHandler;
	}

	public void setDelayHandler(final OscDelayMsgHandler myDelayHandler) {
		this.delayHandler = myDelayHandler;
	}

	public OscGenericMsgHandler getGenericHandler() {
		return this.genericHandler;
	}

	public void setGenericHandler(final OscGenericMsgHandler myGenericHandler) {
		this.genericHandler = myGenericHandler;
	}

	public OscGraphicalEqMsgHandler getGeqHandler() {
		return this.geqHandler;
	}

	public void setGeqHandler(final OscGraphicalEqMsgHandler myGeqHandler) {
		this.geqHandler = myGeqHandler;
	}

	public OscGroupsMsgHandler getGroupsHandler() {
		return this.groupsHandler;
	}

	public void setGroupsHandler(final OscGroupsMsgHandler myGroupsHandler) {
		this.groupsHandler = myGroupsHandler;
	}

	public OscOutputMsgHandler getOutputHandler() {
		return this.outputHandler;
	}

	public void setOutputHandler(final OscOutputMsgHandler myOutputHandler) {
		this.outputHandler = myOutputHandler;
	}

	public OscStateMsgHandler getStateHandler() {
		return this.stateHandler;
	}

	public void setStateHandler(final OscStateMsgHandler myStateHandler) {
		this.stateHandler = myStateHandler;
	}

	public OscInputChannelStripMsgHandler getInputStripHandler() {
		return this.inputStripHandler;
	}

	public void setInputStripHandler(
			final OscInputChannelStripMsgHandler myInputStripHandler) {
		this.inputStripHandler = myInputStripHandler;
	}

	public OscInputChannelStripDynamicsMsgHandler getDynamicsHandler() {
		return this.dynamicsHandler;
	}

	public void setDynamicsHandler(
			final OscInputChannelStripDynamicsMsgHandler myDynamicsHandler) {
		this.dynamicsHandler = myDynamicsHandler;
	}

	public void setMixerGlobalHandler(
			final OscMixerGlobalMsgHandler oscMixerGlobalMsgHandler) {
		this.oscMixerGlobalMsgHandler = oscMixerGlobalMsgHandler;
	}

	public OscMixerGlobalMsgHandler getMixerGlobalMsgHandler() {
		return oscMixerGlobalMsgHandler;
	}
}