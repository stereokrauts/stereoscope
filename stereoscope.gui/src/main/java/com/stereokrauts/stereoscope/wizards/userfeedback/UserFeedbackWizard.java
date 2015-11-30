package com.stereokrauts.stereoscope.wizards.userfeedback;

import java.util.logging.Level;

import model.UserFeedback;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

public final class UserFeedbackWizard extends Wizard {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(UserFeedbackWizard.class);
	private final UserFeedbackModel model = new UserFeedbackModel();

	public UserFeedbackWizard() {
		this.setWindowTitle("User Feedback Wizard");
	}

	@Override
	public void addPages() {
		this.addPage(new UserFeedbackContactData());
	}

	@Override
	public boolean performFinish() {
		try {
			new UserFeedback().provideUserFeedback(this.model.getName(), this.model.getEmail(), this.model.getMessage());
		} catch (final Exception e) {
			LOGGER.log(Level.WARNING, "Could not send user feedback: " + this.model.getMessage(), e);
			MessageDialog.openInformation(this.getShell(), "User Feedback", "There was an error transmitting your message: " + e.getMessage() + " - Please write us via eMail: support@stereokrauts.com. Your message has been saved in the error log.");
			return false;
		}
		MessageDialog.openInformation(this.getShell(), "User Feedback", "Thank you!");
		return true;
	}

	public UserFeedbackModel getModel() {
		return this.model;
	}

}
