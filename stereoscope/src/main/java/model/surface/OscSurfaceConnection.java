package model.surface;

import java.io.IOException;
import java.util.logging.Level;

import model.beans.ConnectionServerTcpUdpIpBean;
import model.beans.ConnectionTcpUdpIpBean;
import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.bridge.oscp5.OscMessageRelayAdapter;
import model.protocol.osc.bridge.oscp5.OscP5Adapter;
import netP5.NetAddress;
import oscP5.OscP5;
import aspects.observer.IAspectedObserver;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class holds the connection information for the OSC peer.
 * @author theide
 *
 */
public final class OscSurfaceConnection implements IAspectedObserver {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(OscSurfaceConnection.class);

	private OscP5 oscP5;
	private NetAddress myRemoteLocation;
	private boolean isConnected = false;

	private final ConnectionTcpUdpIpBean client;
	private final ConnectionServerTcpUdpIpBean server;
	private final OscSurface oscSurface;

	public OscSurfaceConnection(final OscSurface oscSurface, final ConnectionTcpUdpIpBean client, final ConnectionServerTcpUdpIpBean server) {
		this.oscSurface = oscSurface;
		client.getObserverManager().addObserver(this);
		server.getObserverManager().addObserver(this);
		this.client = client;
		this.server = server;
	}

	public synchronized void connect() {
		if (this.oscP5 != null || myRemoteLocation != null) {
			disconnect();
		}
		if (this.client.getUseUdp()) {
			if (oscP5 == null && this.server != null && this.server.getPortNumber() != null) {
				this.oscP5 = new OscP5(new OscMessageRelayAdapter(this.oscSurface), this.server.getPortNumber());
			}
			if (this.client != null && this.client.getNetworkAddress() != null && this.client.getPortNumber() != null) {
				this.myRemoteLocation = new NetAddress(this.client.getNetworkAddress(), this.client.getPortNumber());
			}
		} else {
			if (this.oscP5 == null) {
				this.oscP5 = new OscP5(new OscMessageRelayAdapter(this.oscSurface), this.client.getNetworkAddress(), this.client.getPortNumber(), OscP5.TCP);
			} else {
				throw new IllegalArgumentException("Double call to connect()!");
			}
		}
		if (this.oscP5 != null && (!this.client.getUseUdp() || this.myRemoteLocation != null)) {
			this.isConnected = true;
		}
	}

	public synchronized void disconnect() {
		if (this.oscP5 != null) {
			if (this.myRemoteLocation != null) {
				this.oscP5.disconnect(this.myRemoteLocation);
			}
			if (this.oscP5.tcpServer() != null && this.oscP5.tcpServer().socket() != null) {
				try {
					this.oscP5.tcpServer().socket().close();
				} catch (final IOException e) {
					LOG.log(Level.WARNING, "Error stopping oscP5 TCP server", e);
				}
			}
			this.oscP5.stop();
			this.oscP5.dispose();
		}
		this.isConnected = false;
		this.oscP5 = null;
		this.myRemoteLocation = null;
	}

	public synchronized void shutdown() {
		this.disconnect();
	}

	public synchronized void sendMessage(final IOscMessage m) {
		if (this.isConnected) {
			this.logMessage(m);
			this.internalSendMessage(m);
			oscSurface.receive();
		} else {
			LOG.warning("TouchOscSurface: Lost message to surface: " + m);
		}
	}

	/**
	 * @param m
	 */
	private void internalSendMessage(final IOscMessage m) {
		if (this.client.getUseUdp()) {
			this.oscP5.send(OscP5Adapter.adaptMessage(m), this.myRemoteLocation);
		} else {
			this.oscP5.send(OscP5Adapter.adaptMessage(m));
		}
	}

	/**
	 * @param m
	 */
	private void logMessage(final IOscMessage m) {
		if (!OscAddressUtil.stringify(m.address()).contains("/system/heartbeat")) {
			if (this.client.getUseUdp()) {
				LOG.finest("sending udp osc message to" + this.myRemoteLocation.address() + ": " + " addrpattern: "
						+ OscAddressUtil.stringify(m.address()) + ", toString: " + m);
			} else {
				LOG.finest("sending tcp osc message: " + " addrpattern: " + OscAddressUtil.stringify(m.address()) + ", toString: " + m);
			}
		}
	}

	public OscP5 getOscP5() {
		return this.oscP5;
	}

	public NetAddress getMyRemoteLocation() {
		return this.myRemoteLocation;
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	public ConnectionTcpUdpIpBean getBean() {
		return this.client;
	}

	public ConnectionServerTcpUdpIpBean getServerBean() {
		return this.server;
	}

	@Override
	public void valueChangedEvent(final Object sender, final String fieldName, final Object oldValue, final Object newValue) {
		LOG.info("Bean value changed " + fieldName + " from " + oldValue + " to " + newValue);
		if (sender == this.client) {
			if (fieldName.equals("ip")) {
				this.setNewNetworkAddress(this.client.getNetworkAddress());
			} else if (fieldName.equals("portNumber")) {
				this.setNewOutgoingPort(this.client.getPortNumber());
			} else if (fieldName.equals("useUdp")) {
				this.disconnect();
				this.connect();
			}
		}
		if (sender == this.server) {
			if (fieldName.equals("portNumber")) {
				this.setNewIncomingPort(this.server.getPortNumber());
			} else {
				LOG.warning("The parameter " + fieldName + " is not yet supported!");
			}
		}
	}

	private synchronized void setNewNetworkAddress(final String newNetworkAddress) {
		try {
			LOG.log(Level.INFO, "Initializing new remote connection due to IP address change");
			this.myRemoteLocation = new NetAddress(newNetworkAddress, this.client.getPortNumber());
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Changing the IP address raised an error:", e);
		}
	}

	private synchronized void setNewIncomingPort(final int incomingPort) {
		LOG.log(Level.INFO, "Initializing new local connection due to port change - disconnecting...");
		this.disconnect();
		LOG.log(Level.INFO, "Initializing new local connection due to port change - reconnecting...");
		this.connect();
	}

	private synchronized void setNewOutgoingPort(final int outgoingPort) {
		LOG.log(Level.INFO, "Initializing new remote connection due to port change");
		this.myRemoteLocation = new NetAddress(this.client.getNetworkAddress(), outgoingPort);
	}

}