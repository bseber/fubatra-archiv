//package de.fubatra.archiv.server.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.logging.Logger;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
//import com.google.api.client.extensions.appengine.http.urlfetch.UrlFetchTransport;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.http.GenericUrl;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;
//import com.google.base.Preconditions;
//
//public class Utils {
//
//	private static final Logger LOG = Logger.getLogger(Utils.class.getName());
//	
//	private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();
//	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
//
//	private static final String JSON_RESOURCE = "/client_secrets.json";
//
//	private static final String PROFILE_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
//	private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
//
//	private static GoogleClientSecrets clientSecrets = null;
//
//	public static GoogleClientSecrets getClientCredential() throws IOException {
//		if (clientSecrets == null) {
//			InputStream inputStream = Utils.class.getResourceAsStream(JSON_RESOURCE);
//			Preconditions.checkNotNull(inputStream, "missing resource %s", JSON_RESOURCE);
//			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, inputStream);
//		}
//		return clientSecrets;
//	}
//
//	public static String getRedirectUri(HttpServletRequest req) {
//		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
//		url.setRawPath("/oauthcallback");
//		return url.build();
//	}
//
//	public static GoogleAuthorizationCodeFlow newFlow() throws IOException {
//		return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, getClientCredential(), Arrays.asList(PROFILE_SCOPE, EMAIL_SCOPE))
//				.setCredentialStore(new AppEngineCredentialStore()).build();
//	}
//
//	// static Calendar loadCalendarClient() throws IOException {
//	// String userId =
//	// UserServiceFactory.getUserService().getCurrentUser().getUserId();
//	// Credential credential = newFlow().loadCredential(userId);
//	// return Calendar.builder(HTTP_TRANSPORT,
//	// JSON_FACTORY).setHttpRequestInitializer(credential).build();
//	// }
//
//	/**
//	 * Returns an {@link IOException} (but not a subclass) in order to work
//	 * around restrictive GWT serialization policy.
//	 */
////	public static IOException wrappedIOException(IOException e) {
////		if (e.getClass() == IOException.class) {
////			return e;
////		}
////		return new IOException(e.getMessage());
////	}
//
//	public static String getClientId() throws IOException {
//		String clientId = getClientCredential().getWeb().getClientId();
//		LOG.info("clientId: " + clientId);
//		return clientId;
//	}
//
//	public static String getClientSecret() throws IOException {
//		String clientSecret = getClientCredential().getWeb().getClientSecret();
//		LOG.info("clientSecret: " + clientSecret);
//		return clientSecret;
//	}
//
//	public static Iterable<String> getScopes() {
//		return Arrays.asList(PROFILE_SCOPE, EMAIL_SCOPE);
//	}
//
//}
