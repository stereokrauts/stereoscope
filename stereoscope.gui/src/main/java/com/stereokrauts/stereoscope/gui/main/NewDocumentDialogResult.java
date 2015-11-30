package com.stereokrauts.stereoscope.gui.main;

public class NewDocumentDialogResult {
	private final String pluginId;
	private final boolean closedUsingOkay;
	
	public NewDocumentDialogResult(final String pluginId, final boolean closedUsingOkay) {
		this.pluginId = pluginId;
		this.closedUsingOkay = closedUsingOkay;
	}

	public String getPluginId() {
		return this.pluginId;
	}

	public boolean isClosedUsingOkay() {
		return this.closedUsingOkay;
	}

}
