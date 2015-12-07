package model.persistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import model.beans.BusAttendeeBean;
import model.beans.BusBean;
import model.beans.ClientBean;
import model.beans.ConnectionMidiBean;
import model.beans.ConnectionServerTcpUdpIpBean;
import model.beans.ConnectionTcpUdpIpBean;
import model.beans.DocumentBean;
import model.beans.MixerBean;
import model.beans.MixerConnectionBean;
import model.beans.MixerPluginBean;
import model.beans.OscSurfaceBean;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class DocumentSaverTest {

	@Test
	public void test() throws Exception {
		final DocumentBean doc = this.stubDocumentBean();
		assertNotNull(doc);
		
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DocumentSaver.save(doc, stream);
		final String outputXml = new String(stream.toByteArray());
		
		assertTrue(outputXml.contains("MIDI-INPUT"));
		assertTrue(outputXml.contains("MIDI-OUTPUT"));
		assertTrue(outputXml.contains("com.stereokrauts.stereoscope.mixer.yamaha.m7cl"));
		assertTrue(outputXml.contains("127.0.0.1"));
		assertTrue(outputXml.contains("Test document"));
		
		final DocumentBean d = DocumentLoader.load(IOUtils.toInputStream(outputXml));
		
		assertEquals(doc.getName(), d.getName());
		assertEquals(doc.getBussesList().get(0).getName(), d.getBussesList().get(0).getName());
		assertEquals(doc.getBussesList().size(), d.getBussesList().size());
		assertEquals(doc.getBussesList().get(0).getAttendeesList().size(), d.getBussesList().get(0).getAttendeesList().size());
		assertEquals(doc.getBussesList().get(0).getAttendeesList().get(0).getClass(), d.getBussesList().get(0).getAttendeesList().get(0).getClass());
	}

	private DocumentBean stubDocumentBean() {
		final ArrayList<BusAttendeeBean> attendees = new ArrayList<BusAttendeeBean>();
		final MixerBean attendee1 = new MixerBean();
		
		final MixerPluginBean mixerPluginBean = new MixerPluginBean();
		mixerPluginBean.setPluginId("com.stereokrauts.stereoscope.mixer.yamaha.m7cl");
		attendee1.setPlugin(mixerPluginBean);
		
		final MixerConnectionBean connection = new MixerConnectionBean();
		final ConnectionMidiBean midiConnection = new ConnectionMidiBean();
		midiConnection.setInputPortName("MIDI-INPUT");
		midiConnection.setOutputPortName("MIDI-OUTPUT");
		connection.setMidiConnection(midiConnection );
		attendee1.setConnection(connection );
		
		final OscSurfaceBean attendee2 = new OscSurfaceBean();
		ClientBean clientConfig = new ClientBean();
		attendee2.setClientConfig(clientConfig);
		clientConfig.setAccessibleAux(7);
		clientConfig.setRestrictedClient(true);
		clientConfig.setSnapFaders(true);
		final ConnectionServerTcpUdpIpBean server = new ConnectionServerTcpUdpIpBean();
		server.setPortNumber(500);
		attendee2.setLocalConnParams(server);
		final ConnectionTcpUdpIpBean surfaceConnection = new ConnectionTcpUdpIpBean();
		surfaceConnection.setNetworkAddress("127.0.0.1");
		surfaceConnection.setPortNumber(6000);
		surfaceConnection.setUseUdp(true);
		attendee2.setRemoteConnParams(surfaceConnection );
		
		attendees.add(attendee1);
		attendees.add(attendee2);
		
		final BusBean bus = new BusBean();
		bus.setAttendeeList(attendees);
		bus.setName("my bus");
		
		final DocumentBean doc = new DocumentBean();
		doc.setName("Test document");
		final ArrayList<BusBean> busses = new ArrayList<BusBean>();
		busses.add(bus);
		doc.setBusList(busses);
		
		return doc;
	}

}
