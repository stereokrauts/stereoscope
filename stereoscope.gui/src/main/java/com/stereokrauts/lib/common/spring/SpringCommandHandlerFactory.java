package com.stereokrauts.lib.common.spring;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.springframework.util.SpringExtensionFactory;

public final class SpringCommandHandlerFactory extends SpringExtensionFactory {
	@Override
	public Object create() throws CoreException {
		final Object obj = super.create();
	
		Assert.isTrue(obj instanceof IHandler, "The referred bean should subclass IHandler, but was: "
		+ obj);
	
		final IHandler handler = (IHandler) obj;
	
		return handler;
	}
}
