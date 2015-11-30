package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import commands.CommandExecutor;

/**
 * This class denotes the core class of the model of stereoscope.
 * 
 * @author theide
 * 
 */
public final class Model implements IModel, ApplicationContextAware {
	private final List<Document> openDocuments = new ArrayList<Document>();
	private final List<IObserveModel> observers = new ArrayList<IObserveModel>();

	private Document focusedDocument;
	private final CommandExecutor executor = new CommandExecutor();
	private ApplicationContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.IModel#createNewDocument()
	 */
	@Override
	public Document createNewDocument() {
		final Document newDocument = context.getBean(Document.class);
		openDocuments.add(newDocument);
		focusedDocument = newDocument;
		fireNewDocument(newDocument);
		return newDocument;
	}

	@Override
	public Document createNewDocumentFromFile(final File file) {
		final Document newDocument = (Document) context.getBean("stereoscope.model.document", file);
		newDocument.setDirty(false);
		openDocuments.add(newDocument);
		focusedDocument = newDocument;
		fireNewDocument(newDocument);
		return newDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.IModel#registerObserver(model.IObserveModel)
	 */
	@Override
	public void registerObserver(final IObserveModel obs) {
		if (!observers.contains(obs)) {
			observers.add(obs);
		}
	}

	public void fireNewDocument(final Document doc) {
		for (final IObserveModel ob : observers) {
			ob.newDocumentEvent(doc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.IModel#getFocusedDocument()
	 */
	@Override
	public Document getFocusedDocument() {
		return focusedDocument;
	}

	@Override
	public CommandExecutor getExecutor() {
		return executor;
	}

	@Override
	public void setApplicationContext(final ApplicationContext context) {
		this.context = context;
	}

	@Override
	public void closeDocument(final Document document) {
		document.shutdown();
		openDocuments.remove(document);
	}

}
