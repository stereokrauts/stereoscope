package com.stereokrauts.stereoscope.wizards.document;

import model.protocol.osc.OscConstants;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public final class BusElementAddPageConfiguration extends WizardPage implements Listener {

	private Text txtNetworkPort;
	private Text txtNetworkAddress;
	private Text txtServerNetworkPort;

	/**
	 * Create the wizard.
	 */
	public BusElementAddPageConfiguration() {
		super("wizardPage");
		this.setTitle("Add a new element to the bus");
		this.setDescription("Configuration");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);

		container.setLayout(new GridLayout(4, false));

		final Label lblPleaseChooseThe = new Label(container, SWT.NONE);
		lblPleaseChooseThe.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblPleaseChooseThe.setText("Please enter the connection settings of your Surface");

		final Label lblNetworkAddress = new Label(container, SWT.NONE);
		lblNetworkAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNetworkAddress.setText("Network name or IP:");

		this.txtNetworkAddress = new Text(container, SWT.READ_ONLY);
		this.txtNetworkAddress.setEditable(true);
		this.txtNetworkAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		this.txtNetworkAddress.addListener(SWT.CHANGED, this);

		final Label lblNetworkPort = new Label(container, SWT.NONE);
		lblNetworkPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNetworkPort.setText("OSC Port:");

		this.txtNetworkPort = new Text(container, SWT.READ_ONLY);
		this.txtNetworkPort.setEditable(true);
		this.txtNetworkPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		this.txtNetworkPort.addListener(SWT.CHANGED, this);

		final Label lblServerNetworkPort = new Label(container, SWT.NONE);
		lblServerNetworkPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerNetworkPort.setText("Stereoscope Server OSC Port:");

		txtServerNetworkPort = new Text(container, SWT.READ_ONLY);
		txtServerNetworkPort.setEditable(true);
		txtServerNetworkPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		txtServerNetworkPort.setText("" + OscConstants.DEFAULT_OSC_SERVER_PORT);
		txtServerNetworkPort.addListener(SWT.CHANGED, this);

		container.pack();

		this.setControl(container);
	}

	BusElementAddWizard getCastedWizard() {
		return (BusElementAddWizard) this.getWizard();
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (visible) {
			this.updateTexts();
		}
	}

	private void updateTexts() {
		if (this.getCastedWizard().getResult().getNetworkPort() != null) {
			this.txtNetworkPort.setText("" + this.getCastedWizard().getResult().getNetworkPort());
		} else {
			this.txtNetworkPort.setText("" + OscConstants.DEFAULT_OSC_CLIENT_PORT);
		}
		if (this.getCastedWizard().getResult().getNetworkAddress() != null) {
			this.txtNetworkAddress.setText(this.getCastedWizard().getResult().getNetworkAddress());
		}
	}

	@Override
	public void handleEvent(final Event event) {
		this.getCastedWizard().getResult().setClientPort(txtNetworkPort.getText());
		this.getCastedWizard().getResult().setServerPort(txtServerNetworkPort.getText());
		this.getCastedWizard().getResult().setNetworkAddress(txtNetworkAddress.getText());
		getWizard().getContainer().updateButtons();
	}

}
