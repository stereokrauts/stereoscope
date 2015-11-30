package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;
import com.stereokrauts.stereoscope.plugin.model.InternalMixerPluginBuilder;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.api.IWebclient;

import model.beans.BusAttendeeBean;
import model.beans.BusBean;
import model.beans.ClientBean;
import model.beans.ConnectionServerTcpUdpIpBean;
import model.beans.ConnectionTcpUdpIpBean;
import model.beans.DocumentBean;
import model.beans.MixerBean;
import model.beans.MixerConnectionBean;
import model.beans.OscSurfaceBean;
import model.beans.WebclientBean;
import model.bus.Bus;
import model.bus.BusAttendee;
import model.bus.BusAttendeeMixer;
import model.bus.BusAttendeeSurfaceTouchOsc;
import model.bus.BusAttendeeWebclient;
import model.bus.ClassicBus;
import model.mixer.interfaces.IAmMixer;
import model.persistance.DocumentLoader;
import model.persistance.DocumentSaver;
import model.plugin.ApplicationContext;
import model.surface.touchosc.TouchOscSurface;

/**
 * This class represents a document, which is a collection of
 * mixers and surfaces and surrounding configuration. Main
 * container of Document instances is the Model.
 * @author theide
 *
 */
public final class Document {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(Document.class);

	private Model model;

	private List<IMixerPlugin> mixerPlugins;
	private final Bus masterBus = new ClassicBus();
	private final HashMap<Object, BusAttendee> busElementAssoc = new HashMap<Object, BusAttendee>();

	private final List<IObserveDocumentChanges> observers = new ArrayList<IObserveDocumentChanges>();
	private DocumentBean bean;
	private File fileRepresentation;
	private boolean dirtyFlag;
	private final ApplicationContext context = new ApplicationContext();

	public Document() {
		this.mixerPlugins = new ArrayList<IMixerPlugin>();
		this.bean = new DocumentBean();
		this.bean.setName("<unknown>");
		final ArrayList<BusBean> bussesBean = new ArrayList<BusBean>();
		bussesBean.add(this.masterBus.getBean());
		this.bean.setBusList(bussesBean);
		this.dirtyFlag = true;
	}

	public Document(final File loadFromFile) throws Exception {
		this.setFileRepresentation(loadFromFile);
		this.bean = DocumentLoader.load(new FileInputStream(loadFromFile));

		this.mixerPlugins = new ArrayList<IMixerPlugin>();
		if (this.bean.getBussesList().size() != 1) {
			throw new RuntimeException("Number of busses MUST be one currently.");
		}
		for (final BusAttendeeBean b : this.bean.getBussesList().get(0).getAttendeesList()) {
			if (b instanceof MixerBean) {
				final MixerBean mixerBean = (MixerBean) b;
				final IMixerPlugin plugin = InternalMixerPluginBuilder.build(mixerBean.getPlugin().getPluginId(), this.getApplicationContextForPlugin(), new IPersistentPluginConfiguration() {
					@Override
					public MixerConnectionBean getConnectionBean() {
						return mixerBean.getConnection();
					}
				});
				this.addMixerPlugin(plugin);
			} else if (b instanceof OscSurfaceBean) {
				this.addTouchOscSurface((OscSurfaceBean) b);
			} else if (b instanceof WebclientBean) {
				this.addWebclient((WebclientBean) b);
			}
		}
		dirtyFlag = false;
	}

	public ApplicationContext getApplicationContextForPlugin() {
		return context;
	}

	public void addMixerPlugin(final IMixerPlugin plugin) {
		this.mixerPlugins.add(plugin);
		final BusAttendeeMixer attendee = new BusAttendeeMixer(plugin);
		plugin.registerCommunicationAware(attendee);
		this.busElementAssoc.put(plugin, attendee);
		this.masterBus.addAttendee(attendee, -1);
		plugin.getMixer().registerObserver(this.masterBus.getDispatchEndpoint());
		this.fireBusElementAdded(this.masterBus, attendee);
		this.dirtyFlag = true;
	}

	public TouchOscSurface addTouchOscSurface(final String networkAddress, final int portNumber, final int serverPortNumber) throws Exception {
		final OscSurfaceBean surfaceConfiguration = this.buildOscSurfaceBeanFromParameters(networkAddress, portNumber,
				serverPortNumber);
		final TouchOscSurface srf = new TouchOscSurface(surfaceConfiguration);
		final BusAttendee attendee = new BusAttendeeSurfaceTouchOsc(srf);
		this.masterBus.addAttendee(attendee, -1);
		this.busElementAssoc.put(srf, attendee);
		srf.registerObserver(this.masterBus.getDispatchEndpoint());
		this.fireBusElementAdded(this.masterBus, attendee);
		this.initSurface(srf);
		this.dirtyFlag = true;
		return srf;
	}

	private void addTouchOscSurface(final OscSurfaceBean b) throws Exception {
		final TouchOscSurface srf = new TouchOscSurface(b);
		final BusAttendee attendee = new BusAttendeeSurfaceTouchOsc(srf);
		this.masterBus.addAttendee(attendee, -1);
		this.busElementAssoc.put(srf, attendee);
		srf.registerObserver(this.masterBus.getDispatchEndpoint());
		this.fireBusElementAdded(this.masterBus, attendee);
		this.initSurface(srf);
		this.dirtyFlag = true;
	}

	public void addWebclient(final int webclientPort) {
		final WebclientBean bean2 = new WebclientBean();
		bean2.setPortNumber(webclientPort);
		final ClientBean clientConfig = new ClientBean();
		clientConfig.setAccessibleAux(0);
		clientConfig.setClientName("Webclient Port " + webclientPort);
		clientConfig.setRestrictedClient(Boolean.FALSE);
		clientConfig.setSnapFaders(Boolean.TRUE);
		bean2.setClientConfig(clientConfig);
		addWebclient(bean2);
		this.dirtyFlag = true;
	}

	private void addWebclient(final WebclientBean b) {
		final IWebclient webclient = Webclient.getInstance(b.getPortNumber());
		final BusAttendee attendee = new BusAttendeeWebclient(webclient, b);
		this.masterBus.addAttendee(attendee, -1);
		webclient.registerMessageDispatcher(masterBus.getDispatchEndpoint());
		this.fireBusElementAdded(this.masterBus, attendee);
	}

	public void addBusAttendee(final Bus bus, final BusAttendee attendee, final int index)  {
		bus.addAttendee(attendee, index);
		this.dirtyFlag = true;
		this.fireBusElementAdded(bus, attendee);
	}

	public Bus getMasterBus() {
		return this.masterBus;
	}

	private void fireBusElementAdded(final Bus bus, final BusAttendee attendee) {
		for (final IObserveDocumentChanges observer : this.observers) {
			observer.busElementAdded(bus, attendee);
		}
	}

	private void fireBusElementRemoved(final Bus bus, final BusAttendee attendee) {
		for (final IObserveDocumentChanges observer : this.observers) {
			observer.busElementRemoved(bus, attendee);
		}
	}

	public void registerObserver(final IObserveDocumentChanges observer) {
		this.observers.add(observer);
	}

	private OscSurfaceBean buildOscSurfaceBeanFromParameters(final String networkAddress, final int portNumber, final int serverPortNumber) {
		final OscSurfaceBean surfaceConfiguration = new OscSurfaceBean();
		surfaceConfiguration.setClientConfig(new ClientBean());
		final ConnectionServerTcpUdpIpBean serverBean = new ConnectionServerTcpUdpIpBean();
		serverBean.setPortNumber(serverPortNumber);
		final ConnectionTcpUdpIpBean clientBean = new ConnectionTcpUdpIpBean();
		clientBean.setNetworkAddress(networkAddress);
		clientBean.setPortNumber(portNumber);
		clientBean.setUseUdp(true);
		surfaceConfiguration.setLocalConnParams(serverBean);
		surfaceConfiguration.setRemoteConnParams(clientBean);
		return surfaceConfiguration;
	}

	private void initSurface(final TouchOscSurface srf) {
		srf.connect();
		this.getAllMixerValues(srf, srf.getBus().getTheMixer());
	}

	private void getAllMixerValues(final TouchOscSurface srf, final IAmMixer theMixer) {
		theMixer.getAllChannelLevels();
		theMixer.getAllGroupsStatus();
		theMixer.getAllChannelOnButtons();
		theMixer.getAllAuxLevels(srf.getCurrentAux());
		theMixer.getAllDelayTimes();
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(final Model model) {
		LOG.info("Registered model " + model);
		this.model = model;
	}

	public void removeTouchOscSurface(final TouchOscSurface addedSurface) {
		final BusAttendee attendee = this.busElementAssoc.get(addedSurface);
		this.masterBus.removeAttendee(attendee);
		this.fireBusElementRemoved(this.masterBus, attendee);
		this.dirtyFlag = true;
	}

	public void removeMixerPlugin(final IMixerPlugin plugin) {
		this.masterBus.removeAttendee(this.busElementAssoc.get(plugin));
		this.fireBusElementRemoved(this.masterBus, this.busElementAssoc.get(plugin));
		this.mixerPlugins.remove(plugin);
		this.dirtyFlag = true;
	}

	public int removeBusAttendee(final Bus bus, final BusAttendee attendee) {
		this.fireBusElementRemoved(bus, attendee);
		this.dirtyFlag = true;
		return bus.removeAttendee(attendee);
	}

	public Object getBean() {
		return this.bean;
	}

	public void save() {
		if (this.fileRepresentation != null) {
			try {
				DocumentSaver.save(this.bean, new FileOutputStream(this.fileRepresentation));
				this.dirtyFlag = false;
			} catch (final Exception e) {
				LOG.fatal("Could not save document to " + this.fileRepresentation, e);
			}
		} else {
			throw new IllegalStateException("Save called, but I have no file representation set!");
		}
	}

	public File getFileRepresentation() {
		return this.fileRepresentation;
	}

	public void setFileRepresentation(final File fileRepresentation) {
		this.fileRepresentation = fileRepresentation;
	}

	public boolean isDirty() {
		return this.dirtyFlag;
	}

	public void shutdown() {
		this.fireShutdown();
		this.masterBus.shutdown();
	}

	private void fireShutdown() {
		for (final IObserveDocumentChanges observer : this.observers) {
			observer.documentShutdown(this);
		}
	}

	public void setDirty(final boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
}
