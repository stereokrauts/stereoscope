package com.stereokrauts.stereoscope.gui.document;

import model.bus.Bus;
import model.bus.BusAttendee;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import aspects.observer.IAspectedObserver;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.DocumentController;

/**
 * Draws a bus attendee on the screen.
 * @author th
 *
 */
public final class BusAttendeeView extends Composite implements IAspectedObserver {
	private static final int RECTANGLE_DISTANCE = 15;
	private static final int ALPHA_NON_TRANSPARENT = 255;
	private static final int SELECTED_COLOR_BLUE = 255;
	private static final int SELECTED_COLOR_GREEN = 120;
	private static final int SELECTED_COLOR_RED = 120;
	private static final int WIDGET_IMAGE_SIZE = 80;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(BusAttendeeView.class);

	private Image image;
	private Image scaledImage;
	private String text;

	private DocumentController controller;

	private Bus bus;
	private BusAttendee attendee;

	private boolean selected;

	public BusAttendeeView(final Composite parent, final int style) {
		super(parent, style | SWT.NO_BACKGROUND);
		this.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				BusAttendeeView.this.paintControl(e);
			}
		});
		this.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final Menu active = BusAttendeeView.this.getDisplay().getActiveShell().getMenu();
				if (active != null) {
					active.dispose();
				}
				final Menu menu = new Menu(BusAttendeeView.this.getDisplay().getActiveShell(), SWT.POP_UP);
				final MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Remove");
				item.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(final Event event) {
						BusAttendeeView.this.controller.removeBusElementRequested(BusAttendeeView.this.bus, BusAttendeeView.this.attendee);
					}
				});
				BusAttendeeView.this.getDisplay().getActiveShell().setMenu(menu);
			}
		});
	}

	protected void paintControl(final PaintEvent e) {
		final GC gc = e.gc;
		gc.setAntialias(SWT.ON);
		final int textYoffset = this.scaledImage != null ? this.scaledImage.getBounds().height + 5 : 0;
		LOG.finest("Bounds for " + this + ": " + this.getBounds());
		gc.setLineWidth(3);
		gc.setAlpha(128);
		if (this.selected) {
			gc.setForeground(new Color(this.getDisplay(), SELECTED_COLOR_RED, SELECTED_COLOR_GREEN, SELECTED_COLOR_BLUE));
		} else {
			gc.setForeground(this.getBackground());
		}
		gc.drawRoundRectangle(0, 0, this.getClientArea().width - 3, this.getClientArea().height - 3, RECTANGLE_DISTANCE, RECTANGLE_DISTANCE);
		gc.setForeground(new Color(this.getDisplay(), 0, 0, 0));
		gc.setAlpha(ALPHA_NON_TRANSPARENT);
		if (this.scaledImage != null) {
			// center the image
			final int imageXoffset = (BusLayout.ICON_SIZE_X - this.scaledImage.getImageData().width) / 2;

			gc.drawImage(this.scaledImage, imageXoffset, 0);
		}
		if (this.text != null) {	
			int textXoffset = 0;
			final int textWidth = gc.stringExtent(this.text).x;
			if (this.getBounds().width > textWidth) {
				textXoffset = (this.getBounds().width - textWidth) / 2;
			}
			gc.drawString(this.getText(), textXoffset, textYoffset, true);
		}

	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		return new Point(BusLayout.ICON_SIZE_X, BusLayout.ICON_SIZE_X);
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
		final int width = this.image.getImageData().width;
		final int height = this.image.getImageData().height;
		if (width > WIDGET_IMAGE_SIZE || height > WIDGET_IMAGE_SIZE) {
			final float scaleFactor = (width > height) ? (float) WIDGET_IMAGE_SIZE / width : (float) WIDGET_IMAGE_SIZE / height;
			this.scaledImage = new Image(this.getDisplay(), this.image.getImageData().scaledTo((int) (width * scaleFactor), (int) (height * scaleFactor)));
		} else {
			this.scaledImage = this.image;
		}
		this.redraw();
	}

	public void setController(final DocumentController controller) {
		this.controller = controller;
	}

	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	public void setAttendee(final BusAttendee attendee) {
		this.attendee = attendee;
		attendee.getBean().getObserverManager().addObserver(this);
	}

	public void setSelected(final boolean b) {
		this.selected = b;
		this.redraw();
	}

	@Override
	public void valueChangedEvent(final Object sender, final String fieldName, final Object oldValue, final Object newValue) {
		if (fieldName.equals("clientName")) {
			this.setText(newValue.toString());
			this.redraw();
		}
	}

}
