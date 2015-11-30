package model.properties;

import model.properties.beans.PropertyBean;
import model.properties.beans.PropertyBind;

public final class StubPropertizedClass {
	@PropertyBean
	private StubPropertizedSubClass info;

	@PropertyBind(displayName = "ip", group = DefaultPropertyGroup.class)
	private String ipAddress;

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public StubPropertizedSubClass getInfo() {
		return this.info;
	}

	public void setInfo(final StubPropertizedSubClass info) {
		this.info = info;
	}
}
