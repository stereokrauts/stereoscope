package com.stereokrauts.stereoscope.wizards.document;

import model.protocol.osc.OscConstants;

import org.eclipse.jface.wizard.Wizard;

import com.stereokrauts.stereoscope.wizards.document.BusElementAddWizardResult.ClientType;

public final class BusElementAddWizard extends Wizard {
	BusElementAddWizardResult result = new BusElementAddWizardResult(ClientType.TOUCHOSC, null, "" + OscConstants.DEFAULT_OSC_CLIENT_PORT, "" + OscConstants.DEFAULT_OSC_SERVER_PORT);

	public BusElementAddWizard() {
		this.setWindowTitle("Add a TouchOSC surface to the bus");
	}

	@Override
	public void addPages() {
		this.addPage(new BusElementAddPageAutodetected());
		this.addPage(new BusElementAddPageConfiguration());
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public BusElementAddWizardResult getResult() {
		return result;
	}

	@Override
	public boolean canFinish() {
		return validateWebclient() || validateTouchOSC();
	}

	private boolean validateTouchOSC() {
		return getResult().isWebclient() && getResult().getWebclientPort() != null && getResult().getWebclientPort() != 0;
	}

	private boolean validateWebclient() {
		return getResult().isTouchOSC()
				&& getResult().getNetworkAddress() != null && !getResult().getNetworkAddress().equals("")
				&& getResult().getNetworkPort() != null && getResult().getNetworkPort() != 0
				&& getResult().getServerNetworkPort() != null && getResult().getServerNetworkPort() != 0;
	}

}
