package com.stereokrauts.stereoscope.gui.metrics.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.stereokrauts.stereoscope.gui.metrics.view.ArrowShape.Direction;

public class LaunchArrowShape {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new ArrowShape(shell, SWT.NONE, Direction.UP);
		shell.open();


		// run the event loop as long as the window is open
		while (!shell.isDisposed()) {
			// read the next OS event queue and transfer it to a SWT event 
			if (!display.readAndDispatch())
			{
				// if there are currently no other OS event to process
				// sleep until the next OS event is available 
				display.sleep();
			}
		}

		// disposes all associated windows and their components
		display.dispose(); 
	}

}
