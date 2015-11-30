package com.stereokrauts.stereoscope.wizards.document;

public class BusElementAddWizardResult {
	public enum ClientType {
		TOUCHOSC,
		WEBCLIENT
	}

	private String networkAddress;
	private String networkPort;
	private String serverNetworkPort;
	private ClientType type;
	private String webclientPort;

	public BusElementAddWizardResult(final ClientType type, final String networkAddress, final String networkPort, final String serverNetworkPort) {
		this.type = type;
		this.networkAddress = networkAddress;
		this.networkPort = networkPort;
		this.serverNetworkPort = serverNetworkPort;
	}

	public Integer getNetworkPort() {
		return networkPort != null ? Integer.parseInt(this.networkPort) : null;
	}
	public String getNetworkAddress() {
		return this.networkAddress;
	}
	public Integer getServerNetworkPort() {
		return serverNetworkPort != null ? Integer.parseInt(this.serverNetworkPort) : null;
	}

	public boolean isTouchOSC() {
		return ClientType.TOUCHOSC.equals(type);
	}

	public boolean isWebclient() {
		return ClientType.WEBCLIENT.equals(type);
	}

	public void setClientPort(final String string) {
		networkPort = string;
	}

	public void setNetworkAddress(final String ipAddress) {
		networkAddress = ipAddress;
	}

	public void setServerPort(final String text) {
		serverNetworkPort = text;
	}

	public void setType(final ClientType type) {
		this.type = type;
	}

	public void setWebClientPort(final String text) {
		webclientPort = text;
	}

	public Integer getWebclientPort() {
		return webclientPort != null ? Integer.parseInt(webclientPort) : null;
	}
}
