package com.stereokrauts.stereoscope.wizards.newmixer;

import org.eclipse.jface.wizard.Wizard;

public final class NewMixerWizard extends Wizard {
	private final NewMixerWizardModel model = new NewMixerWizardModel(); 

	public NewMixerWizard() {
		this.setWindowTitle("New Mixer...");
	}

	@Override
	public void addPages() {
		this.addPage(new NewMixerWizardPage());
	}

	@Override
	public boolean performFinish() {
		return this.model.isValid();
	}

}
