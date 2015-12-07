package main.protection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test for the class VersionComparator. */
public final class VersionComparatorTest {
	@Test
	public void isNewerVersion() {
		assertTrue(VersionComparator.isNewerVersion("1", "2"));
		assertTrue(VersionComparator.isNewerVersion("1.0", "1.1"));
		assertTrue(VersionComparator.isNewerVersion("1.0.0", "1.1.0"));
		assertTrue(VersionComparator.isNewerVersion("1.0.0", "1.0.1"));
		assertTrue(VersionComparator.isNewerVersion("1", "1.1"));
		assertTrue(VersionComparator.isNewerVersion("1.7", "1.7.1"));
		
		assertFalse(VersionComparator.isNewerVersion("2", "1"));
		assertFalse(VersionComparator.isNewerVersion("1", "1"));
		assertFalse(VersionComparator.isNewerVersion("1.1", "1"));
		assertFalse(VersionComparator.isNewerVersion("1.0.1", "1.0"));
		assertFalse(VersionComparator.isNewerVersion("1.1.0", "1.0.1"));
	}
}
