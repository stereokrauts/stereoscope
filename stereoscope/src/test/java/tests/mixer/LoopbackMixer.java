package tests.mixer;

import java.awt.image.BufferedImage;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;

/**
 * This class simulates a mixer, which simply sends back all values that
 * are being sent to it (immediately!).
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public class LoopbackMixer extends IAmMixer implements IProvideChannelNames,
IMixerWithGraphicalEq, IMessageReceiver {
	IMessageReceiver myObserver = null;


	@Override
	public void registerObserver(final IMessageReceiver observer) {
		this.myObserver = observer;
	}


	@Override
	public int getChannelCount() {
		/* this is a loopback device, ignore requests */
		return 32;
	}


	@Override
	public int getAuxCount() {
		/* this is a loopback device, spoof response */
		return 32;
	}

	@Override
	public int getGeqCount() {
		/* this is a loopback device, spoof response */
		return 32;
	}


	@Override
	public int getBusCount() {
		/* this is a loopback device, spoof response */
		return 32;
	}


	@Override
	public int getOutputCount() {
		/* this is a loopback device, spoof response */
		return 32;
	}

	@Override
	public void getGeqBandLevel(final int eqNumber, final boolean rightChannel, final int band) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllGeqLevels(final byte eqNumber) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getChannelNames() {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAuxMaster(final int aux) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getChannelLevel(final int channel) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getChannelOnButton(final int channel) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllAuxLevels(final int aux) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllAuxChannelOn(final int aux) {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllChannelLevels() {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllChannelOnButtons() {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getAllDelayTimes() {
	/* this is a loopback device, ignore requests */	}

	@Override
	public void getOutputMaster() { 
	/*rj: added this but don't know for what usecase. */	}

	public BufferedImage getSmallImage() {
		return null;
	}


	@Override
	public void handleNotification(final IMessageWithSender message) {
		this.myObserver.handleNotification(message);
	}


	@Override
	public void isFlexEQ(final short eqNumber) {
		/* ignore */
	}


	@Override
	public void getAllInputValues(final int inputChn) {
		// TODO rjansen Auto-generated method stub

	}


	@Override
	public int getMatrixCount() {
		// TODO rjansen Auto-generated method stub
		return 0;
	}


	@Override
	public void getAllGroupsStatus() {

	}


	@Override
	public void getBusMaster(final int bus) {

	}


	@Override
	public void getAllBusStatus() {

	}


	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return null;
	}

}
