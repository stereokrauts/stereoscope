package com.stereokrauts.stereoscope.gui.startup;


import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;

/**
 * This is a button that simply displays an image it is
 * given.
 * @author th
 *
 */
public final class StartupMixerButton extends Composite {
	private Image image;
	private String text;
	private MixerPluginImageAssociation pluginDescriptor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StartupMixerButton(final Composite parent, final int style) {
		super(parent, style);
		this.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(final PaintEvent e) {
				StartupMixerButton.this.paintControl(e);
			}
		});
		this.setBackground(parent.getBackground());
	}
	

	protected void paintControl(final PaintEvent e) {
		final GC gc = e.gc;
		int textYOffset = 0;
		if (this.image != null) {
			int imageXOffset = 0;
			if (this.text != null) {
				final int textWidth = gc.stringExtent(this.text).x;
				if (textWidth > this.getImage().getBounds().width) {
					imageXOffset = (textWidth - this.getImage().getBounds().width) / 2;
				}
			}
			
			gc.drawImage(this.getImage(), imageXOffset, 0);
			textYOffset += this.getImage().getBounds().height;
		}
		if (this.text != null) {	
			int textXOffset = 0;
			if (this.image != null) {
				final int textWidth = gc.stringExtent(this.text).x;
				if (this.getImage().getBounds().width > textWidth) {
					textXOffset = (this.getImage().getBounds().width - textWidth) / 2;
				}
			}
			gc.drawString(this.getText(), textXOffset, textYOffset);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		int height = 0;
		int width = 0;
		if (this.text != null) {
			final GC gc = new GC(this);
			final Point extent = gc.stringExtent(this.text);
			gc.dispose();
			height += extent.y;
			width += extent.x;
		}
		if (this.image != null) {
			height += this.image.getBounds().height;
			width = Math.max(width, this.image.getBounds().width);
		}
		return new Point(width, height);
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
		this.redraw();
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(final Image i) {
		this.image = i;
		this.redraw();
	}

	public MixerPluginImageAssociation getPluginAssociation() {
		return this.pluginDescriptor;
	}

	public void setPluginAssociation(final MixerPluginImageAssociation pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

}
