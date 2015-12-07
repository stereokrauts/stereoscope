package model;

import java.io.File;

import commands.CommandExecutor;

/**
 * Service interface to the stereoscope core model.
 * @author th
 *
 */
public interface IModel {
	Document createNewDocument();
	void registerObserver(IObserveModel obs);
	Document getFocusedDocument();
	CommandExecutor getExecutor();
	Document createNewDocumentFromFile(File file);
	void closeDocument(Document document);
}