package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This class provides functionality to let the user provide feedback
 * on the application.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * @author Roland Jansen &lt;jansen@stereokrauts.com&gt;
 *
 */
public final class UserFeedback {
	/**
	 * This function is called when the user wants to provide feedback to
	 * the developers of Stereoscope and sends the data to the
	 * Stereokrauts server.
	 * 
	 * This function includes version number and obtained licenses in
	 * the message, as well as further information. The user must be informed
	 * of transmitted data in the GUI, so update the information the
	 * the view.UserFeedbackView class.
	 * 
	 * @param name The name of the user
	 * @param eMail The email address of the user
	 * @param message The message the user provided
	 * @throws Exception An error occured while transmitting the message.
	 */
	public String provideUserFeedback(final String name, final String eMail, 
			final String message)
	throws Exception {
		
		HttpURLConnection connection = null;
		try {
			String data = URLEncoder.encode("fb_name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
			data += "&" + URLEncoder.encode("fb_email", "UTF-8") + "=" + URLEncoder.encode(eMail, "UTF-8");
			data += "&" + URLEncoder.encode("fb_message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
			
			final URL url = new URL("http://www.stereokrauts.com/feedback-receiver");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			connection.setRequestProperty("Content-Length", ""
					+ Integer.toString(data.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			//Send request
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
			
			//Get Response	
			final InputStream is = connection.getInputStream();
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			final StringBuffer response = new StringBuffer(); 
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		
		} finally {
			if (connection != null) {
		        connection.disconnect();
			}
		}
	}
}
