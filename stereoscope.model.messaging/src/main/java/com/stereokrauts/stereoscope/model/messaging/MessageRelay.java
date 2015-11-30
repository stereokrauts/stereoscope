package com.stereokrauts.stereoscope.model.messaging;

import java.util.ArrayList;
import java.util.HashMap;


import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/**
 * This class dispatches messages to certain handlers. A class
 * can register for messages that are of a certain type that inherits
 * from AbstractMessage<?> and will be called whenever a message
 * of that certain class is being dispatched through this class.
 * 
 * The class take care of class hiearchies, so a message handler that
 * listens for all AbstractMessage messages will be called for a
 * given message, if no more specific handler is found before.
 * @author theide
 *
 */
public final class MessageRelay {
	private final SLogger logger;
	
	private final HashMap<Class<? extends IMessage>, ArrayList<IMessageHandler>> handlerAssociation;
	
	public MessageRelay(final String name) {
		this.logger = StereoscopeLogManager.getLogger("message-relay-" + name.toLowerCase());
		this.handlerAssociation = new HashMap<Class<? extends IMessage>, ArrayList<IMessageHandler>>();
	}
	
	public void registerMessageHandler(final Class<? extends IMessage> class1, final IMessageHandler handler) {
		if (!this.haveHandlerForClass(class1)) {
			this.initializeHandlerForClass(class1);
		}
		this.addHandlerForClass(class1, handler);
		this.logger.info("Registered Message-Handler " + handler);
	}

	public void unregisterMessageHandler(final IMessageHandler handler) {
		for (final Class<? extends IMessage> clz : this.handlerAssociation.keySet()) {
			this.handlerAssociation.get(clz).remove(handler);
		}
		this.logger.info("Unregistered Message-Handler " + handler);
	}

	public boolean handleMessage(final IMessage msg) {
		return this.handleMessageWalkUpClassHierarchy(0, msg);
	}

	private boolean handleMessageWalkUpClassHierarchy(final int i, final IMessage msg) {
		final Class<?> lookup = this.retrieveNthParentClass(i, msg);
		if (lookup != null) {
			if (this.haveHandlerForClass(lookup)) {
				for (final IMessageHandler hnd : this.getHandlerForClass(lookup)) {
					hnd.handleMessage(msg);
				}
				return true;
			} else {
				return this.handleMessageWalkUpClassHierarchy(i + 1, msg);
			}
		}
		return false;
	}

	/**
	 * @param lookup
	 * @return
	 */
	private ArrayList<IMessageHandler> getHandlerForClass(final Class<?> lookup) {
		return this.handlerAssociation.get(lookup);
	}

	/**
	 * @param lookup
	 * @return
	 */
	private boolean haveHandlerForClass(final Class<?> lookup) {
		return this.handlerAssociation.containsKey(lookup);
	}
	
	/**
	 * @param class1
	 * @param handler
	 */
	private void addHandlerForClass(final Class<? extends IMessage> class1,
			final IMessageHandler handler) {
		this.handlerAssociation.get(class1).add(handler);
	}


	private void initializeHandlerForClass(final Class<? extends IMessage> class1) {
		this.handlerAssociation.put(class1, new ArrayList<IMessageHandler>());
	}

	/**
	 * @param hierarchyLimit The number of parent classes to walk up
	 * @param msg The object of which to find the parent class
	 * @return The nth parent class of the given object
	 */
	private Class<?> retrieveNthParentClass(final int hierarchyLimit, final IMessage msg) {
		Class<?> lookup = msg.getClass();
		for (int j = 0; j < hierarchyLimit; j++) {
			lookup = lookup.getSuperclass();
			if (!AbstractMessage.class.isAssignableFrom(lookup)) {
				return null;
			}
		}
		return lookup;
	}
}
