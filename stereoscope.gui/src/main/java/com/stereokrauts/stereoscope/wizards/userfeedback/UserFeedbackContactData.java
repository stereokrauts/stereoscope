package com.stereokrauts.stereoscope.wizards.userfeedback;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import com.stereokrauts.stereoscope.gui.validators.EmailAddressValidator;
import com.stereokrauts.stereoscope.gui.validators.NonEmptyStringValidator;

public final class UserFeedbackContactData extends WizardPage {
	private Binding messageBinding;
	private Binding nameBinding;
	private static final int MESSAGE_FIELD_HEIGHT = 180;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtName;
	private Text txtEmail;
	private Text formText;
	private UserFeedbackModel model;
	private Binding emailBinding;

	/**
	 * Create the wizard.
	 */
	public UserFeedbackContactData() {
		super("wizardPage");
		this.setTitle("User Feedback");
		this.setDescription("Tell us your comments, suggestions, problems ...");
	}

	@Override
	public void setWizard(final IWizard newWizard) {
		super.setWizard(newWizard);
		this.model = ((UserFeedbackWizard) newWizard).getModel();
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		this.setControl(container);
		{
			final TableWrapLayout twlContainer = new TableWrapLayout();
			twlContainer.numColumns = 2;
			container.setLayout(twlContainer);
		}

		final Label lblNewLabel = this.formToolkit.createLabel(container, "Please fill out the following fields to get in touch with us. We appreciate your feedback. Please make sure that all fields are filled out, so we can reply to your questions and suggestions. Thank you!", SWT.WRAP);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 2));

		final Label lblYourName = this.formToolkit.createLabel(container, "Your name:", SWT.NONE);
		lblYourName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		final TableWrapData twdLblYourName = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1);
		twdLblYourName.align = TableWrapData.RIGHT;
		lblYourName.setLayoutData(twdLblYourName);

		this.txtName = this.formToolkit.createText(container, "New Text", SWT.NONE);
		final TableWrapData twdTxtStereoscopeUser = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1);
		twdTxtStereoscopeUser.grabHorizontal = true;
		this.txtName.setLayoutData(twdTxtStereoscopeUser);
		this.txtName.setText("");

		final Label lblYourEmail = this.formToolkit.createLabel(container, "Your eMail:", SWT.NONE);
		lblYourEmail.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblYourEmail.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

		this.txtEmail = this.formToolkit.createText(container, "New Text", SWT.NONE);
		this.txtEmail.setText("");
		this.txtEmail.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));

		final Label lblMessage = this.formToolkit.createLabel(container, "Message:", SWT.NONE);
		lblMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		final TableWrapData twdLblMessage = new TableWrapData(TableWrapData.LEFT, TableWrapData.TOP, 1, 1);
		twdLblMessage.align = TableWrapData.RIGHT;
		lblMessage.setLayoutData(twdLblMessage);

		this.formText = this.formToolkit.createText(container, "");
		this.formText.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		final TableWrapData twdFormText = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
		twdFormText.heightHint = MESSAGE_FIELD_HEIGHT;
		this.formText.setLayoutData(twdFormText);
		this.initDataBindings();
		this.initDecorators();
	}

	private void initDecorators() {
		ControlDecorationSupport.create(this.emailBinding, SWT.LEFT | SWT.TOP, this.txtEmail.getParent());
		ControlDecorationSupport.create(this.nameBinding, SWT.LEFT | SWT.TOP, this.txtName.getParent());
		ControlDecorationSupport.create(this.messageBinding, SWT.LEFT | SWT.TOP, this.formText.getParent());
	}

	@Override
	public boolean isPageComplete() {
		if (this.model.isValid()) {
			return true;
		}
		return false;
	}
	protected DataBindingContext initDataBindings() {
		final DataBindingContext bindingContext = new DataBindingContext();
		//
		final IObservableValue txtEmailexamplecomObserveTextObserveWidget = SWTObservables.observeText(this.txtEmail, SWT.Modify);
		final IObservableValue modelEmailObserveValue = BeansObservables.observeValue(this.model, "email");
		final UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterConvertValidator(new EmailAddressValidator());
		this.emailBinding = bindingContext.bindValue(txtEmailexamplecomObserveTextObserveWidget, modelEmailObserveValue, strategy, null);
		//
		final IObservableValue txtStereoscopeUserObserveTextObserveWidget = SWTObservables.observeText(this.txtName, SWT.Modify);
		final IObservableValue modelNameObserveValue = BeansObservables.observeValue(this.model, "name");
		final UpdateValueStrategy strategy_1 = new UpdateValueStrategy();
		strategy_1.setAfterConvertValidator(new NonEmptyStringValidator());
		this.nameBinding = bindingContext.bindValue(txtStereoscopeUserObserveTextObserveWidget, modelNameObserveValue, strategy_1, null);
		//
		final IObservableValue formTextObserveTextObserveWidget = SWTObservables.observeText(this.formText, SWT.Modify);
		final IObservableValue modelMessageObserveValue = BeansObservables.observeValue(this.model, "message");
		final UpdateValueStrategy strategy_2 = new UpdateValueStrategy();
		strategy_2.setAfterConvertValidator(new NonEmptyStringValidator());
		this.messageBinding = bindingContext.bindValue(formTextObserveTextObserveWidget, modelMessageObserveValue, strategy_2, null);
		//
		return bindingContext;
	}
}
