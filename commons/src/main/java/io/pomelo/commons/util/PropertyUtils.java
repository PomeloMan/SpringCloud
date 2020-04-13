package io.pomelo.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

	private static final Properties properties = new Properties();

	static {
		InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream("config.properties");
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getProperty(String param) {
		return properties.getProperty(param);
	}
}
