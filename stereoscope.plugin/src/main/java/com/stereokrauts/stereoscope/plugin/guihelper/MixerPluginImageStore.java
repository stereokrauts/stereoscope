package com.stereokrauts.stereoscope.plugin.guihelper;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This class stores associations between all mixer plugins
 * and their provided images.
 * @author th
 *
 */
public final class MixerPluginImageStore extends Observable {
	private static MixerPluginImageStore instance;
	
	private final ArrayList<MixerPluginImageAssociation> associations = new ArrayList<MixerPluginImageAssociation>();
	
	private MixerPluginImageStore() { }
	
	public void putAssociation(final MixerPluginImageAssociation a) {
		this.associations.add(a);
		this.setChanged();
		this.notifyObservers(a);
	}
	
	public void removeAssociations(final String pluginId) {
		for (final MixerPluginImageAssociation a : this.associations) {
			if (a.getPluginId().equals(pluginId)) {
				this.associations.remove(a);
				this.setChanged();
			}
		}
		this.notifyObservers();
	}
	
	public Iterable<MixerPluginImageAssociation> getAllEntries() {
		return this.associations;
	}
	
	public static MixerPluginImageStore getInstance() {
		if (instance == null) {
			instance = new MixerPluginImageStore();
		}
		return instance;
	}

	public MixerPluginImageAssociation getEntryFor(final String name) throws MixerPluginDidNotProvideImageException {
		for (final MixerPluginImageAssociation a : this.associations) {
			if (a.getPluginId().equals(name)) {
				return a;
			}
		}
		throw new MixerPluginDidNotProvideImageException();
	}
	
}
