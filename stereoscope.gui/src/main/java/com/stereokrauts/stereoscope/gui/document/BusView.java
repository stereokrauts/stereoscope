package com.stereokrauts.stereoscope.gui.document;

import java.awt.Desktop;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import model.bus.Bus;
import model.bus.BusAttendee;
import model.bus.BusAttendeeType;
import model.bus.BusAttendeeWebclient;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.DocumentController;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationObservable;
import com.stereokrauts.stereoscope.gui.util.ImageUtil;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginDidNotProvideImageException;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageStore;

public final class BusView extends Composite	 {

	private static final SLogger LOG = StereoscopeLogManager.getLogger(BusView.class);

	private Bus bus;
	private BusAttendeeView emptyAttendee;
	private final Map<BusAttendee, BusAttendeeView> attendees = new HashMap<BusAttendee, BusAttendeeView>();

	private DocumentController controller;

	protected BusAttendeeView selection;

	public BusView(final Composite parent, final int style) {
		super(parent, style);
		this.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				BusView.this.paintControl(e);
			}
		});
		this.setBackground(parent.getBackground());
		this.setLayout(new BusLayout());

		initializeBusAttendeeAddButton(this.emptyAttendee, "Add another...", new Runnable() {
			@Override
			public void run() {
				BusView.this.controller.newBusElementRequested();
			}
		});
	}

	private void initializeBusAttendeeAddButton(BusAttendeeView attendeeView, final String title, final Runnable mouseAction) {
		attendeeView = new BusAttendeeView(this, SWT.NONE);
		try {
			final URL image = FileLocator.resolve(new URL("platform:/plugin/stereoscope.gui/src/main/resources/images/BusAttendeeAdd.png"));
			final ImageData desc = new ImageData(image.openStream());
			attendeeView.setImage(new Image(this.getDisplay(), desc));
			attendeeView.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(final MouseEvent e) {
					mouseAction.run();
				}

				@Override
				public void mouseDown(final MouseEvent e) { }

				@Override
				public void mouseDoubleClick(final MouseEvent e) { }
			});
		} catch (final Exception e1) {
			LOG.log(Level.WARNING, "Could not load BusAttendeeAdd image.", e1);
		}
		attendeeView.setText(title);
	}

	protected void paintControl(final PaintEvent e) {
		final GC gc = e.gc;

		gc.setLineWidth(2);

		final int middleY = BusLayout.ICON_SIZE_Y + 2 * BusLayout.DEFAULT_SPACING_X;
		final int spacingX = BusLayout.DEFAULT_SPACING_X + (BusLayout.ICON_SIZE_X / 2);

		gc.drawLine(spacingX, middleY, this.getSize().x - spacingX, middleY);

		for (int i = 0; i < this.attendees.keySet().size() + 1; i++) {
			final int xoffset = (i * BusLayout.ICON_SIZE_X);
			final int upDown = (i % 2) > 0 ? -BusLayout.DEFAULT_SPACING_X : BusLayout.DEFAULT_SPACING_X;
			gc.drawLine(xoffset + spacingX, middleY, xoffset + spacingX, middleY + upDown);
		}
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final int height = 2 * BusLayout.ICON_SIZE_Y + 5 * BusLayout.DEFAULT_SPACING_X;
		int width = 2 * BusLayout.DEFAULT_SPACING_X + BusLayout.ICON_SIZE_X;
		if (this.bus != null) {
			width += this.bus.getAttendees().size() * (BusLayout.ICON_SIZE_X + BusLayout.DEFAULT_SPACING_X) + 2 * BusLayout.DEFAULT_SPACING_X;
		}
		LOG.info("Computed my own size: height=" + height + ", width=" + width);
		return new Point(width, height);
	}

	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	public void addBusAttendee(final Bus bus, final BusAttendee attendee) {
		final BusAttendeeWithCommunicationView commView = new BusAttendeeWithCommunicationView(BusView.this, SWT.NONE);
		if (attendee instanceof ICommunicationObservable) {
			final ICommunicationObservable iCommunicationObservable = (ICommunicationObservable) attendee;
			iCommunicationObservable.setCommunicationObserver(commView);
		}
		final BusAttendeeView view = new BusAttendeeView(commView, SWT.NONE);
		view.setController(this.controller);
		if (attendee.getType().equals(BusAttendeeType.MIXER)) {
			this.addMixerImageToView(attendee, view);
		} else if (attendee.getType().equals(BusAttendeeType.SURFACE_TOUCHOSC)) {
			this.addTouchOscImageToView(attendee, view);
		} else if (attendee.getType().equals(BusAttendeeType.WEBCLIENT)) {
			final BusAttendeeWebclient webClientAttendee = (BusAttendeeWebclient) attendee;
			this.addWebclientImageToView(attendee, view);
			view.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(final MouseEvent e) {
				}

				@Override
				public void mouseDown(final MouseEvent e) {
				}

				@Override
				public void mouseDoubleClick(final MouseEvent e) {
					try
					{
						final URI uri = new URI(String.format("http://localhost:%d/", webClientAttendee.getBean().getPortNumber()));
						final Desktop dt = Desktop.getDesktop();
						dt.browse(uri);
					}
					catch(final Exception ex){}
				}
			});
		}
		view.setText(attendee.getName());
		view.setBus(bus);
		view.setAttendee(attendee);
		view.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(final MouseEvent e) {
				LOG.fine("Showing properties of " + attendee);
				if (BusView.this.selection != null) {
					BusView.this.selection.setSelected(false);
				}
				view.setSelected(true);
				BusView.this.controller.displayPropertiesOf(bus, attendee);
				BusView.this.selection = view;
			}

			@Override
			public void mouseDown(final MouseEvent e) { }

			@Override
			public void mouseDoubleClick(final MouseEvent e) { }
		});
		this.attendees.put(attendee, view);
		this.pack();
		this.redraw();
	}

	private void addTouchOscImageToView(final BusAttendee attendee,
			final BusAttendeeView view) {
		try {
			final InputStream touchOscImage = FileLocator.resolve(new URL("platform:/plugin/stereoscope.gui/src/main/resources/images/BusAttendee_ipad.png")).openStream();
			view.setImage(new Image(this.getDisplay(), new ImageData(touchOscImage)));
		} catch (final Exception e) {
			LOG.fatal("Could not load TouchOsc image for " + attendee.getName(), e);
		}
	}

	private void addWebclientImageToView(final BusAttendee attendee,
			final BusAttendeeView view) {
		try {
			final InputStream touchOscImage = FileLocator.resolve(new URL("platform:/plugin/stereoscope.gui/src/main/resources/images/BusAttendee_webclient.png")).openStream();
			view.setImage(new Image(this.getDisplay(), new ImageData(touchOscImage)));
		} catch (final Exception e) {
			LOG.fatal("Could not load Webclient image for " + attendee.getName(), e);
		}
	}

	private void addMixerImageToView(final BusAttendee attendee,
			final BusAttendeeView view) {
		try {
			final MixerPluginImageAssociation assoc = MixerPluginImageStore.getInstance().getEntryFor(attendee.getIdentifier());
			view.setImage(new Image(this.getDisplay(), ImageUtil.getImageDataFrom(assoc.getImage())));
		} catch (final MixerPluginDidNotProvideImageException e) {
			LOG.fatal("Mixer " + attendee.getName() + " did not provide image", e);
		}
	}

	public void removeBusAttendee(final Bus bus, final BusAttendee attendee) {
		this.attendees.get(attendee).dispose();
		this.attendees.remove(attendee);
		this.pack();
		this.redraw();
	}

	public void setController(final DocumentController controller) {
		this.controller = controller;
	}
}
