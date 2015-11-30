package model.protocol.midi.passthrough;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.spi.MidiDeviceProvider;

import stereoscope.licensing.ProductInformation;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class manages the ThruMidiDevice.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class ThruMidiDeviceProvider extends MidiDeviceProvider {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(ThruMidiDeviceProvider.class);

	/**
	 * This is the info object with which the MidiThruDevice is created.
	 */
	public static final MidiDevice.Info INFO = new MidiDevice.Info(
			"stereOSCope Passthrough", 
			"Theta Tontechnik", 
			"This port provides access to the MIDI device to which stereOSCope"
					+ " is currently bound to.",
					ProductInformation.PRODUCT_VERSION) { };

					/**
					 * The singleton instance of the ThruMidiDevice. (Singleton is
					 * not show to the outside world, but current implementation does
					 * enforce it)
					 */
					private ThruMidiDevice instance = null;;


					@Override
					public Info[] getDeviceInfo() {
						LOG.info("ThruMidiDeviceProvider - getDeviceInfo!");
						return new MidiDevice.Info[] { INFO };
					}


					@Override
					public MidiDevice getDevice(final Info info) {
						LOG.info("ThruMidiDeviceProvider - getDevice!");

						if (info == INFO) {
							LOG.info("ThruMidiDeviceProvider - getDevice "
									+ "- returning instance.");
							if (this.instance == null) {
								this.instance = new ThruMidiDevice(info);
							}
							return this.instance;
						}
						throw new IllegalArgumentException();
					}

}
