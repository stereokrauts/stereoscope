package com.stereokrauts.stereoscope.plugin.interfaces;



/**
 * This class is provided to each plugin at initialization phase
 * and can be used by the plugin to access specific application
 * functions.
 * 
 * @author Tobias Heide <tobi@s-hei.de>
 */
public abstract class AbstractApplicationContext {
	public abstract Object getViewContext();
}
