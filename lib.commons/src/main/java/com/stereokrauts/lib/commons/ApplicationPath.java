package com.stereokrauts.lib.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class contains paths to different places that are used throughout
 * the whole application.
 * @author theide
 *
 */
public final class ApplicationPath {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(ApplicationPath.class);

	/**
	 * This field contains the absolute path to the directory where
	 * user data is stored.
	 */
	private static final String USERDATA_PATH = System.getProperty("user.home") + File.separator + ".stereoscope";

	private final File[] storePaths;

	public ApplicationPath() {
		storePaths = new File[] {
				new File(USERDATA_PATH),
				new File(Platform.getInstallLocation().getURL().getPath())
		};
	}

	public ApplicationPath(final File[] applicationPaths) {
		storePaths = applicationPaths;
	}

	public void storeFile(final String filename, final InputStream contents) {
		for (final File storeLocation : storePaths) {
			final File targetFile = new File(storeLocation, filename);
			targetFile.getParentFile().mkdirs();
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(targetFile);
				final ReadableByteChannel input = Channels.newChannel(contents);
				output.getChannel().transferFrom(input, 0, Long.MAX_VALUE);
			} catch (final IOException e) {
				LOGGER.log(Level.WARNING, "Could not store file: ", e);
			} finally {
				IOUtils.closeQuietly(output);
			}
		}
	}

	public Collection<File> readDirectory(final String directoryName) throws FileNotFoundException {
		final List<File> missingAndSyncLocations = new ArrayList<>();
		File foundInLocation = null;
		final ArrayList<File> files = new ArrayList<>();

		for (final File storeLocation : storePaths) {
			LOGGER.info("Inspecting " + storeLocation + " for directory " + directoryName);
			final File targetFile = new File(storeLocation, directoryName);
			if (fileExistsHereAndNoHigherRankedLocationAlreadyMatched(foundInLocation, targetFile)) {
				LOGGER.info("First match for " + directoryName + " found!");
				foundInLocation = targetFile.getParentFile();
				files.addAll(Arrays.asList(targetFile.listFiles()));
			} else {
				missingAndSyncLocations.add(storeLocation);
			}
		}
		if (foundInLocation == null) {
			throw new FileNotFoundException("The directory " + directoryName + " was not found in any of the applications search folders: " + storePaths);
		}
		if (!missingAndSyncLocations.isEmpty()) {
			copyFromFoundToMissing(foundInLocation, directoryName, missingAndSyncLocations);
		}
		
		LOGGER.info("Found " + files + " in " + foundInLocation + " subdirectory " + directoryName);
		
		return files;
	}

	public InputStream readFile(final String filename) throws FileNotFoundException {
		final List<File> missingInLocations = new ArrayList<>();
		File foundInLocation = null;
		FileInputStream stream = null;

		for (final File storeLocation : storePaths) {
			final File targetFile = new File(storeLocation, filename);
			if (fileExistsHereAndNoHigherRankedLocationAlreadyMatched(foundInLocation, targetFile)) {
				LOGGER.info("Requested file " + filename + " found in " + storeLocation);
				foundInLocation = targetFile.getParentFile();
				stream = new FileInputStream(targetFile);
			} else if (fileDoesNotExistHere(targetFile)) {
				missingInLocations.add(storeLocation);
			}
		}
		if (fileIsMissingInSomeFolders(missingInLocations, foundInLocation)) {
			copyFromFoundToMissing(foundInLocation, filename, missingInLocations);
		}
		if (foundInLocation == null) {
			throw new FileNotFoundException("The file " + filename + " was not found in any of the applications search folders: " + storePaths);
		}
		return stream;
	}

	private boolean fileIsMissingInSomeFolders(final List<File> missingInLocations, final File foundInLocation) {
		return !missingInLocations.isEmpty() && foundInLocation != null;
	}

	private boolean fileDoesNotExistHere(final File targetFile) {
		return ! targetFile.exists();
	}

	private boolean fileExistsHereAndNoHigherRankedLocationAlreadyMatched(
			final File foundInLocation, final File targetFile) {
		return targetFile.exists() && foundInLocation == null;
	}

	private void copyFromFoundToMissing(final File foundInLocation, final String targetName, final List<File> missingInLocations) {
		LOGGER.info("Found " + targetName + " in " + foundInLocation + " but not in " + missingInLocations + " - starting distribution...");
		final File sourceFile = new File(foundInLocation, targetName);
		final Path foundInLocationPath = foundInLocation.toPath();
		
		for (final File missingLocation : missingInLocations) {
			final Path missingLocationPath = missingLocation.toPath();
				try {
					if (sourceFile.isDirectory()) {
					Files.walkFileTree(sourceFile.toPath(), new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							final Path relativeFile = foundInLocationPath.relativize(file);
							LOGGER.info("Copying file " + relativeFile + " from " + file + " to " + missingLocationPath);
							try {
								Files.copy(file, missingLocationPath.resolve(relativeFile), StandardCopyOption.REPLACE_EXISTING);
							} catch (final IOException e) {
								LOGGER.log(Level.WARNING, "Could not copy missing file " + relativeFile + " from " + file + " to " + missingLocationPath, e);
							}
							return super.visitFile(file, attrs);
						}
						
						@Override
						public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
							final Path relativeDirectory = foundInLocationPath.relativize(dir);
							try {
								Path resolvedTargetDirectory = missingLocationPath.resolve(relativeDirectory);
								if (!resolvedTargetDirectory.toFile().exists()) {
									LOGGER.info("Creating directory " + resolvedTargetDirectory);
									Files.createDirectory(resolvedTargetDirectory);
								}
						} catch (final IOException e) {
							LOGGER.log(Level.WARNING, "Could not create directory " + relativeDirectory + " in " + missingLocationPath, e);
						}
							return super.preVisitDirectory(dir, attrs);
						}
						
					});
					} else {
						Files.copy(sourceFile.toPath(), missingLocationPath.resolve(targetName), StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "RECURSIVE ERROR - Could not copy missing file from " + foundInLocation + " to " + missingLocation, e);
				}
		}

	}

}
