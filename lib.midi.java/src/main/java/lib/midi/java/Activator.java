package lib.midi.java;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		if (isMac()) {
			bundleContext.getBundle().stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
