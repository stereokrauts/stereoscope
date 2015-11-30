package com.stereokrauts.stereoscope.gui.document;

import model.Document;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class EditableDocument implements IEditorInput {
	private final Document document;

	public EditableDocument(final Document doc) {
		this.document = doc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "Stereoscope Document Editor";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Editor for a Stereoscope Document";
	}

	public Document getDocument() {
		return this.document;
	}

}
