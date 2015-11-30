package com.stereokrauts.lib.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ApplicationPathTest {

	private static final String HOME_FOLDER = "home";
	private static final String APP_FOLDER = "app";
	private static final String TEST_FOLDER = "test";
	private static final String TEST_SUB_FOLDER = "sub";

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private File home;
	private File app;
	private ApplicationPath path;

	@Before
	public void setup() throws Exception {
		folder.create();
		home = folder.newFolder(HOME_FOLDER);
		app = folder.newFolder(APP_FOLDER);
	}

	@Test
	public void testSync() throws Exception {
		new File(home, TEST_FOLDER).createNewFile();

		path = new ApplicationPath(new File[] { home, app });
		path.readFile(TEST_FOLDER);
		assertTrue(new File(app, TEST_FOLDER).exists());
	}

	@Test
	public void testSyncInverse() throws Exception {
		new File(app, TEST_FOLDER).createNewFile();

		path = new ApplicationPath(new File[] { home, app });
		path.readFile(TEST_FOLDER);
		assertTrue(new File(home, TEST_FOLDER).exists());
	}

	@Test
	public void testCopy_worksOnCompleteDirectories() throws Exception {
		File testFolderSource = folder.newFolder(HOME_FOLDER, TEST_FOLDER, TEST_SUB_FOLDER);
		File testFolderTarget = folder.newFolder(APP_FOLDER, TEST_FOLDER, TEST_SUB_FOLDER);
		
		new File(testFolderSource, "testFile.txt").createNewFile();
		
		path = new ApplicationPath(new File[] { home, app });
		path.readDirectory(TEST_FOLDER);
		
		assertTrue(new File(testFolderTarget, "testFile.txt").exists());
	}

	@Test
	public void testSync_firstFolderIsStronger() throws Exception {
		final File homeFile = new File(home, TEST_FOLDER);
		final File appFile = new File(app, TEST_FOLDER);

		writeFileContents(homeFile, "test1");
		writeFileContents(appFile, "test2");

		path = new ApplicationPath(new File[] { home, app });
		final InputStream inputStream = path.readFile(TEST_FOLDER);
		final String contents = IOUtils.toString(inputStream);
		assertEquals("test1", contents);
	}

	private void writeFileContents(final File file, final String contents) throws IOException {
		final FileWriter writer = new FileWriter(file);
		writer.append(contents);
		writer.close();
	}
}
