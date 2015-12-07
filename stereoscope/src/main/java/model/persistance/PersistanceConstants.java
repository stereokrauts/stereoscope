package model.persistance;

/**
 * A class holding constants for the persistance subsystem.
 * @author th
 *
 */
final class PersistanceConstants {
	private PersistanceConstants() { }

	private static final String SCHEMA_NAME = "resources/DocumentSchema.xsd";

	public static String getSchemaLocation() {
		return SCHEMA_NAME; // Activator.getContext().getBundle().schemaName;
	} 
}
