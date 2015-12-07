package model.persistance;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.beans.DocumentBean;

/**
 * Loads a document from persistance (e.g. hard drive).
 * @author th
 *
 */
public final class DocumentLoader {
	private JAXBContext context;
	private final InputStream fileLocation;

	private DocumentLoader(final InputStream fileLocation) throws DocumentException {
		this.fileLocation = fileLocation;
		try {
			this.context = JAXBContext.newInstance(DocumentBean.class);
		} catch (final JAXBException e) {
			throw new DocumentException(e);
		}
	}

	public static DocumentBean load(final InputStream fileLocation) throws DocumentException {
		return (new DocumentLoader(fileLocation)).getDocument();
	}

	private DocumentBean getDocument() throws DocumentException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = this.context.createUnmarshaller();
			//unmarshaller.setSchema(arg0);
			return (DocumentBean) unmarshaller.unmarshal(this.fileLocation);
		} catch (final Exception e) {
			throw new DocumentException(e);
		}
	}
}
