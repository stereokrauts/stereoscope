package stereokrauts.junit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assume;

public class SkipTest {
	/**
	 * Ignore a test until a certain date.
	 *
	 * <p>This method will make the test silently fail
	 * (as if it had been ignored) until a certain
	 * date.
	 *
	 * <p>For documentation purposes, you can give a
	 * message that explains why it's ignored.
	 */
	public static void ignoreUntil(
			final String date_YYYY_MM_DD,
			final String message
			) {
		Date date;
		final SimpleDateFormat format
		= new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			date = format.parse( date_YYYY_MM_DD );
		} catch( final ParseException e ) {
			throw new RuntimeException(
					"Can't parse date ["
							+ date_YYYY_MM_DD
							+ "] using format "
							+ format.toPattern()
					);
		}
		Assume.assumeTrue( new Date().after( date ) );
	}
}
