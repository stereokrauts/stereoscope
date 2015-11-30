package com.stereokrauts.lib.common.spring;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.springframework.util.SpringExtensionFactory;

import com.stereokrauts.stereoscope.controller.interfaces.IController;

/**
 * This class is glue code between eclipse API and
 * Spring-DM.
 * @author th
 *
 */
public final class SpringEditorFactory extends SpringExtensionFactory {
	@Override
	public Object create() throws CoreException {
		final Object obj = super.create();
	
		Assert.isTrue(obj instanceof IController, "The referred bean should subclass IController, but was: "
		+ obj);
	
		final IController<?, ?> controller = (IController<?, ?>) obj;
	
		return controller.getView();
	}
}
