package com.stereokrauts.stereoscope.gui.main;

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

import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageStore;

public final class NewDocumentDialog extends Dialog {
	private NewDocumentDialogResult result;
	private Shell shlNewDocument;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewDocumentDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public NewDocumentDialogResult open() {
		this.createContents();
		this.shlNewDocument.open();
		this.shlNewDocument.layout();
		final Display display = this.getParent().getDisplay();
		while (!this.shlNewDocument.isDisposed()) {
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
		this.shlNewDocument = new Shell(this.getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		this.shlNewDocument.setText("Surface connection");
		this.shlNewDocument.setLayout(new GridLayout(4, false));

		final Label lblPleaseChooseThe = new Label(this.shlNewDocument, SWT.NONE);
		lblPleaseChooseThe.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblPleaseChooseThe.setText("Please select the mixer type you wish to control with Stereoscope");

		final Label lblNetworkAddress = new Label(this.shlNewDocument, SWT.NONE);
		lblNetworkAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNetworkAddress.setText("Mixer Type:");

		final Combo cmbMixerSelection = new Combo(this.shlNewDocument, SWT.READ_ONLY);
		cmbMixerSelection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		for (final MixerPluginImageAssociation assc : MixerPluginImageStore.getInstance().getAllEntries()) {
			cmbMixerSelection.add(assc.getPluginId());
		}
		cmbMixerSelection.select(0);

		new Label(this.shlNewDocument, SWT.NONE);
		final Button btnOk = new Button(this.shlNewDocument, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				NewDocumentDialog.this.result = new NewDocumentDialogResult(cmbMixerSelection.getText(),
						true);
				NewDocumentDialog.this.shlNewDocument.close();
			}
		});
		btnOk.setText("OK");

		final Button btnAbort = new Button(this.shlNewDocument, SWT.NONE);
		btnAbort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				NewDocumentDialog.this.result = new NewDocumentDialogResult("", false);
				NewDocumentDialog.this.shlNewDocument.close();
			}
		});
		btnAbort.setText("Abort");

		this.shlNewDocument.pack();
		new Label(this.shlNewDocument, SWT.NONE);
	}

	public NewDocumentDialogResult getResult() {
		return this.result;
	}

}
