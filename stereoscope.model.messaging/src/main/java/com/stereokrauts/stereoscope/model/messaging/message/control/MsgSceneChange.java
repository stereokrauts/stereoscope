package com.stereokrauts.stereoscope.model.messaging.message.control;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class MsgSceneChange extends AbstractMessage<Integer> {
	private final String newSceneName;
	
	public MsgSceneChange(final Integer newSceneNumber, final String newSceneName) {
		super(newSceneNumber);
		this.newSceneName = newSceneName;
	}

	public String getNewSceneName() {
		return this.newSceneName;
	}

}
