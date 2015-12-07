package model.surface.touchosc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import model.protocol.osc.OscConstants;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class is used to advertise the stereoscope services in the
 * network, so that TouchOSC clients recognize this host automatically.
 * @author theide
 *
 */
public final class BonjourTouchOscHelper implements Runnable {
	private static final int REPEAT_ADVERTISE_PAUSE = 500;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(BonjourTouchOscHelper.class);

	private static final String OSC_SERVICE_NAME = "_osc._udp.";
	private static final String OSC_SERVICE_DOMAIN = "local.";
	private final JmDNS advertiser;
	private final JmDNS listener;

	private final Set<BonjourAdvertisedTouchOscInstance> instances;
	private boolean running = true;
	private final int port;

	private BonjourTouchOscHelper(final int stereoscopePort) throws Exception {
		this.port = stereoscopePort;
		this.advertiser = JmDNS.create();
		this.listener = JmDNS.create();
		this.instances = new HashSet<BonjourAdvertisedTouchOscInstance>();
		this.observers = new ArrayList<IObserveClients>();

		this.startAdvertisingStereoscopeService();
	}

	/**
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void startAdvertisingStereoscopeService()
			throws IOException {
		final ConcurrentHashMap<String, String> settings = new ConcurrentHashMap<String, String>();

		settings.put("Target", getLocalHostName());
		final ServiceInfo info = ServiceInfo.create(OSC_SERVICE_NAME + OSC_SERVICE_DOMAIN, "Stereoscope",
				this.port, 1, 1, settings);
		this.advertiser.registerService(info);
	}

	@Override
	public void run() {
		while (this.running) {
			final Map<String, ServiceInfo[]> infos = this.listener.listBySubtype(OSC_SERVICE_NAME + OSC_SERVICE_DOMAIN);
			if (infos != null) {
				for (final ServiceInfo[] info : infos.values()) {
					for (int i = 0; i < info.length; i++) {
						this.processAdvertisedBonjourService(info, i);
					}
				}
			}
			try {
				Thread.sleep(REPEAT_ADVERTISE_PAUSE);
			} catch (final InterruptedException e) {
				this.running = false;
			}
		}
	}

	/**
	 * @param infos
	 * @param i
	 */
	private void processAdvertisedBonjourService(final ServiceInfo[] infos, final int i) {
		if (!infos[i].getName().contains("Stereoscope")) {
			this.touchOscInstanceFoundInNetwork(infos, i);
		}
	}

	/**
	 * @param infos
	 * @param i
	 */
	private void touchOscInstanceFoundInNetwork(final ServiceInfo[] infos, final int i) {
		final BonjourAdvertisedTouchOscInstance myInstance = new BonjourAdvertisedTouchOscInstance(
				this, infos[i].getHostAddress(), infos[i].getPort(),
				infos[i].getName());
		if (!this.instances.contains(myInstance)) {
			LOG.info("Found iPad at: " + infos[i].getHostAddress() + ":"
					+ infos[i].getPort());
			this.instances.add(myInstance);
			this.fireClientDetected(myInstance);
		}
	}

	private final ArrayList<IObserveClients> observers;

	public void registerObserver(final IObserveClients observer) {
		this.observers.add(observer);
	}

	public void removeObserver(final IObserveClients ob) {
		this.observers.remove(ob);
	}

	private void fireClientDetected(final BonjourAdvertisedTouchOscInstance inst) {
		for (final IObserveClients ob : this.observers) {
			ob.surfaceDetected(inst);
		}
	}

	public void shutdown() {
		try {
			this.listener.close();
			this.advertiser.unregisterAllServices();
			this.advertiser.close();
			this.running = false;
		} catch (final IOException e) {
			LOG.log(Level.WARNING, "Error shutting down Bonjour", e);
		}
	}

	public Set<BonjourAdvertisedTouchOscInstance> getInstances() {
		return Collections.unmodifiableSet(this.instances);
	}

	private static Thread instanceThread;
	private static Object instanceLock = new Object();
	private static BonjourTouchOscHelper instance;

	public static BonjourTouchOscHelper getInstance(final int port)
			throws Exception {
		synchronized (instanceLock) {
			if (instance != null) {
				if (instance.port != port) {
					instance.shutdown();
					startNewInstance(port);
				}
			} else {
				startNewInstance(port);
			}
			return instance;
		}
	}

	public static BonjourTouchOscHelper getInstance()
			throws Exception {
		synchronized (instanceLock) {
			if (instance == null) {
				startNewInstance(OscConstants.DEFAULT_OSC_SERVER_PORT);
			}
			return instance;
		}
	}


	/**
	 * @param port
	 * @throws Exception
	 */
	private static void startNewInstance(final int port) throws Exception {
		instance = new BonjourTouchOscHelper(port);
		instanceThread = new Thread(instance, "TouchOSC Bonjour Advertiser");
		instanceThread.start();
	}

	private static String getLocalHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
}
