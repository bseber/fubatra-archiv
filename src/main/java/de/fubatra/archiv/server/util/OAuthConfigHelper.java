//package de.fubatra.archiv.server.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.Properties;
//import java.util.logging.Logger;
//
//public class OAuthConfigHelper {
//
//	private static final Logger log = Logger.getLogger(OAuthConfigHelper.class.getName());
//	private static final String CONFIG_PROPERTIES = "/config.properties";
//	private static final Properties config;
//	
//	static {
//		config = new Properties();
//		try {
//			InputStream input = OAuthConfigHelper.class.getResourceAsStream(CONFIG_PROPERTIES);
//			config.load(input);
//		} catch (IOException e) {
//			throw new RuntimeException("Unable to load config file: " + CONFIG_PROPERTIES);
//		}
//	}
//
//	private static String CLIENT_ID;
//	private static String CLIENT_SECRET;
//	private static String GOOGLE_API_KEY;
//	private static Iterable<String> SCOPES;
//	private static String REDIRECT_URI;
//
//	public static String getClientId() {
//		if (CLIENT_ID == null) {
//			CLIENT_ID = OAuthConfigHelper.getProperty("oauth_client_id");
//		}
//		return CLIENT_ID;
//	}
//	
//	public static String getClientSecret() {
//		if (CLIENT_SECRET == null) {
//			CLIENT_SECRET = OAuthConfigHelper.getProperty("oauth_client_secret");
//		}
//		return CLIENT_SECRET;
//	}
//	
//	public static String getGoogleApiKey() {
//		if (GOOGLE_API_KEY == null) {
//			GOOGLE_API_KEY = OAuthConfigHelper.getProperty("google_api_key");
//		}
//		return GOOGLE_API_KEY;
//	}
//
//	public static String getRedirectUri() {
//		if (REDIRECT_URI == null) {
//			REDIRECT_URI = OAuthConfigHelper.getProperty("oauth_redirect_uri");
//		}
//		return REDIRECT_URI;
//	}
//	
//	public static Iterable<String> getScopes() {
//		if (SCOPES == null) {
//			String property = OAuthConfigHelper.getProperty("oauth_scopes");
//			SCOPES = Arrays.asList(property.split(" "));
//		}
//		return SCOPES;
//	}
//	
//	private static String getProperty(String key) {
//		if (!config.containsKey(key) || config.getProperty(key).trim().isEmpty()) {
//			log.severe("Could not find property " + key);
//			throw new RuntimeException("Could not find property " + key);
//		}
//		return config.getProperty(key).trim();
//	}
//
//}
