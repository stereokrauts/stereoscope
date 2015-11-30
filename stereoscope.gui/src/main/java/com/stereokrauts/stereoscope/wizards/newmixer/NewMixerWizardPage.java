package com.stereokrauts.stereoscope.wizards.newmixer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.forms.widgets.TableWrapData;

public final class NewMixerWizardPage extends WizardPage {
	private Composite mixerConfigurationPart;
	private Composite container;

	/**
	 * Create the wizard.
	 */
	public NewMixerWizardPage() {
		super("wizardPage");
		this.setTitle("Please select the mixer type…");
		this.setDescription("");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(final Composite parent) {
		this.container = new Composite(parent, SWT.NULL);

		final TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		this.container.setLayout(layout);

		this.setControl(this.container);

		final Label lblMixerType = new Label(this.container, SWT.NONE);
		lblMixerType.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));
		lblMixerType.setText("Mixer Type:");

		final Combo combo = new Combo(this.container, SWT.NONE);
		combo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		this.fillMixerSelectionCombo(combo);

		final Label lblNewLabel = new Label(this.container, SWT.NONE);
		lblNewLabel.setText("Configuration:");

		this.mixerConfigurationPart = new Composite(this.container, SWT.NONE);
	}

	private void fillMixerSelectionCombo(final Combo combo) {

	}

	public Composite getMixerConfigurationArea() {
		return this.mixerConfigurationPart;
	}
}
