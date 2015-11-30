package com.stereokrauts.stereoscope.webgui.layouts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.script.ScriptException;

import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.stereokrauts.stereoscope.webgui.FileHelper;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.lib.commons.*;

public class StdLayout extends AbstractLayout {
	private Webclient frontend;
	private FileHelper fileHelper;
	
	
	private String inputMacro;
	private String auxMacro;
	private String auxStatefulSwitchMacro;
	private String chStripPEQMacro;
	private String chStripInputMacro;
	private String chStripDyna1Macro;
	private String chStripDyna2Macro;
	private String geqBandMacro;
	private String delayMacro;

	private boolean generateLayoutFiles = false;
	private boolean isSmartphone = false;
	private int chnPerPage = 16;
	private int xOffset = 10;
	private int yOffset = 10;
	private int faderHeight = 250;
	private boolean splitChannelStrip = false;
	private boolean hasGeq = false;
	private int inputCount;
	private int auxCount;
	private int busCount;
	private int matrixCount;
	private int outputCount;
	private int geqCount;
	private String mixerName;
	private String[] geqBandLabels = {"20", "25", "31.5", "40", "50", "63", "80", "100", "125", "160",
			"200", "250", "315", "400", "500", "630", "800", "1k", "1k25", "1k6",
			"2k", "2k5", "3k15", "4k", "5k", "6k3", "8k", "10k", "12k5", "16k", "20k"
	};
	
	
	public StdLayout(final Webclient frontend, final String mediaQuery) throws ScriptException, JsonGenerationException, JsonMappingException, IOException {
		this.frontend = frontend;
		fileHelper = new FileHelper();
		
		this.setFrontendProperties(mediaQuery);
		this.buildLayout();
		
		if (generateLayoutFiles) {
			this.writeLayoutFiles();
		}
	}
	
	void setFrontendProperties(String mediaQuery) {
		if (mediaQuery.matches("small")) {
			//width: 0px - 640px
			this.isSmartphone = true;
			this.chnPerPage = 4;
			this.splitChannelStrip = true;
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
		} else if (mediaQuery.matches("iPhonePortrait")) {
			//320x480
			this.isSmartphone = true;
			this.chnPerPage = 4;
			this.xOffset = 20;
			this.yOffset = 30;
		} else if (mediaQuery.matches("iPhoneLandscape")) {
			//480x320
			this.isSmartphone = true;
			this.chnPerPage = 8;
			this.faderHeight = 150;
		} else if (mediaQuery.matches("iPhone5Portrait")) {
			//320x568
			this.isSmartphone = true;
			this.chnPerPage = 4;
			this.faderHeight = 350;
			this.xOffset = 30;
			this.yOffset = 30;
		} else if (mediaQuery.matches("iPhone5Landscape")) {
			//568x320
			this.isSmartphone = true;
			this.chnPerPage = 10;
			this.faderHeight = 150;
		} else if (mediaQuery.matches("iPhone6Portrait")) {
			//375x667
			this.isSmartphone = true;
			this.chnPerPage = 6;
			this.faderHeight = 420;
			this.xOffset = 8;
			this.yOffset = 40;
		} else if (mediaQuery.matches("iPhone6Landscape")) {
			//667x375
			this.isSmartphone = true;
			this.chnPerPage = 10;
			this.faderHeight = 200;
			this.xOffset = 50;
		} else if (mediaQuery.matches("iPhone6plusPortrait")) {
			//width: 414px
			this.isSmartphone = true;
			this.chnPerPage = 6;
			this.faderHeight = 500;
			this.xOffset = 25;
			this.yOffset = 40;
		} else if (mediaQuery.matches("iPhone6plusLandscape")) {
			//width: 736px
			this.isSmartphone = true;
			this.chnPerPage = 12;
			this.faderHeight = 240;
			this.xOffset = 35;
		}
	};
	
	private void setMixerName() {
		String[] name = this.frontend.getState().getName().split("\\.");
		this.mixerName = "\"" + this.capitalize(name[2]) + " " + this.capitalize(name[3]) + "\"";
	};
	
	void buildLayout() throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		
		this.loadMacros();
		this.setMixerProps();
		String header = this.getHeader();
		String inputs = this.getChannelPageTree("Inputs", 0);
		String auxiliaries = "," + this.getChannelPageTree("Auxiliaries", 1);
		
		String channelStrip = ""; 
		if (!isSmartphone) {
				channelStrip = "," + this.getChannelStrip(this.splitChannelStrip, 2);
		}
		
		String geqAndDelay = "";
		if (hasGeq && !isSmartphone) {
			geqAndDelay = "," + this.getGeq(3) + ",";
			geqAndDelay += this.getDelay(4);
		} else if (!isSmartphone){
			geqAndDelay = "," + this.getDelay(3);
		}
		
		this.setLayout("{"
					+ header
					+ "\"pages\": {"
					+ inputs
					+ auxiliaries
					+ channelStrip
					+ geqAndDelay
					+ "}"  // close "pages"
					+ "}"); // close layout
		this.setLayout(stripComments(this.getLayout()));
		//System.out.print(addLineNumbers(this.getLayout())); // this is really expensive! Use only for dev.
		this.setLayout(evaluateLayout(this.getLayout()));
	}

	private void setMixerProps() {
		this.inputCount = this.frontend.getState().getInputCount();
		this.auxCount = this.frontend.getState().getAuxCount();
		this.busCount = this.frontend.getState().getBusCount();
		this.matrixCount = this.frontend.getState().getMatrixCount();
		this.outputCount = this.frontend.getState().getOutputCount();
		this.geqCount = this.frontend.getState().getGeqCount();
		this.hasGeq = this.frontend.getState().isMixerWithGraphicalEQ();
		//this.mixerName = "\"" + this.frontend.getState().getName() + "\"";
		this.setMixerName();
	}
	
	private void loadMacros() {
		try {
			inputMacro = fileHelper.getFileEntry("input.macro.json");
			auxMacro = fileHelper.getFileEntry("aux-master.macro.json");
			auxStatefulSwitchMacro = fileHelper.getFileEntry("aux-stateful-switch.macro.json");
			chStripInputMacro = fileHelper.getFileEntry("ch-strip-input.macro.json");
			chStripPEQMacro = fileHelper.getFileEntry("ch-strip-peq.macro.json");
			chStripDyna1Macro = fileHelper.getFileEntry("ch-strip-dyna1.macro.json");
			chStripDyna2Macro = fileHelper.getFileEntry("ch-strip-dyna2.macro.json");
			geqBandMacro = fileHelper.getFileEntry("geq-band.macro.json");
			delayMacro = fileHelper.getFileEntry("output-delay.macro.json");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String getHeader() {
		return "\"setup\": {" + br
				+ "		\"version\": 10," + br
				+ "		\"mode\": \"IPAD_LANDSCAPE\"," + br
				+ "		\"name\": \"standard-layout.stsc.std.json\"," + br
				+ "		\"description\": \"The standard layout which covers all essential mixer functions\"," + br
				+ "		\"author\": \"Stereokrauts\"" + br
				+ "}," + br
				+ "\"mixer\": {" + br
				+ "		\"mixerModel\": " + this.mixerName + "," + br
				+ "		\"channelCount\": " + this.inputCount + "," + br
				+ "		\"auxCount\": " + this.auxCount + "," + br
				+ "		\"busCount\": " + this.busCount + "," + br
				+ "		\"matrixCount\": " + this.matrixCount + "," + br
				+ "		\"outputCount\": " + this.outputCount + "," + br
				+ "		\"geqCount\": " + this.geqCount + br
				+ "}," + br;
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
		String controls = "\"controls\": [";
		if (section.matches("Inputs")) {
			controls += this.getFaderPageControls(chnOffset, "inputs");
			controls += getMasterFader(section, "blue");
		} else if (section.matches("Auxiliaries")) {
			controls += this.getFaderPageControls(chnOffset, "aux");
			controls += getMasterFader(section, "green");
		} else if (section.matches("Busses")) {
			//not implemented yet
		} else {
			//fail silently
		}
		
		controls += "]";
		return controls;
	}
	
	private String getFaderPageControls(int chnOffset, String section) {
		String controls = "";
		String macro = "";
		if (section.matches("inputs")) {
			macro = this.inputMacro;
		} else {
			macro = this.auxMacro;
			controls += getStatefulAuxSwitch() + "," + br;
		}
		for (int i = 0; i < this.chnPerPage; i++) {
			int xCoord = this.xOffset + i * 50;
			String chn = this.replacePattern(macro, "n", i + chnOffset);
			chn = this.replacePattern(chn, "x", xCoord);
			chn = this.replacePattern(chn, "y", this.yOffset);
			chn = this.replacePattern(chn, "fh", this.faderHeight);
			chn = this.replacePattern(chn, "addFader", this.faderHeight + this.yOffset);
			
			if (i < this.chnPerPage - 1) {
				chn += ",";
			}
			controls += chn;
		}
		
		return controls;
	}
	
	private String getStatefulAuxSwitch() {
		String oscStr = "/stereoscope/system/state/selectedAux/label";
		String auxList = "";
		for (int i = 1; i <= auxCount; i++) {
			if (i < auxCount) {
				auxList += "\"/stereoscope/system/state/selectedAux/changeTo/" + i + "\"," + br;
			} else {
				auxList += "\"/stereoscope/system/state/selectedAux/changeTo/" + i + "\"" + br;
			}
		}
		return "{" + br
				+ "\"type\": \"statefulSwitch\"," + br
				+ "\"name\": \"Aux Stateful Switch\"," + br
				+ "\"oscMessage\": \"" + oscStr + "\"," + br
				+ "\"oscMsgList\": [" + br
				+ auxList
				+ "]," + br
				+ "\"dataType\": \"string\"," + br
				+ "\"initValue\": \"You are on Aux: \"," + br
				+ "\"smallDevice\": " + this.isSmartphone + "," + br
				+ "\"visibility\": true," + br
				+ "\"orient\": \"horizontal\"," + br
				+ "\"coords\": {\"x\": 0, \"y\": 0}," + br
				+ "\"width\": 0," + br
				+ "\"height\": 0," + br
				+ "\"color\": \"white\"" + br
				+ "}" + br;
	}
	
	private String getStatefulChannelSwitch() {
		String oscStr = "/stereoscope/system/state/selectedInput/label";
		String chList = "";
		for (int i = 1; i <= inputCount; i++) {
			if (i < inputCount) {
				chList += "\"/stereoscope/system/state/selectedInput/changeTo/" + i + "\"," + br;
			} else {
				chList += "\"/stereoscope/system/state/selectedInput/changeTo/" + i + "\"" + br;
			}
		}
		return "{" + br
				+ "\"type\": \"statefulSwitch\"," + br
				+ "\"name\": \"Channel Stateful Switch\"," + br
				+ "\"oscMessage\": \"" + oscStr + "\"," + br
				+ "\"oscMsgList\": [" + br
				+ chList
				+ "]," + br
				+ "\"dataType\": \"string\"," + br
				+ "\"initValue\": \"You are on channel: \"," + br
				+ "\"smallDevice\": " + this.isSmartphone + "," + br
				+ "\"visibility\": true," + br
				+ "\"orient\": \"horizontal\"," + br
				+ "\"coords\": {\"x\": 0, \"y\": 0}," + br
				+ "\"width\": 0," + br
				+ "\"height\": 0," + br
				+ "\"color\": \"white\"" + br
				+ "}" + br;
	}

	private String getMasterFader(String section, String color) {
		int xOffsetSum = this.chnPerPage * 50 + this.xOffset + 20;
		String oscStr = "";
		if (section.matches("Inputs")) {
			oscStr = "/stereoscope/output/master/level";
		} else if (section.matches("Auxiliaries")) {
			oscStr = "/stereoscope/stateful/aux/level";
		}
		return ",{" + br
				+ "\"type\": \"volumeFader\"," + br
				+ "\"name\": \"Volume " + section + " Output\"," + br
				+ "\"oscMessage\": \"" + oscStr + "\"," + br
				+ "\"dataType\": \"float\"," + br
				+ "\"initValue\": 0.0," + br
				+ "\"coords\": {\"x\": " + xOffsetSum + ", \"y\": " + (this.yOffset + 50) + "}," + br
				+ "\"width\": 40," + br
				+ "\"height\": " + this.faderHeight + "," + br
				+ "\"color\": \"" + color + "\"," + br
				+ "\"visibility\": true," + br
				+ "\"orient\": \"horizontal\"," + br
				+ "\"mode\": \"normal\"" + br
				+ "},{" + br
				+ "\"type\": \"staticLabel\"," + br
				+ "\"name\": \"label output\"," + br
				+ "\"oscMessage\": \"/null\"," + br
				+ "\"dataType\": \"string\"," + br
				+ "\"initValue\": \"output\"," + br
				+ "\"coords\": {\"x\": " + xOffsetSum + ",\"y\": " + (this.yOffset + this.faderHeight + 80) + "}," + br
				+ "\"width\": 40," + br
				+ "\"height\": 30," + br
				+ "\"color\": \"white\"," + br
				+ "\"visibility\": true," + br
				+ "\"orient\": \"horizontal\"," + br
				+ "\"background\": true," + br
				+ "\"bgColor\": \"gray\"," + br
				+ "\"edge\": false," + br
				+ "\"size\": 12" + br
				+ "}" + br;
	}
	
	private String getChannelStrip(Boolean splitPage, int pageCount) {
		int inputXOffset = 550;
		int inputYOffset = 20;
		String color = "brown";
		String json = "\"page" + pageCount + "\": {" + br	//OPEN BRACE
				+ "\"name\": \"Channelstrip\"," + br
				+ "\"color\": \"" + color + "\"," + br;
		if (splitPage) {
			json += "\"pages\": {" + br;					//OPEN BRACE
			json += "\"page0\": {" + br						//OPEN BRACE
					+ "\"name\": \"Parametric Equalizer\"," + br
					+ "\"color\": \"" + color + "\"," + br
					+ "\"controls\": [" + br;
			json += getStatefulChannelSwitch() + "," + br;
			json += getMacroControls(chStripPEQMacro, this.xOffset, this.yOffset) + "," + br;
			json += getMacroControls(chStripInputMacro, inputXOffset, inputYOffset) + br;
			json += "]" + br;
			json += "}," + br;								//CLOSE BRACE
			json += "\"page1\": {" + br						//OPEN BRACE
					+ "\"name\": \"Dynamics 1\"," + br
					+ "\"color\": \"" + color + "\"," + br
					+ "\"controls\": [" + br;
			json += getStatefulChannelSwitch() + "," + br;
			json += getDynamicsControls(1, this.xOffset, this.yOffset) + "," + br;
			json += getMacroControls(chStripInputMacro, inputXOffset, inputYOffset) + br;
			json += "]" + br;
			json += "}," + br;								//CLOSE BRACE
			json += "\"page2\": {" + br						//OPEN BRACE
					+ "\"name\": \"Dynamics 2\"," + br
					+ "\"color\": \"" + color + "\"," + br
					+ "\"controls\": [" + br;
			json += getStatefulChannelSwitch() + "," + br;
			json += getDynamicsControls(2, this.xOffset, this.yOffset) + "," + br;
			json += getMacroControls(chStripInputMacro, inputXOffset, inputYOffset) + br;
			json += "]" + br;
			json += "}" + br;								//CLOSE BRACE
			json += "}" + br;								//CLOSE BRACE
		} else {
			json += getChStripAll();
		}
		json += "}" + br;									//CLOSE BRACE
		return json;
	}
	
	private String getDynamicsControls(int unit, int x, int y) {
		String json = "";
		if (unit == 1) {
			json = getMacroControls(chStripDyna1Macro, x, y);
		} else if (unit == 2) {
			json = getMacroControls(chStripDyna2Macro, x, y);
		}
		return json;
	}
	
	private String getChStripAll() {
		int inputXOffset = 550;
		int inputYOffset = 20;
		int peqXOffset = 20;
		int peqYOffset = 40;
		int dyna1XOffset = 650;
		int dyna1YOffset = 20;
		int dyna2XOffset = 650;
		int dyna2YOffset = 430;
		
		String json = "";
		json += "\"controls\": [" + br;
		json += getStatefulChannelSwitch() + "," + br;
		json += getMacroControls(chStripPEQMacro, peqXOffset, peqYOffset) + "," + br;
		json += getMacroControls(chStripInputMacro, inputXOffset, inputYOffset) + "," + br;
		json += getMacroControls(chStripDyna1Macro, dyna1XOffset, dyna1YOffset) + "," + br;
		json += getMacroControls(chStripDyna2Macro, dyna2XOffset, dyna2YOffset) + br;
		json += "]" + br;
		return json;
	}

	private String getGeq(int pageCount) {
		String color = "brown";
		String json = "";
		json = "\"page" + pageCount + "\": {" + br			//OPEN BRACE
				+ "\"name\": \"Graqhical EQ\"," + br
				+ "\"color\": \"" + color + "\"," + br;
		json += getGeqControls("left");
		json += "}" + br;									//CLOSE BRACE

		return json;
	}

	private String getGeqControls(String side) {
		int xOffset = 0;
		int yOffset = 0;
		int bandWidth = 40;
		String json = "";
		String controls = "";

		for (int i = 0; i < geqBandLabels.length; i++) {
			xOffset = i * bandWidth;
			String band = getMacroControls(geqBandMacro, xOffset, yOffset);
			band = replacePattern(band, "n", (i + 1));
			band = replacePattern(band, "m", side);
			band = replacePattern(band, "hz", geqBandLabels[i]);
			controls += band;
			if ((i + 1) != geqBandLabels.length) {
				controls += ",";
			}
			controls += br;
		}
		json += "\"controls\": [" + br;
		json += controls;
		json += "]" + br;

		return json;
	}
	
	private String getDelay(int pageCount) {
		int xOffset = 20;
		int yOffset = 20;
		String color = "white";
		String json = "";
		String controls = "";
		
		controls += getDelayUnit("bus", xOffset, yOffset) + "," + br;
		controls += getDelayUnit("aux", xOffset, (yOffset + 140)) + "," + br;
		controls += getDelayUnit("omni", xOffset, (yOffset + 280)) + br;
		
		json = "\"page" + pageCount + "\": {" + br			//OPEN BRACE
				+ "\"name\": \"Delay\"," + br
				+ "\"color\": \"" + color + "\"," + br;
		json += "\"controls\": [" + br;
		json += controls;
		json += "]" + br;
		json += "}" + br;									//CLOSE BRACE
		return json;
	}
	
	private String getDelayUnit(String unit, int x, int y) {
		int unitCount = 0;
		String json = getDelayUnitLabel(unit, x, (y + 35)) + "," + br;
		x += 40;
		if (unit == "bus") {
			unitCount = busCount;
		} else if (unit == "aux") {
			unitCount = auxCount;
		} else if (unit == "omni") {
			unitCount = outputCount;
		} else {
			frontend.LOG.error("Unknown delay unit of name " + unit);
		}
		for (int i = 0; i < unitCount; i++) {
			int xOffset = x + (i * 40);
			json += getSingleDelayControl(unit, (i + 1), xOffset, y);
			if ((i + 1) != unitCount) {
				json += "," + br;
			} else {
				json += br;
			}
		}
		return json;
	}
	
	private String getSingleDelayControl(String unit, int count, int x, int y) {
		String json = getMacroControls(delayMacro, x, y);
		json = replacePattern(json, "unit", unit);
		json = replacePattern(json, "n", count);
		return json;
	}
	
	private String getDelayUnitLabel(String unit, int x, int y) {
		unit = WordUtils.capitalize(unit);
		return "{" + br
				+ "\"type\": \"staticLabel\"," + br
				+ "\"name\": \"" + unit + " Delay\"," + br
				+ "\"oscMessage\": \"/null\"," + br
				+ "\"dataType\": \"string\"," + br
				+ "\"initValue\":\"" + unit + "\"," + br
				+ "\"coords\": {\"x\": " + x + ", \"y\": " + y + "}," + br
				+ "\"width\": 30," + br
				+ "\"height\": 30," + br
				+ "\"color\": \"gray\"," + br
				+ "\"visibility\": true," + br
				+ "\"orient\": \"horizontatl\"," + br
				+ "\"bgColor\": \"gray\"," + br
				+ "\"background\": true," + br
				+ "\"edge\": false," + br
				+ "\"size\": 12" + br
				+ "}";
				
	}
	
	private String getDelayUnitBackground(int x, int y, int w, int h) {
		
		return "";
	}

	private String getMacroControls(String macro, int xOffset, int yOffset) {
		String json = replacePattern(macro, "x", xOffset);
		json = replacePattern(json, "y", yOffset);
		return json;
	}
	
	public void writeLayoutFiles() throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		String[] mediaQueries = this.getMediaQueryArray();
		
		for (String mq: mediaQueries) {
			this.setFrontendProperties(mq);
			this.buildLayout();
			String fname = "layout-std-" + mq + ".json";
			
			addLayout(fname, this.getLayout());
		}
	}
	
	private void addLayout(final String fname, final String layout) {
		final ApplicationPath path = new ApplicationPath();
		
		InputStream stream = new ByteArrayInputStream(layout.getBytes(StandardCharsets.UTF_8));
		path.storeFile(fname, stream);
		
	}
	
	private String[] getMediaQueryArray() {
		String[] mqa = {
				"small",
				"medium",
				"large",
				"xlarge",
				"xxlarge",
				"iPhonePortrait",
				"iPhoneLandscape",
				"iPhone5Portrait",
				"iPhone5Landscape",
				"iPhone6Portrait",
				"iPhone6Landscape",
				"iPhone6plusPortrait",
				"iPhone6plusLandscape"
		};
		return mqa;
	}

}
