package com.stereokrauts.stereoscope.gui.metrics.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


public class ArrowShape extends Canvas implements PaintListener {
	boolean isActive = false;
	boolean lastPaintedState = true;

	private final Direction direction;

	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	public ArrowShape(final Composite parent, final int style, final Direction direction) {
		super(parent, style);
		this.direction = direction;
		addPaintListener(this);
	}

	public void setActive(final boolean active) {
		this.isActive = active;
		if (this.lastPaintedState != this.isActive) {
			this.redraw();
		}
	}

	@Override
	public void paintControl(final PaintEvent e) {
		final Rectangle clientArea = this.getClientArea();
		final GC gc = e.gc;
		gc.setAntialias(SWT.ON);

		final int width = Math.min(clientArea.width, clientArea.height) - 1;
		final int height = Math.min(clientArea.width, clientArea.height) - 1;

		final int xtriAnglePosition = width / 3;
		final int yShaftPosition = height / 3;

		final Point tip = new Point(0, height / 2);
		final Point triUp = new Point(xtriAnglePosition, 0);
		final Point shaftUp = new Point(xtriAnglePosition, yShaftPosition);
		final Point shaftUpEnd = new Point(width, yShaftPosition);
		final Point shaftDownEnd = new Point(width, height - yShaftPosition);
		final Point shaftDown = new Point(xtriAnglePosition, height - yShaftPosition);
		final Point triDown = new Point(xtriAnglePosition, height);

		float cos = 0;
		float sin = 0;
		int dx = 0;
		int dy = 0;

		switch (direction) {
		case LEFT:
			cos = 1;
			sin = 0;
			dx = 0;
			dy = 0;
			break;
		case DOWN:
			cos = (float)Math.cos(1.5 * Math.PI);
			sin = (float)Math.sin(1.5 * Math.PI);
			dx = 0;
			dy = height + 1;
			break;
		case RIGHT:
			cos = (float)Math.cos(Math.PI);
			sin = (float)Math.sin(Math.PI);
			dx = width;
			dy = height;
			break;
		case UP:
			cos = (float)Math.cos(Math.PI / 2);
			sin = (float)Math.sin(Math.PI / 2);
			dx = width;
			dy = 0;
			break;
		}

		final int[] pointArray = new int[] {
				tip.x, tip.y, // Tip
				triUp.x, triUp.y,  // Upper end of arrow
				shaftUp.x, shaftUp.y, // Beginning of shaft
				shaftUpEnd.x, shaftUpEnd.y, // End of shaft
				shaftDownEnd.x, shaftDownEnd.y, // Lower end
				shaftDown.x, shaftDown.y, // Lower beginning of shaft
				triDown.x, triDown.y, // lower end of arrow
				tip.x, tip.y // Tip and finnished.
		};

		final Transform rotation = new Transform(getDisplay());
		rotation.setElements(cos, sin, -sin, cos, dx, dy);
		gc.setTransform(rotation);

		if (!this.isActive) {
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		} else {
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_GREEN));
		}
		gc.fillPolygon(pointArray);
		gc.setForeground(getParent().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.drawPolygon(pointArray);

		rotation.dispose();

		this.lastPaintedState = this.isActive;
	}
}

