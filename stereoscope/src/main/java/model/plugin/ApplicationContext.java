package model.plugin;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

/**
 * This class is the only thing a application receives from the core system
 * and should be a central place from where the plugin can reach other functions
 * of the application.
 * @author theide
 *
 */
public final class ApplicationContext extends AbstractApplicationContext {
	private Object viewContext;

	@Override
	public Object getViewContext() {
		return viewContext;
	}

	public void setViewContext(final Object viewContext) {
		this.viewContext = viewContext;
	}
}
