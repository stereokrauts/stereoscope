package model.persistance;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.beans.DocumentBean;

/**
 * Saves a document to persistance (e.g. a hard drive).
 * @author th
 *
 */
public final class DocumentSaver {
	private JAXBContext context;

	public DocumentSaver() throws DocumentException {
		try {
			this.context = JAXBContext.newInstance(DocumentBean.class);
		} catch (final JAXBException e) {
			throw new DocumentException(e);
		}
	}

	public void internalSaveDocument(final DocumentBean d, final OutputStream saveTo) throws DocumentException {
		try {
			final Marshaller marshaller = this.context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(d, saveTo);
		} catch (final JAXBException e) {
			throw new DocumentException(e);
		}
	}

	public static void save(final DocumentBean d, final OutputStream saveTo) throws DocumentException {
		new DocumentSaver().internalSaveDocument(d, saveTo);
	}
}
