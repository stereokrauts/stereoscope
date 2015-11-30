package com.stereokrauts.stereoscope.webgui.layouts;

import java.io.IOException;

import javax.script.ScriptException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.stereokrauts.stereoscope.webgui.FileHelper;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class AuxLayout extends AbstractLayout {
	private Webclient frontend;
	private FileHelper fileHelper;
	private String layout;
	private String inputMacro;
	private String auxMacro;
	private String auxStatefulSwitchMacro;

	private int chnPerPage = 16;
	private int auxNumber;
	private int inputCount;
	private int auxCount;
	private int busCount;
	private int matrixCount;
	private int outputCount;
	private int geqCount;
	private String mixerName;
	
	public AuxLayout(final Webclient frontend, final String mediaQuery, final int aux) throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		this.frontend = frontend;
		fileHelper = new FileHelper();
		this.auxNumber = aux;
		this.setFrontendProperties(mediaQuery);
		this.buildLayout();
	}
	
	public String getLayout() {
		return this.layout;
	}
	
	void setFrontendProperties(String mediaQuery) {
		if (mediaQuery.matches("small")) {
			//width: 0px - 640px
			this.chnPerPage = 4;
		} else if (mediaQuery.matches("medium")) {
			//width: 641px - 1024px
			this.chnPerPage = 12;
		} else if (mediaQuery.matches("large")) {
			//width: 1025px - 1440px
			this.chnPerPage = 16;
		} else if (mediaQuery.matches("xlarge")) {
			//width: 1441px - 1920px
			this.chnPerPage = 32;
		} else if (mediaQuery.matches("xxlarge")) {
			//width: 1921px
			this.chnPerPage = 48;
		} else {
			
		}
	}
	
	private void setMixerName() {
		String[] name = this.frontend.getState().getName().split("\\.");
		this.mixerName = "\"" + this.capitalize(name[2]) + " " + this.capitalize(name[3]) + "\"";
	};
	
	void buildLayout() throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		
		this.loadMacros();
		this.setMixerProps();
		String header = this.getHeader();
		String auxiliaries = this.getChannelPageTree("Auxiliaries", 0);
		this.layout = "{"
					+ header
					+ "\"pages\": {"
					+ auxiliaries 
					+ "}"  // close "pages"
					+ "}"; // close layout
		System.out.print(this.layout);
		this.setLayout(evaluateLayout(this.getLayout()));
	}
	
	private void setMixerProps() {
		this.inputCount = this.frontend.getState().getInputCount();
		this.auxCount = this.frontend.getState().getAuxCount();
		this.busCount = this.frontend.getState().getBusCount();
		this.matrixCount = this.frontend.getState().getMatrixCount();
		this.outputCount = this.frontend.getState().getOutputCount();
		this.geqCount = this.frontend.getState().getGeqCount();
		this.setMixerName();
	}
	
	private void loadMacros() {
		try {
			auxMacro = fileHelper.getFileEntry("aux-static.macro.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String getHeader() {
		return "\"setup\": {"
				+ "		\"version\": 10,"
				+ "		\"mode\": \"IPAD_LANDSCAPE\" "
				+ "},"
				+ "\"mixer\": {"
				+ "		\"mixerModel\": " + this.mixerName + ","
				+ "		\"channelCount\": " + this.inputCount + ","
				+ "		\"auxCount\": " + this.auxCount + ","
				+ "		\"busCount\": " + this.busCount + ","
				+ "		\"matrixCount\": " + this.matrixCount + ","
				+ "		\"outputCount\": " + this.outputCount + ","
				+ "		\"geqCount\": " + this.geqCount
				+ "},";
	}
	
	private String getChannelPageTree(String section, int pageCount) {
		String pageTree = "\"page" + pageCount + "\": {"	//OPEN BRACE
				+ "\"name\": \"" + section + "\",";
		
		if (this.inputCount <= this.chnPerPage) {
			pageTree += this.getControls(section, 0);
		} else {
			pageTree += "\"pages\": {";						//OPEN BRACE
			int subPageCount = this.inputCount / 16;
			if (this.inputCount % this.chnPerPage > 0) {
				subPageCount += 1;
			}
			for (int i = 0; i < subPageCount; i++) {
				int a = i * this.chnPerPage + 1;
				int b = (i + 1) * this.chnPerPage;
				String pageName = section + a + "-" + b;
				pageTree += "\"page" + i + "\": {"			//OPEN BRACE
						+ "\"name\": \"" + pageName + "\",";
				pageTree += this.getControls(section, a);
				pageTree += "}";							//CLOSE BRACE
				if (i < subPageCount - 1) {
					pageTree += ",";
				}
			}
			pageTree += "}";								//CLOSE BRACE
		}
		pageTree += "}";									//CLOSE BRACE
		return pageTree;
	}
	
	private String getControls(String section, int chnOffset) {
		int xOffset = 20;
		int yOffset = 50;
		String controls = "\"controls\": [";
		if (section.matches("Auxiliaries")) {
			controls += this.getAuxControls(xOffset, yOffset, chnOffset);
			controls += getMasterFader(section, xOffset, yOffset, "green");
		} else {
			//fail silently
		}
		
		controls += "]";
		return controls;
	}
	
	private String getAuxControls(int xOffset, int yOffset, int chnOffset) {
		String controls = "";
		//int switchYOffset = 480;
		for (int i = 0; i < this.chnPerPage; i++) {
			int xCoord = xOffset + i * 50;
			String chn = this.replacePattern(this.auxMacro, "n", i + chnOffset);
			chn = this.replacePattern(chn, "m", auxNumber);
			chn = this.replacePattern(chn, "x", xCoord);
			chn = this.replacePattern(chn, "y", yOffset);
			
			if (i < this.chnPerPage - 1) {
				chn += ",";
			}
			controls += chn;
		}
		/*
		for (int i = 0; i < this.auxCount; i++) {
			int xCoord = xOffset + i * 50;
			String auxSwitch = ",";
			auxSwitch += this.replaceParameter(this.auxStatefulSwitchMacro, "%n", i + 1);
			auxSwitch = this.replaceParameter(auxSwitch, "%x", xCoord);
			auxSwitch = this.replaceParameter(auxSwitch, "%y", switchYOffset);
			controls += auxSwitch;
		}
		*/
		
		return controls;
	}
	
	private String getMasterFader(String section, int xOffset, int yOffset, String color) {
		int xOffsetSum = this.chnPerPage * 50 + xOffset + 20;
		String oscStr = "";
		if (section.matches("Inputs")) {
			oscStr = "/stereoscope/output/master/level";
		} else if (section.matches("Auxiliaries")) {
			oscStr = "/stereoscope/stateful/aux/level";
		}
		return ",{"
				+ "\"type\": \"volumeFader\","
				+ "\"name\": \"Volume " + section + " Output\","
				+ "\"oscMessage\": \"" + oscStr + "\","
				+ "\"dataType\": \"float\","
				+ "\"initValue\": 0.0,"
				+ "\"coords\": {\"x\": " + xOffsetSum + ", \"y\": " + (yOffset + 50) + "},"
				+ "\"width\": 40,"
				+ "\"height\": 250,"
				+ "\"color\": \"" + color + "\","
				+ "\"visibility\": true,"
				+ "\"orient\": \"horizontal\","
				+ "\"mode\": \"normal\""
				+ "},{"
				+ "\"type\": \"staticLabel\","
				+ "\"name\": \"label output\","
				+ "\"oscMessage\": \"/null\","
				+ "\"dataType\": \"string\","
				+ "\"initValue\": \"output\","
				+ "\"coords\": {\"x\": " + xOffsetSum + ",\"y\": " + (yOffset + 330) + "},"
				+ "\"width\": 40,"
				+ "\"height\": 30,"
				+ "\"color\": \"white\","
				+ "\"visibility\": true,"
				+ "\"orient\": \"horizontal\","
				+ "\"background\": true,"
				+ "\"bgColor\": \"gray\","
				+ "\"edge\": false,"
				+ "\"size\": 12"
				+ "}";
	}
	
}
