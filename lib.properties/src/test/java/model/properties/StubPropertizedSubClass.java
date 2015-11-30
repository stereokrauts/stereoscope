package model.properties;

import model.properties.beans.PropertyBind;

public final class StubPropertizedSubClass {
	@PropertyBind(displayName = "port", group = DefaultPropertyGroup.class)
	private String port;

	public String getPort() {
		return this.port;
	}

	public void setPort(final String port) {
		this.port = port;
	}
}
