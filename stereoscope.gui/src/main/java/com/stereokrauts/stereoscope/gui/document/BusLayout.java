package com.stereokrauts.stereoscope.gui.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.gui.document.BusLayoutAware.LayoutPosition;

public final class BusLayout extends Layout {
	private class Coordinate {
		Point p;
		LayoutPosition position;

		public Coordinate(final Point p, final LayoutPosition position) {
			this.p = p;
			this.position = position;
		}
	}
	private List<Coordinate> busAttendeesCoordinates;
	private static final SLogger LOG = StereoscopeLogManager.getLogger(BusLayout.class);

	public static final int DEFAULT_SPACING_Y = 20;
	public static final int DEFAULT_SPACING_X = 20;

	public static final int ICON_SIZE_X = 100;
	public static final int ICON_SIZE_Y = 115;

	static final int TEXT_HEIGHT = 10;

	private int childCounter;

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		final Control children[] = composite.getChildren();
		if (flushCache || this.busAttendeesCoordinates == null || this.busAttendeesCoordinates.size() != children.length) {
			this.initialize(children);
		}

		return new Point(this.childCounter * (DEFAULT_SPACING_X + ICON_SIZE_X), 2 * (ICON_SIZE_Y + DEFAULT_SPACING_X));
	}

	private void initialize(final Control[] children) {
		this.busAttendeesCoordinates = new ArrayList<Coordinate>();
		this.childCounter = 0;
		boolean placeOnTopOfLine = false;
		for (int i = 0; i < children.length; i++) {
			this.busAttendeesCoordinates.add(new Coordinate(new Point(this.childCounter * ICON_SIZE_X + DEFAULT_SPACING_X, placeOnTopOfLine ? DEFAULT_SPACING_Y : (ICON_SIZE_Y + 3 * DEFAULT_SPACING_Y)), placeOnTopOfLine ? LayoutPosition.TOP : LayoutPosition.BOTTOM));
			this.childCounter++;
			placeOnTopOfLine = !placeOnTopOfLine;
		}
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Control children[] = composite.getChildren();
		LOG.finest("Layouting...");
		if (flushCache || this.busAttendeesCoordinates == null || this.busAttendeesCoordinates.size() != children.length) {
			LOG.finest("Reinitializing children");
			this.initialize(children);
		}

		//Rectangle rect = composite.getClientArea();
		for (int i = 0; i < children.length; i++) {
			final Coordinate coordinate = this.busAttendeesCoordinates.get(i);
			final Point place = coordinate.p;        	
			LOG.finest("Putting child " + children[i] + " of " + composite + " at position " + place);
			children[i].setBounds(place.x, place.y, ICON_SIZE_X, ICON_SIZE_Y + TEXT_HEIGHT);
			if (children[i] instanceof BusLayoutAware) {
				final BusLayoutAware busLayoutAware = (BusLayoutAware) children[i];
				busLayoutAware.setBusLayoutPosition(coordinate.position);
			}
		}

		LOG.finest("Finished layouting!");
	}


}
