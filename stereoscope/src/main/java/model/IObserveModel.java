package model;

/**
 * Implementers of this interface may register on the {@link Model}
 * for changes to it's document.
 * @author theide
 *
 */
public interface IObserveModel {
	void newDocumentEvent(Document doc);
}
