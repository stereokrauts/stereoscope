package model.mixer.interfaces;

import com.stereokrauts.lib.midi.api.IReactToMidi;

/**
 * This class must be present in any plugin, as it will
 * receive all MIDI data that is being received from the
 * mixer.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public abstract class IUnderstandMixer implements IReactToMidi {
	/**
	 * The default constructor requires a IAmMixer parameter,
	 * because the implementing class will most probably want
	 * to inform the program intern object representing the
	 * mixer of status changes which happen at the real
	 * mixer.
	 * @param partner The corresponding IAmMixer object that
	 * will receive status updates.
	 */
	public IUnderstandMixer(final IAmMixer partner) {
	}
}
