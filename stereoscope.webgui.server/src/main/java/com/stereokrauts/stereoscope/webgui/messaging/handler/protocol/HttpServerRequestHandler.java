/*
 * Copyright 2012 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stereokrauts.stereoscope.webgui.messaging.handler.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.impl.Context;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.webgui.FileHelper;
import com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.util.ResolvingPath;



public final class HttpServerRequestHandler implements Handler<HttpServerRequest> {
	public static final SLogger LOG = StereoscopeLogManager
			.getLogger("frontend-msg-handling");
	FileHelper fileHelper = new FileHelper();
	
    public Path createInvalidPath(Path other) {
        URL resource = this.getClass().getClassLoader().getResource(".");
    	//URL resource = this.getClass().getClassLoader().getResource("index.html");
        if (resource != null) {
            try {
                URI uri = resource.toURI();
                return Paths.get(uri + other.toString());
            } catch (URISyntaxException e) {
            	LOG.error("HTTP request handler: Desired resource not found.");
            }
        }
        // If all else fails, return null, but beware that this can cause PathAdjuster to NPE.
        return null;
    }

    public void handle(HttpServerRequest request) {
        String path = null;
        String basePath = getBasePath("stereoscope.webgui.client");
    	setPathAdjuster();
        if ("/".equals(request.path)) {
        	path = basePath + "/index.html";
        } else {
        	path = basePath + request.path;
        }
      	if (path.endsWith(".js")) {
      		setContentType(request, "text/javascript");
      	} else if (path.endsWith(".css")) {
      		setContentType(request, "text/css");
      	} else if (path.endsWith(".xml")) {
    		setContentType(request, "text/xml");
    	} else if (path.endsWith(".svg")) {
    		setContentType(request, "image/svg+xml");
    	}
        request.response.setChunked(true);
        try {
        	Buffer buffer = new Buffer(this.getOsgiFileResource("stereoscope.webgui.client", path));
        	request.response.end(buffer);
		} catch (IOException e) {
			LOG.error("HTTP request handler: resource not found: stereoscope.webgui.client" + path + ". Read stacktrace for details.");
			String content = "<h1>HTML file not found</h1><p>See error log for more details.</p>";
			request.response.end(content);
			e.printStackTrace();
		}
    }

	private void setContentType(HttpServerRequest request, String contentType) {
		request.response.putHeader("Content-Type", contentType);
	}
    
    private void setPathAdjuster() {
        Context vertxContext = Context.getContext();
        if (vertxContext.getPathAdjustment() == null) {
            vertxContext.setPathAdjustment(new ResolvingPath(this));
        }
    }
    
    private byte[] getOsgiFileResource(final String bundleId, String path) throws IOException {
    	path = fileHelper.removeLeadingSlash(path);
      	Bundle bundle = Platform.getBundle(bundleId);
    	URL url = bundle.getEntry(path);
    	if (url == null) {
    		throw new IOException(String.format("Could not find resource %s in bundle %s.", path, bundleId));
    	}
    	return this.getUrlContent(url);
    }
    
    private String getBasePath(final String bundleId) {
    	String path = "/src/main/webapp";
    	Bundle bundle = Platform.getBundle(bundleId);
    	URL url = bundle.getEntry(path);
    	if (url == null) {
    		path = "/bin";
    	}
    	return path;
    }
    
  	private byte[] getUrlContent(final URL url) throws IOException {
    	InputStream buffer = url.openStream();
    	byte[] content = IOUtils.toByteArray(buffer);
    	return content;
    }
    
    
    
}