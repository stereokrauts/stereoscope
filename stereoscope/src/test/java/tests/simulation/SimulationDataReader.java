package tests.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This class turns the simulation data which was generated by the
 * SimulationDataGenerator into a computer readable form to run
 * a real test.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public class SimulationDataReader {
	private final BufferedReader reader;

	public SimulationDataReader(final String filename) throws Exception {
		final File data = new File(filename);
		this.reader = new BufferedReader(new FileReader(data));
	}
	
	public SimulationMessage getNextMessage() throws Exception {
		String rawMessage;
		try {
			rawMessage = this.reader.readLine();
		} catch (final IOException e) {
			/* EOF :-) */
			return null;
		}
		if (rawMessage == null) {
			return null;
		}
		final StringTokenizer st = new StringTokenizer(rawMessage, ";");
		final int type = Integer.parseInt(st.nextToken());
		
		final SimulationMessage rv = new SimulationMessage();
		rv.setMessageType(type);
		
		int ch;
		int aux;
		float level;
		int geqNumber;
		int geqBand;
		boolean isRight;
		boolean isChannelOn;
		
		switch (type) {
		case SimulationMessage.MESSAGE_TYPE_CHLEVEL:
			ch = Integer.parseInt(st.nextToken());
			level = Float.parseFloat(st.nextToken());
			rv.setChannelNumber(ch);
			rv.setLevel(level);
			break;
		case SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL:
			aux = Integer.parseInt(st.nextToken());
			ch = Integer.parseInt(st.nextToken());
			level = Float.parseFloat(st.nextToken());
			rv.setAuxNumber(aux);
			rv.setChannelNumber(ch);
			rv.setLevel(level);
			break;
		case SimulationMessage.MESSAGE_TYPE_MASTER:
			level = Float.parseFloat(st.nextToken());
			rv.setLevel(level);
			break;
		case SimulationMessage.MESSAGE_TYPE_AUXMASTER:
			aux = Integer.parseInt(st.nextToken());
			level = Float.parseFloat(st.nextToken());
			rv.setAuxNumber(aux);
			rv.setLevel(level);
			break;
		case SimulationMessage.MESSAGE_TYPE_CHONBUTTON:
			ch = Integer.parseInt(st.nextToken());
			isChannelOn = (Integer.parseInt(st.nextToken()) == 1) ? true : false;
			rv.setChannelNumber(ch);
			rv.setChannelOn(isChannelOn);
			break;
		case SimulationMessage.MESSAGE_TYPE_GEQBAND:
			geqNumber = Integer.parseInt(st.nextToken());
			isRight = (Integer.parseInt(st.nextToken()) == 1) ? true : false;
			geqBand = Integer.parseInt(st.nextToken());
			level = Float.parseFloat(st.nextToken());
			rv.setGeqNumber(geqNumber);
			rv.setRightGeq(isRight);
			rv.setBandNumber(geqBand);
			rv.setLevel(level);
			break;
		case SimulationMessage.MESSAGE_TYPE_GEQFULLRESET:
			geqNumber = Integer.parseInt(st.nextToken());
			rv.setGeqNumber(geqNumber);
			break;
		case SimulationMessage.MESSAGE_TYPE_GEQBANDRESET:
			geqNumber = Integer.parseInt(st.nextToken());
			geqBand = Integer.parseInt(st.nextToken());
			rv.setGeqNumber(geqNumber);
			rv.setBandNumber(geqBand);
			break;
		default:
			throw new RuntimeException("Oops, there is an error in the simulation data (unknown message type: " + type + ".");
		}
		
		return rv;
	}
}