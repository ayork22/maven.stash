package com.apm.stash;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetPropertiesFile {

	public static String getPropertyValue(String property) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("stash.properties");

			// load the properties file
			prop.load(input);

		} catch (IOException ex) {
			System.out.println("No Properties File Found");
			ex.printStackTrace();

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop.getProperty(property);
	}

}