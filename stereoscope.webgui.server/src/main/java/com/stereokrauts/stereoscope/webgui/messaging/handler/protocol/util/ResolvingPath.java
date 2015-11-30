package com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.HttpServerRequestHandler;

public final class ResolvingPath extends PathUnimpl {

    /**
	 * 
	 */
	private final HttpServerRequestHandler httpServerRequestHandler;

	/**
	 * @param httpServerRequestHandler
	 */
	public ResolvingPath(HttpServerRequestHandler httpServerRequestHandler) {
		this.httpServerRequestHandler = httpServerRequestHandler;
	}

	@Override
    public Path resolve(Path other) {
    	if (other != null) {
            String resource = other.toString();
            BundleContext bundleContext = FrameworkUtil.getBundle(HttpServerRequestHandler.class).getBundleContext();
            if (resource != null) {
            	try {
            		String location = FileLocator.getBundleFile(bundleContext.getBundle()).getAbsolutePath();
            		Path resourcePath = Paths.get(location, "/", resource);
            		return resourcePath;
            	} catch (IOException e1) {
            		e1.printStackTrace();
            	}
            } else {
                return this.httpServerRequestHandler.createInvalidPath(other);
            }
        }
        return null;
    }


}