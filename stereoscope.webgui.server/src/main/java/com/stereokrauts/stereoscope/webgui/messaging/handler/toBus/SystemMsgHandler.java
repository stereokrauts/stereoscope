package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import com.stereokrauts.lib.commons.ApplicationPath;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSelectedAuxChanged;
import com.stereokrauts.stereoscope.model.messaging.message.layouts.MsgLayoutListResponse;
import com.stereokrauts.stereoscope.model.messaging.message.layouts.MsgLayoutResponse;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelNames;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelStrip;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncDelayTimes;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncEverything;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncGeqBandLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncOutputs;
import com.stereokrauts.stereoscope.webgui.FileHelper;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.layouts.AuxLayout;
import com.stereokrauts.stereoscope.webgui.layouts.StdLayout;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusSystemHandler;
import com.stereokrauts.stereoscope.webgui.touchosc.TouchOscLayoutParser;

public class SystemMsgHandler extends AbstractFrontendMsgHandler {
	public static final SLogger LOG = StereoscopeLogManager
			.getLogger("webgui-systemmessage-logger");
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/system/(.*)"
	};

	private final FromBusSystemHandler sender;
	private final FileHelper fileHelper = new FileHelper();
	private TouchOscLayoutParser toscParser;
	private StdLayout stdLayout;

	public SystemMsgHandler(final Webclient frontend) {
		super(frontend);
		sender = new FromBusSystemHandler(frontend);
	}

	@Override
	public boolean handleMessage(final FrontendMessage msg) {
		LOG.info("I have a new message: " + msg);
		final int aux = frontend.getState().getCurrentAux();
		final int geq = frontend.getState().getCurrentGEQ();
		final int input = frontend.getState().getCurrentInput();
		if (msg.getOscAddress().matches(".*/request/frontend/layoutList$")) {
			try {
				this.handleLayoutListRequest();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return true;
		} else if (msg.getOscAddress().matches(".*/request/frontend/layout/custom$")) {
			try {
				this.handleCustomLayoutRequest(msg);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return true;
		} else if (msg.getOscAddress().matches(".*/request/frontend/layout/std$")) {
			try {
				this.handleStdLayoutRequest(msg);
			} catch (final JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else if (msg.getOscAddress().matches(".*/request/frontend/layout/aux$")) {
			try {
				this.handleAuxLayoutRequest(msg);
			} catch (final JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else if (msg.getOscAddress().matches(".*/request/mixerProperties$")) {
			this.handleMixerPropRequest();
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/all$")) {
			this.handleFullResync(aux, geq, input);
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/channelLevels$")) {
			this.handleChannelLevelResync();
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/channelNames$")) {
			this.handleChannelNamesResync();
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/channelOn$")) {
			this.handleChannelOnResync();
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/auxSendLevels$")) {
			this.handleAuxSendLevelResync(aux);
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/auxSendOn$")) {
			this.handleAuxSendOnResync(aux);
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/geqBandLevels$")) {
			this.handleGeqBandLevelResync(geq);
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/delayTimes$")) {
			this.handleDelayTimeResync();
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/channelStrip$")) {
			this.handleChannelStripResync(input);
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/selectedAux$")) {
			this.handleSelectedAuxResync(
					this.frontend.getState().getCurrentAux());
			return true;
		} else if (msg.getOscAddress().matches(".*/resync/outputs$")) {
			this.handleOutputResync(
					this.frontend.getState().getCurrentAux());
			return true;
		} else if (msg.getOscAddress().matches(".*/interchange/touchosc$")) {
			System.out.println("Filename: " + msg.getFileName());
			System.out.println("Filesize: " + msg.getFileSize());
			System.out.println("Size of bytefield: " + msg.getBinaryValue().length);
			this.handleTouchOscData(msg);
			return true;
		}
		return false;
	}	

	private void handleTouchOscData(final FrontendMessage msg) {
		if (msg.getFileSize() == msg.getBinaryValue().length) {
			try {
				final ApplicationPath path = new ApplicationPath();
				path.storeFile(FileHelper.LAYOUT_FOLDER, new ByteArrayInputStream(msg.getBinaryValue()));
				this.handleLayoutListRequest();
				LOG.info("Successfully saved file " + msg.getFileName() + " to disc.");
			} catch (final IOException e) {
				LOG.error("Failed to save file " + msg.getFileName() + " to disc.");
				e.printStackTrace();
			}
		} else {
			LOG.error("Length of received data does not match original file size.");
		}
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	public void handleLayoutListRequest() throws IOException {
		final ApplicationPath path = new ApplicationPath();
		final Collection<File> layouts = path.readDirectory(FileHelper.LAYOUT_FOLDER);
		String files = this.getVirtualLayouts();
		files += fileHelper.getOsgiFolderEntries("stereoscope.webgui.server", FileHelper.LAYOUT_PATH);
		files += StringUtils.join(layouts, "\r\n");
		final String[] fileList = files.split("\\r?\\n");
		String loFiles = "";
		for (final String fname : fileList) {
			final String ext = fileHelper.getFileExtension(fname);
			if (ext.matches("json")) {
				if (!fileHelper.isMacro(fname)) {
					loFiles += fname + "/";
				}
			}
		}
		final String[] loFileList = loFiles.split("/");
		sender.handleMessage(new MsgLayoutListResponse(loFileList));
	}

	private String getVirtualLayouts() {
		String layouts = "standard-layout.std.json\r\n";
		for (int i = 1; i <= frontend.getState().getAuxCount(); i++) {
			layouts += "aux" + i + "-layout.aux.json\r\n";
		}
		return layouts;
	}

	private void handleCustomLayoutRequest(final FrontendMessage msg) throws IOException {
		try {
			this.loadLayout(msg.getStringValue());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void handleStdLayoutRequest(final FrontendMessage msg) throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		stdLayout = new StdLayout(this.getFrontend(), msg.getStringValue());
		//stdLayout.writeLayoutFiles();
		sender.handleMessage(new MsgLayoutResponse(stdLayout.getLayout()));
	}

	private void handleAuxLayoutRequest(final FrontendMessage msg) throws JsonGenerationException, JsonMappingException, ScriptException, IOException {
		final String[] parts = msg.getStringValue().split("#");
		final String mediaQuery = parts[1];
		final int aux = Integer.parseInt(parts[0]);
		final AuxLayout auxLayout = new AuxLayout(getFrontend(), mediaQuery, aux);
		sender.handleMessage(new MsgLayoutResponse(auxLayout.getLayout()));
	}

	private void handleMixerPropRequest() {
		this.frontend.requestMixerState();
	}

	private void handleFullResync(final int aux, final int geq, final int input) {
		final ResyncEverything msg = new ResyncEverything(aux, geq, input, true);
		this.frontend.fireChange(msg);
	}

	private void handleChannelLevelResync() {
		this.frontend.fireChange(new ResyncChannelLevels(true));
	}

	private void handleChannelNamesResync() {
		this.frontend.fireChange(new ResyncChannelNames(true));
	}

	private void handleChannelOnResync() {
		this.frontend.fireChange(new ResyncChannelOnButtons(true));
	}

	private void handleAuxSendLevelResync(final int aux) {
		this.frontend.fireChange(new ResyncAuxSendLevels(aux));
	}

	private void handleAuxSendOnResync(final int aux) {
		this.frontend.fireChange(new ResyncAuxSendOnButtons(aux));
	}

	private void handleGeqBandLevelResync(final int geq) {
		this.frontend.fireChange(new ResyncGeqBandLevels(geq));
	}

	private void handleDelayTimeResync() {
		this.frontend.fireChange(new ResyncDelayTimes(true));
	}

	private void handleChannelStripResync(final int input) {
		this.frontend.fireChange(new ResyncChannelStrip(input));
	}

	private void handleSelectedAuxResync(final int currentAux) {
		this.frontend.fireChange(new MsgSelectedAuxChanged(currentAux));	
	}

	private void handleOutputResync(final int currentAux) {
		this.frontend.fireChange(new ResyncOutputs(currentAux, true));
	}

	private void loadLayout(final String fileName) throws Exception {
		final String delimiter = File.separator;

		if (fileName.endsWith(".json")) {
			final String layout = fileHelper.getFileEntry(fileName);
			sender.handleMessage(new MsgLayoutResponse(layout));
		} else if (fileName.endsWith(".touchosc")) {
			final String files = fileHelper.getOsgiFolderEntries("stereoscope.webgui.server", FileHelper.LAYOUT_PATH);
			String toscFileName = "";
			final String[] fileList = files.split("\\r?\\n");
			Boolean isOsgiResource = false;
			for (final String fname : fileList) {
				if (fname.matches(fileName)) {
					isOsgiResource = true;
				}
			}
			InputStream touchOscInputStream = null;
			if (isOsgiResource) {
				toscFileName = FileHelper.PLUGIN_PATH + FileHelper.LAYOUT_PATH + fileName;
				touchOscInputStream = new URL(toscFileName).openStream();
			} else {
				touchOscInputStream = new ApplicationPath().readFile(fileName);
			}
			final String touchOscFileContent = fileHelper.getTouchOscXmlDescription(touchOscInputStream);
			toscParser = new TouchOscLayoutParser(touchOscFileContent);
			final String layout = toscParser.getJson();
			sender.handleMessage(new MsgLayoutResponse(layout));
		} else {
			LOG.error("Layout not found: " + fileName);
		}
	}


}
