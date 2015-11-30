package com.stereokrauts.stereoscope.plugin.ui;

import java.util.List;

import org.osgi.framework.Bundle;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;
import com.stereokrauts.stereoscope.plugin.interfaces.Activator;

public final class MidiPortSelectionFactory {
	public static MidiPortSelection create(final AbstractApplicationContext ctx, final List<? extends Object> midiInputPorts, final List<? extends Object> midiOutputPorts) {
		if (haveSwtBundle()) {
			try {
				return (MidiPortSelection) Class.forName("com.stereokrauts.stereoscope.plugin.gui.EclipseMidiPortSelection").getConstructor(AbstractApplicationContext.class,
						List.class,
						List.class).newInstance(ctx, midiInputPorts, midiOutputPorts);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new MidiPortSelection() {
			@Override
			public MidiPortSelectionResult open() {
				return null;
			}

			@Override
			public MidiPortSelectionResult getResult() {
				return null;
			}
		};
	}

	private static boolean haveSwtBundle() {
		final Bundle[] bundles = Activator.getContext().getBundles();
		for (final Bundle b : bundles) {
			if (b.getSymbolicName().contains("eclipse.swt")) {
				return true;
			}
		}
		return false;
	}
}
