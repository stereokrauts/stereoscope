package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;

import java.util.Arrays;
import java.util.List;

import com.stereokrauts.lib.midi.api.IMidiInputPort;
import com.stereokrauts.lib.midi.api.IMidiOutputPort;
import com.stereokrauts.lib.midi.api.MidiException;
import com.stereokrauts.lib.midi.api.MidiHelper;
import com.stereokrauts.plugin.midibridge.Midi;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;
import com.stereokrauts.stereoscope.plugin.model.UserRequestedAbortException;
import com.stereokrauts.stereoscope.plugin.ui.MidiPortSelectionFactory;
import com.stereokrauts.stereoscope.plugin.ui.MidiPortSelectionResult;

/**
 * Builder for the DDX3216 plugin.
 * @author th
 *
 */
public final class DDX3216PluginBuilder implements IMixerPluginBuilder {

	@Override
	public IMixerPlugin buildPlugin(final AbstractApplicationContext ctx) throws UserRequestedAbortException, MidiException {
		final List<IMidiInputPort> inputs = Arrays.asList(Midi.getMidiSubsystem().getInputPorts());
		final List<IMidiOutputPort> outputs  = Arrays.asList(Midi.getMidiSubsystem().getOutputPorts());
		final MidiPortSelectionResult result = (MidiPortSelectionFactory.create(ctx, inputs, outputs)).open();
		if (result.isClosedUsingOkay()) {
			return new DDX3216Plugin(ctx, (IMidiInputPort) result.getMidiInputPort(), (IMidiOutputPort) result.getMidiOutputPort());
		} else {
			throw new UserRequestedAbortException();
		}
	}

	@Override
	public IMixerPlugin buildPlugin(final AbstractApplicationContext ctx, final IPersistentPluginConfiguration configuration) throws MidiException {
		final List<IMidiInputPort> inputs = Arrays.asList(Midi.getMidiSubsystem().getInputPorts());
		final List<IMidiOutputPort> outputs  = Arrays.asList(Midi.getMidiSubsystem().getOutputPorts());
		final IMidiInputPort input = MidiHelper.getPortFromList(inputs, configuration.getConnectionBean().getMidiConnection().getInputPortName());
		final IMidiOutputPort output = MidiHelper.getPortFromList(outputs, configuration.getConnectionBean().getMidiConnection().getOutputPortName());
		return new DDX3216Plugin(ctx, input, output);
	}

}
