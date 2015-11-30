package com.stereokrauts.stereoscope.gui.document;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.gui.metrics.view.ArrowShape;
import com.stereokrauts.stereoscope.gui.metrics.view.ArrowShape.Direction;

public class BusAttendeeWithCommunicationView extends Composite implements BusLayoutAware, ICommunicationAware {
	public class BusCommunicationLayout extends Layout {
		@Override
		protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
			final Point childSize = composite.getChildren()[2].computeSize(wHint, hHint);
			return new Point(childSize.x, childSize.y + MY_HEIGHT);
		}

		@Override
		protected void layout(final Composite composite, final boolean flushCache) {
			final Control[] children = composite.getChildren();
			final Point whishedSize = children[2].computeSize(0, 0);

			if (LayoutPosition.BOTTOM.equals(position)) {
				children[0].setBounds(whishedSize.x / 2 - MY_HEIGHT, 0, MY_HEIGHT, MY_HEIGHT);
				children[1].setBounds(whishedSize.x / 2, 0, MY_HEIGHT, MY_HEIGHT);

				children[2].setBounds(0, MY_HEIGHT, whishedSize.x, whishedSize.y);
			} else {
				children[2].setBounds(0, 0, whishedSize.x, whishedSize.y);

				children[0].setBounds(whishedSize.x / 2 - MY_HEIGHT, whishedSize.y, MY_HEIGHT, whishedSize.y + MY_HEIGHT);
				children[1].setBounds(whishedSize.x / 2, whishedSize.y, MY_HEIGHT, whishedSize.y + MY_HEIGHT);
			}
		}

	}

	private static final int MY_HEIGHT = 15;

	private final ArrowShape up;
	private final ArrowShape down;
	private LayoutPosition position;

	public BusAttendeeWithCommunicationView(final Composite parent, final int style) {
		super(parent, style);

		up = new ArrowShape(this, style, Direction.UP);
		down = new ArrowShape(this, style, Direction.DOWN);

		setLayout(new BusCommunicationLayout());
		this.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				BusAttendeeWithCommunicationView.this.paintControl(e);
			}
		});

		switchOffThread = new SwitchOffThread();
		final Thread t = new Thread(switchOffThread);
		t.start();
	}

	protected void paintControl(final PaintEvent e) {
	}

	@Override
	public void setBusLayoutPosition(final LayoutPosition position) {
		this.position = position;
		this.layout();
		this.redraw();
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		return new Point(BusLayout.ICON_SIZE_X, BusLayout.ICON_SIZE_Y);
	}

	private static long REFRESH_TIMEOUT = TimeUnit.SECONDS.toMillis(1);
	private long lastUpActive = 0;
	private long lastDownActive = 0;

	private final SwitchOffThread switchOffThread;

	private class SwitchOffThread implements Runnable {
		private boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				try {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							final long currentTime = System.currentTimeMillis();
							if (currentTime - BusAttendeeWithCommunicationView.this.lastUpActive > REFRESH_TIMEOUT) {
								if (!up.isDisposed()) {
									up.setActive(false);
								}
							}
							if (currentTime - BusAttendeeWithCommunicationView.this.lastDownActive > REFRESH_TIMEOUT) {
								if (!down.isDisposed()) {
									down.setActive(false);
								}
							}
						}
					});
				} catch (final SWTException e) {
					// just ignore, happens on shutdown occassionally.
				}
				try {
					Thread.sleep(REFRESH_TIMEOUT);
				} catch (final InterruptedException e) {
					stop = true;
				}
			}
		}

		public void requestStop() {
			this.stop = true;
		}
	}

	@Override
	public void receive() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (LayoutPosition.BOTTOM.equals(position)) {
					if (!down.isDisposed()) {
						down.setActive(true);
					}
					lastDownActive = System.currentTimeMillis();
				} else {
					if (!up.isDisposed()) {
						up.setActive(true);
					}
					lastUpActive = System.currentTimeMillis();
				}
			}
		});
	}

	@Override
	public void transmit() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (LayoutPosition.BOTTOM.equals(position)) {
					if (!up.isDisposed()) {
						up.setActive(true);
					}
					lastUpActive = System.currentTimeMillis();
				} else {
					if (!down.isDisposed()) {
						down.setActive(true);
					}
					lastDownActive = System.currentTimeMillis();
				}
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		switchOffThread.requestStop();
	}

}
