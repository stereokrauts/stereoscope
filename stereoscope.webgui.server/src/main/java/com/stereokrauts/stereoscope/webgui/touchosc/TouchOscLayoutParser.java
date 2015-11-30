package com.stereokrauts.stereoscope.webgui.touchosc;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;

public class TouchOscLayoutParser {

	String xmlContent;
	String jsonContent;
	Layout layout;

	public TouchOscLayoutParser(final String touchOscFileContents) throws Exception {
		this.buildObjectTree();


		this.convertLayoutToJson();
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public Layout getLayout() {
		return this.layout;
	}

	public String getJson() {
		return this.jsonContent;
	}

	public void buildObjectTree() throws JAXBException {
		final JAXBContext jc = JAXBContext.newInstance(new Class[] {Layout.class} );
		final Unmarshaller um = jc.createUnmarshaller();
		this.layout = (Layout) um.unmarshal(new StreamSource(new StringReader(this.xmlContent)));
	}

	public void convertLayoutToJson() throws Exception {
		this.jsonContent = "{";

		if (this.layout != null) {
			this.jsonContent += this.getJsonSetupSection() + ",";
			this.jsonContent += this.getJsonPageSection();

		}
		this.jsonContent += "}";
	}

	private String getJsonPageSection() throws Exception {
		String json = "\"pages\": {";
		for (int i = 0; i < this.layout.getTabpage().size(); i++) {
			final Tabpage page = this.layout.getTabpage().get(i);
			json += "\"page" + i + "\": {";
			json += "\"name\": \"" + decode(page.getName()) + "\",";
			json += "\"color\": \"gray\",";
			json += "\"controls\": [";
			for (final Control element : page.getControl()) {
				json += this.getJsonControlElement(element);
			}
			json += "] } ";
			if (i < this.layout.getTabpage().size() - 1) {
				json += ",";
			}

		}
		json += "}";
		return json;
	}

	private String getJsonControlElement(final Control element) throws Exception {
		String json = "{ ";
		json += this.getControlType(element.getType()) + ", ";
		json += "\"name\": \"" + decode(element.getName()) + "\", ";
		json += this.getCtlDimensions(element) + ",";
		json += this.getCtlCoords(element) + ",";
		json += "\"color\": \"" + element.getColor() + "\", ";
		json += "\"oscMessage\": \"" + element.getOscCs() + "\", ";
		if (element.getType().matches("^label.*")) {
			json += "\"dataType\": \"string\",";
			json += "\"initValue\": \"" + decode(element.getText()) + "\", ";
			json += "\"size\": " + element.getSize() + ", ";
			json += "\"background\": ";
			json += element.isBackground() ? "true" : "false";
			json += ", \"edge\": ";
			json += element.isOutline() ? "true" : "false";
			json += ", ";
		} else if (element.getType().matches("^fader.*") || element.getType().matches("^rotary.*")) {
			json += "\"dataType\": \"float\",";
			json += "\"initValue\": 0.0, ";
		} else if (element.getType().matches("push") || element.getType().matches("toggle")) {
			json += "\"dataType\": \"boolean\",";
			json += "\"initValue\": false, ";
		} else if (element.getType().matches("led")) {
			json += "\"dataType\": \"boolean\",";
			json += "\"initValue\": false, ";
		}
		json += "\"visibility\": true";
		json += " },";
		return json;
	}

	private String getControlType(final String toscType) throws Exception {
		switch(toscType) {
		case "faderv":
			return "\"type\": \"basicFader\", \"orient\": \"vertical\"";
		case "faderh":
			return "\"type\": \"basicFader\", \"orient\": \"horizontal\"";
		case "labelv":
			return "\"type\": \"basicLabel\", \"orient\": \"vertical\"";
		case "labelh":
			return "\"type\": \"basicLabel\", \"orient\": \"horizontal\"";
		case "rotaryv":
			return "\"type\": \"basicRotary\", \"orient\": \"vertical\"";
		case "rotaryh":
			return "\"type\": \"basicRotary\", \"orient\": \"horizontal\"";
		case "toggle":
			return "\"type\": \"basicToggleButton\"";
		case "push":
			return "\"type\": \"basicButton\"";
		case "led":
			return "\"type\": \"basicVisualFeedback\"";
		default: throw new Exception("Control of type '" + toscType + "' not supported.");
		}
	}

	private String getJsonSetupSection() {
		String mode;
		if (this.layout.getMode().intValue() == 1 
				&& this.layout.getOrientation().matches("vertical")) {
			mode = "IPAD_LANDSCAPE";
		} else if (this.layout.getMode().intValue() == 1 
				&& this.layout.getOrientation().matches("horizontal")) {
			mode = "IPAD_PORTRAIT";
		} else if (this.layout.getMode().intValue() == 0 
				&& this.layout.getOrientation().matches("vertical")) {
			mode = "IPHONE_LANDSCAPE";
		} else if (this.layout.getMode().intValue() == 0 
				&& this.layout.getOrientation().matches("horizontal")) {
			mode = "IPHONE_PORTRAIT";
		} else {
			mode = "IPAD_LANDSCAPE";
		}
		return "\"setup\": {"
		+ "\"version\": 1.0,"
		+ "\"mode\": \"" + mode + "\","
		+ "\"description\": \"Converted from TouchOSC layout.\","
		+ "\"author\": \"Unknown\" } ";
	}

	private String getCtlCoords(final Control element) {
		String coords = "\"coords\": {";
		final int x = element.getX();
		final int y = element.getY();
		if (this.layout.getOrientation().matches("vertical")) {
			int xMax;
			if (this.layout.getMode().intValue() == 1) {
				xMax = 768;
			} else {
				xMax = 640;
			}
			final int realX = y;
			final int realY = xMax - x - element.getW();
			coords += "\"x\": " + realX + ", \"y\": " + realY;
		} else {
			coords += "\"x\": " + x + ", \"y\": " + y;
		}
		coords += "}";
		return coords;
	}

	private String getCtlDimensions(final Control element) {
		String dim = "";
		if (this.layout.getOrientation().matches("vertical")) {
			dim += "\"width\": " + element.getH() + ", ";
			dim += "\"height\": " + element.getW();
		} else {
			dim += "\"width\": " + element.getW() + ", ";
			dim += "\"height\": " + element.getH();
		}
		return dim;
	}

	private String decode(final String str) throws UnsupportedEncodingException {
		final byte[] decoded = Base64.decodeBase64(str);
		return new String(decoded, "UTF-8");
	}

}
