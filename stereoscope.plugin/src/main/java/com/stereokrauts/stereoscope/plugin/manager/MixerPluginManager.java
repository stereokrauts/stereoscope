package com.stereokrauts.stereoscope.plugin.manager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.Bundle;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.plugin.Activator;
import com.stereokrauts.stereoscope.plugin.generated.mixerPlugin.MixerPlugin;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageStore;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;

public final class MixerPluginManager {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(MixerPluginManager.class);

	private static MixerPluginManager instance;

	private final Map<String, String> displayNameBundleNameAssoc = new HashMap<String, String>();
	private final Map<String, String> bundleNameDescriptorAssoc = new HashMap<String, String>();
	private final Map<String, Bundle> bundleNameBundleAssoc = new HashMap<String, Bundle>();
	private final ArrayList<String> bundlesWithImages = new ArrayList<String>();

	private MixerPluginManager() {
		this.evaluateMixerPluginBundles();
	}

	private void evaluateMixerPluginBundles() {
		for (final Bundle bundle : Activator.getContext().getBundles()) {
			final String displayName = (String) bundle.getHeaders().get("Bundle-Name");
			final String bundleName = bundle.getSymbolicName();
			final Enumeration<URL> stscMixerPluginDescriptor = bundle.findEntries("META-INF", "stereoscope.plugin.mixer.xml", false);
			if (stscMixerPluginDescriptor != null && stscMixerPluginDescriptor.hasMoreElements()) {
				// Is a mixer plugin
				this.displayNameBundleNameAssoc.put(displayName, bundleName);
				this.bundleNameBundleAssoc.put(bundleName, bundle);
				try {
					this.bundleNameDescriptorAssoc.put(bundleName, this.getDescriptorClassFrom(stscMixerPluginDescriptor.nextElement()));
					LOG.info("Found a mixer plugin: " + bundleName + " (" + displayName + ")");
					final Enumeration<URL> image = bundle.findEntries("resources", "stereoscope.plugin.mixer.png", false);
					if (image != null && image.hasMoreElements()) {
						LOG.finest("Mixer plugin does provide it's own image");
						this.bundlesWithImages.add(bundleName);
						this.executeExtension(bundleName, displayName, image.nextElement());
					}
				} catch (final JAXBException e) {
					LOG.log(Level.WARNING, "Could not load plugin descriptor", e);
				}
			}
		}
	}

	private String getDescriptorClassFrom(final URL nextElement) throws JAXBException {
		final JAXBContext ctx = JAXBContext.newInstance(MixerPlugin.class);
		final Unmarshaller unmarshaller = ctx.createUnmarshaller();
		final MixerPlugin descriptor = (MixerPlugin) unmarshaller.unmarshal(nextElement);
		return descriptor.getDescriptorClass();
	}

	public AbstractMixerPluginDescriptor getDescriptorClassFor(final String pluginId) throws Exception {
		if (this.bundleNameDescriptorAssoc.containsKey(pluginId)) {
			// Get package admin service.
			final Bundle bnd = this.bundleNameBundleAssoc.get(pluginId);
			if (bnd != null) {
				final Object returnValue = bnd.loadClass(this.bundleNameDescriptorAssoc.get(pluginId)).newInstance();
				if (returnValue instanceof AbstractMixerPluginDescriptor) {
					return (AbstractMixerPluginDescriptor) returnValue;
				}
				throw new Exception("PluginDescriptor for " + pluginId + " does not implement correct interfaces!");
			}
		}
		throw new Exception("Could not find plugin descriptor for " + pluginId);
	}

	private void executeExtension(final String bundleName, final String bundleDisplayName, final URL imgData) {
		MixerPluginImageStore.getInstance().putAssociation(new MixerPluginImageAssociation(bundleName, bundleDisplayName, imgData));
	}

	public static MixerPluginManager getInstance() {
		if (instance == null) {
			instance = new MixerPluginManager();
		}
		return instance;
	}

}
