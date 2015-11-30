package com.stereokrauts.stereoscope.webgui;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.vertx.java.core.Handler;

import com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.HttpServerRequestHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.SockJSHandler;

public class WebFrontendInitializer {
	private SockJSHandler sockJSHandler;
	private HttpServerRequestHandler httpRequestHandler;

	public WebFrontendInitializer(final Webclient webclient, final int serverPort) {
		final BundleContext context = Activator.getContext();
		final String portNumber = Integer.toString(serverPort);
		registerHttpHandler(context, portNumber);
		registerSockJS(context, portNumber, webclient);
	}

	private void registerHttpHandler(final BundleContext context, final String port) {
		httpRequestHandler = new HttpServerRequestHandler();
		final Dictionary<String, String> properties = new Hashtable<>();
		properties.put("type", "HttpServerRequestHandler");
		properties.put("port", port);
		properties.put("protocol", "http,sockjs");
		context.registerService(Handler.class, httpRequestHandler, properties);
	}

	private void registerSockJS(final BundleContext context, final String port, final Webclient webclient) {
		sockJSHandler = new SockJSHandler(webclient);
		final Dictionary<String, String> properties = new Hashtable<>();
		properties.put("type", "SockJSHandler");
		properties.put("port", port);
		properties.put("prefix", "/stereoscope");
		context.registerService(Handler.class, sockJSHandler, properties);
	}

	public SockJSHandler getSockJSHandler() {
		return sockJSHandler;
	}

	public HttpServerRequestHandler getHttpRequestHandler() {
		return httpRequestHandler;
	}
}
