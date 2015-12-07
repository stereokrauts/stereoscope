package tests.simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * This class generates test data which can be used in a simulation run
 * for example with the OscSurfaceChangeSimulation.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public class SimulationDataGenerator {
	/* how many test data items to write into the simulation data file */
	private static final int HOWMANY_MESSAGES = 100000;
	
	/* mixer configuration */
	private static final int HOWMANY_GEQS = 8;
	private static final int HOWMANY_GEQ_BANDS = 31;
	private static final int HOWMANY_AUXS = 24;
	private static final int HOWMANY_CHANNELS = 72;
	
	private static BufferedWriter simulationDataStream;
	private static Random rand;

	public static void main(final String[] args) {
		final File simulationData = new File("simulation.txt");
		try {
			rand = new Random(System.currentTimeMillis());
			simulationDataStream = new BufferedWriter(new FileWriter(simulationData));
			generateSimulationData();
			simulationDataStream.close();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void generateSimulationData() throws Exception {
		for (int i = 0; i < HOWMANY_MESSAGES; i++) {
			final int messageType = rand.nextInt(SimulationMessage.MESSAGE_TYPES_COUNT);
			switch (messageType) {
			case SimulationMessage.MESSAGE_TYPE_CHLEVEL:
				createMessageChLevel();
				break;
			case SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL:
				createMessageChAuxLevel();
				break;
			case SimulationMessage.MESSAGE_TYPE_MASTER:
				createMessageMaster();
				break;
			case SimulationMessage.MESSAGE_TYPE_AUXMASTER:
				createMessageAuxMaster();
				break;
			case SimulationMessage.MESSAGE_TYPE_CHONBUTTON:
				createMessageChOnButton();
				break;
			case SimulationMessage.MESSAGE_TYPE_GEQBAND:
				createMessageGeqBand();
				break;
			case SimulationMessage.MESSAGE_TYPE_GEQBANDRESET:
				createMessageGeqBandReset();
				break;
			case SimulationMessage.MESSAGE_TYPE_GEQFULLRESET:
				createMessageGeqFullReset();
				break;
			default:
				throw new RuntimeException("Oops, there is an error in the definition of message types.");
			}
		}
	}

	private static void createMessageGeqFullReset() throws IOException {
		final int geqNumber = rand.nextInt(HOWMANY_GEQS);
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_GEQFULLRESET + ";" + geqNumber + "\n");
	}

	private static void createMessageGeqBandReset() throws IOException {
		final int geqNumber = rand.nextInt(HOWMANY_GEQS);
		final int leftRight = rand.nextInt(2);
		final int bandNumber = rand.nextInt(HOWMANY_GEQ_BANDS);
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_GEQBANDRESET + ";" + geqNumber + ";" + leftRight + ";" + bandNumber + "\n");
	}

	private static void createMessageGeqBand() throws IOException {
		final int geqNumber = rand.nextInt(HOWMANY_GEQS);
		final int leftRight = rand.nextInt(2);
		final int bandNumber = rand.nextInt(HOWMANY_GEQ_BANDS);
		final float newLevel = rand.nextFloat() * 2.0f - 1.0f;
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_GEQBAND + ";" + geqNumber + ";" + leftRight + ";" + bandNumber + ";" + newLevel + "\n");
	}

	private static void createMessageChOnButton() throws IOException {
		final int chNumber = rand.nextInt(HOWMANY_GEQS);
		final int onOff = rand.nextInt(2);
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_CHONBUTTON + ";" + chNumber + ";" + onOff + "\n");
	}

	private static void createMessageAuxMaster() throws IOException {
		final int auxNumber = rand.nextInt(HOWMANY_AUXS);
		final float level = rand.nextFloat();
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_AUXMASTER + ";" + auxNumber + ";" + level + "\n");
	}

	private static void createMessageMaster() throws IOException {
		final float level = rand.nextFloat();
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_MASTER + ";" + level + "\n");
	}

	private static void createMessageChAuxLevel() throws IOException {
		final int auxNumber = rand.nextInt(HOWMANY_AUXS);
		final int chNumber = rand.nextInt(HOWMANY_CHANNELS);
		final float level = rand.nextFloat();
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL + ";" + auxNumber + ";" + chNumber + ";" + level + "\n");
	}

	private static void createMessageChLevel() throws IOException {
		final int chNumber = rand.nextInt(HOWMANY_CHANNELS);
		final float level = rand.nextFloat();
		simulationDataStream.write(SimulationMessage.MESSAGE_TYPE_CHLEVEL + ";" + chNumber + ";" + level + "\n");
	}
}
