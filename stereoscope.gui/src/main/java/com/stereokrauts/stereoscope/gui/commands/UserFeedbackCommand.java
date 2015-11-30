package com.stereokrauts.stereoscope.gui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.stereoscope.wizards.userfeedback.UserFeedbackWizard;

public final class UserFeedbackCommand extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final UserFeedbackWizard wizard = new UserFeedbackWizard();
		final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);
		dialog.open();
		return null;
	}

}
