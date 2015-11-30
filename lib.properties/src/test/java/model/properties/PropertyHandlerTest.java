package model.properties;

import static org.junit.Assert.assertEquals;
import model.properties.beans.PropertyHandler;

import org.junit.Before;
import org.junit.Test;

public final class PropertyHandlerTest {

	private StubPropertizedClass clazz;
	private StubPropertizedSubClass clazzSub;

	@Before
	public void initialize() {
		this.clazz = new StubPropertizedClass();
		this.clazz.setIpAddress("127.0.0.1");
		this.clazzSub = new StubPropertizedSubClass();
		this.clazzSub.setPort("123");
		this.clazz.setInfo(this.clazzSub);
	}

	@Test
	public void testRetrieval() throws PropertiesException {
		final PropertyCollection props = PropertyHandler.extractProperties(this.clazz);
		assertEquals("127.0.0.1", props.getElementWithName("ipAddress").getValue());
		assertEquals("123", props.getElementWithName("info.port").getValue());
	}

	@Test
	public void testAssignment() throws PropertiesException {
		final PropertyCollection props = PropertyHandler.extractProperties(this.clazz);

		props.setValue("ipAddress", "1.2.3.4");
		props.setValue("info.port", "789");

		assertEquals("1.2.3.4", this.clazz.getIpAddress());
		assertEquals("789", this.clazz.getInfo().getPort());
	}

}
