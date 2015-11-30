package model;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;

import commands.document.NewDocumentCommand;

/**
 * This class tests the class Model.
 * @author theide
 *
 */
public class ModelTest {
	
	@Ignore
	public final void newDocumentCreation() throws Exception {
		final Model myModel = new Model();
		final NewDocumentCommand cmd = new NewDocumentCommand(myModel);
		cmd.execute();
		assertNotNull(myModel.getFocusedDocument());
	}
}
