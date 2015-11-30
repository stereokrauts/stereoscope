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
import java.util.HashSet;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.sockjs.SockJSSocket;

import com.stereokrauts.stereoscope.webgui.JsonConverter;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public final class SockJSHandler implements Handler<SockJSSocket> {
	private SockJSSocket socket;
	private final Webclient frontend; // = Webclient.getInstance(this);
	private final JsonConverter json;
	private final Set<String> connectedClients = new HashSet<>();

	public SockJSHandler(final Webclient frontend) {
		this.frontend = frontend;
		json = new JsonConverter();
	}

	@Override
	public void handle(final SockJSSocket sock) {
		socket = sock;
		if (connectedClients.size() > 0) {
			sock.close();
			throw new IllegalStateException("Only one client is allowed per web-socket connection!");
		}
		connectedClients.add(sock.writeHandlerID);

		sock.dataHandler(new Handler<Buffer>() {
			@Override
			public void handle(final Buffer data) {
				final String jsonMsg = data.toString();
				FrontendMessage msg;
				try {
					if (dataIsJson(jsonMsg)) {
						msg = json.convertFromJson(jsonMsg);
						frontend.getToBusRelay().handleEvent(msg);
					} else {
						// this is where tosc layout upload should be handled
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});
		sock.endHandler(new Handler<Void>() {
			@Override
			public void handle(final Void arg0) {
				connectedClients.remove(sock.writeHandlerID);
			}
		});
	}

	public SockJSSocket getSocket() {
		return socket;
	}

	private Boolean dataIsJson(final String jsonMsg) {
		if (jsonMsg.substring(0, 1).matches("\\{")
				&& jsonMsg.substring(jsonMsg.length() - 1).matches("\\}")) {
			return true;
		} else {
			return false;
		}
	}

}
