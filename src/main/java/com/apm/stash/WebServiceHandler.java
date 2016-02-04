    package com.apm.stash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class WebServiceHandler {

	/**
	 * @param metrics
	 *            = JSON formatted metrics
	 * @param hostname
	 *            = EPA Host
	 * @param port
	 *            = EPA Port
	 * @return String = is what we're returning - Metrics posted successfully or
	 *         NOT
	 */
	public static String sendMetric(String metrics, String hostname, int port) {

		String output = "";
		try {

			// Pass JSON File Data to REST Service
			try {
				URL url = new URL("http://" + hostname + ":" + Integer.toString(port) + "/apm/metricFeed");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(metrics.toString());
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				// *****This returns output from EPAgent after sending it

				while ((output = in.readLine()) != null) {
					output = output + "\n";
				}
			//	System.out.println(output);

				Calendar cal = Calendar.getInstance();
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                
             	System.out.println("\nMetrics Posted Successfully TIME = " + time.format(cal.getTime()));
			//	System.out.println("\nMetrics Posted Successfully..");
				in.close();
				StashWrapper.logger.debug("Metrics Posted Successfully..");

			} catch (Exception e) {
				// System.out.println("\nError while Posting Metrics");
				// System.out.println(e);
				StashWrapper.logger.error("Error while Posting Metrics : " + e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			StashWrapper.logger.error(e);
		}
		// returning output from EPAgent
		return output;
	}

	/**
	 * Get JSON data from WebService Provider
	 * 
	 * @param WebServiceURL
	 *            = WebService URL
	 * @param UserName
	 *            = UserName
	 * @param UserPassword
	 *            = User Password
	 * @return = JSON in STRING format
	 */
	public static String callURL(String WebServiceURL, String UserName, String UserPassword) {

		StringBuilder sb = new StringBuilder();
		try {

			String authString = UserName + ":" + UserPassword;

			String authStringEnc = new BASE64Encoder().encode(authString.getBytes());

			URL url = new URL(WebServiceURL);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			StashWrapper.logger.error(e + " - Can't connect to BitBucket Server");
		} catch (IOException e) {
			e.printStackTrace();
			StashWrapper.logger.error(e + " - Can't connect to BitBucket Server");
		}
		return sb.toString();
	}
}
