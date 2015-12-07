package model.persistance;

/**
 * Generic exception class for exceptions that relate to document operations.
 * @author th
 *
 */
public class DocumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6791868633016631605L;

	public DocumentException(final Exception e) {
		super(e);
	}

}
