package com.stereokrauts.stereoscope.gui.commands;

import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Opens a link in the system webbrowser.
 * @author th
 *
 */
public final class OpenLinkInBrowserHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final String uriString = event.getParameter("stereoscope.gui.commands.parameter.uri");
		this.openBrowserWindow(uriString);
		return null;
	}

	private void openBrowserWindow(final String uriString) {
		final java.awt.Desktop osDesktop = java.awt.Desktop.getDesktop();
		if (osDesktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
			try {
				osDesktop.browse(URI.create(uriString));
			} catch (final IOException e) {
				JOptionPane.showMessageDialog(null, 
						"Could not open Webbrowser. Please start "
								+ "your browser manually and go to " + uriString);
			}
		}
	}
}
