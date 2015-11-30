package com.stereokrauts.stereoscope.plugin.gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;
import com.stereokrauts.stereoscope.plugin.ui.MidiPortSelection;

public final class EclipseMidiPortSelection extends Dialog implements MidiPortSelection {
	private EclipseMidiPortSelectionResult result;
	private Shell shlPleaseConfigureThe;
	private final List<? extends Object> midiInputPorts;
	private final List<? extends Object> midiOutputPorts;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EclipseMidiPortSelection(final AbstractApplicationContext ctx, final List<? extends Object> midiInputPorts, final List<? extends Object> midiOutputPorts) {
		super(((IViewContext)ctx.getViewContext()).getRootShell(), SWT.DEFAULT);
		this.midiInputPorts = midiInputPorts;
		this.midiOutputPorts = midiOutputPorts;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	@Override
	public EclipseMidiPortSelectionResult open() {
		this.createContents();
		this.shlPleaseConfigureThe.open();
		this.shlPleaseConfigureThe.layout();
		final Display display = this.getParent().getDisplay();
		while (!this.shlPleaseConfigureThe.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		this.shlPleaseConfigureThe = new Shell(this.getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		this.shlPleaseConfigureThe.setText("Mixer connection");
		this.shlPleaseConfigureThe.setLayout(new GridLayout(4, false));

		final Label lblPleaseChooseThe = new Label(this.shlPleaseConfigureThe, SWT.NONE);
		lblPleaseChooseThe.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblPleaseChooseThe.setText("Please choose the correct MIDI ports to which your mixer is connected:");

		final Label lblMidiInputPort = new Label(this.shlPleaseConfigureThe, SWT.NONE);
		lblMidiInputPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMidiInputPort.setText("MIDI Input Port:");

		final Combo cmbMidiInput = new Combo(this.shlPleaseConfigureThe, SWT.READ_ONLY);
		cmbMidiInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		for (final Object o : this.midiInputPorts) {
			cmbMidiInput.add(o.toString());
		}
		cmbMidiInput.select(0);

		final Label lblMidiOutputPort = new Label(this.shlPleaseConfigureThe, SWT.NONE);
		lblMidiOutputPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMidiOutputPort.setText("MIDI Output Port:");

		final Combo cmbMidiOutput = new Combo(this.shlPleaseConfigureThe, SWT.READ_ONLY);
		cmbMidiOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		for (final Object o : this.midiOutputPorts) {
			cmbMidiOutput.add(o.toString());
		}
		cmbMidiOutput.select(0);

		new Label(this.shlPleaseConfigureThe, SWT.NONE);
		final Button btnOk = new Button(this.shlPleaseConfigureThe, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				EclipseMidiPortSelection.this.result = new EclipseMidiPortSelectionResult(EclipseMidiPortSelection.this.midiInputPorts.get(cmbMidiInput.getSelectionIndex()),
						EclipseMidiPortSelection.this.midiOutputPorts.get(cmbMidiOutput.getSelectionIndex()),
						true);
				EclipseMidiPortSelection.this.shlPleaseConfigureThe.close();
			}
		});
		btnOk.setText("OK");

		final Button btnAbort = new Button(this.shlPleaseConfigureThe, SWT.NONE);
		btnAbort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				EclipseMidiPortSelection.this.result = new EclipseMidiPortSelectionResult("", "", false);
				EclipseMidiPortSelection.this.shlPleaseConfigureThe.close();
			}
		});
		btnAbort.setText("Abort");
		new Label(this.shlPleaseConfigureThe, SWT.NONE);

		this.shlPleaseConfigureThe.pack();
	}

	@Override
	public EclipseMidiPortSelectionResult getResult() {
		return this.result;
	}

}
