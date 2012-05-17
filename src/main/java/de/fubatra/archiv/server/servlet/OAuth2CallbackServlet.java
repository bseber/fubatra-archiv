//package de.fubatra.archiv.server.servlet;
//
//import java.io.IOException;
//import java.util.logging.Logger;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.http.GenericUrl;
//import com.google.api.client.http.HttpRequest;
//import com.google.api.client.http.HttpRequestFactory;
//import com.google.api.client.http.HttpResponse;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.gson.Gson;
//import com.google.inject.Inject;
//
//import de.fubatra.archiv.server.domain.GoogleUserInfo;
//import de.fubatra.archiv.server.domain.UserInfo;
//import de.fubatra.archiv.server.service.UserService;
//import de.fubatra.archiv.server.util.OAuthConfigHelper;
//
//public class OAuth2CallbackServlet extends HttpServlet {
//
//	private static final long serialVersionUID = -9102413932106026942L;
//
//	private static final Logger LOG = Logger.getLogger(OAuth2CallbackServlet.class.getName());
//
//	private static final String USERINFO_URI = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
//	
//	private final UserService userService;
//
//	@Inject
//	public OAuth2CallbackServlet(UserService userService) {
//		this.userService = userService;
//	}
//	
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		doProcess(req, resp);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		doProcess(req, resp);
//	}
//
//	private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		String error = req.getParameter("error");
//		if (error != null) {
//			LOG.severe("There was a problem during authentication: " + error);
//			redirectUser(req, resp);
//			return;
//		}
//
//		String code = req.getParameter("code");
//
//		if (code == null || code.isEmpty()) {
//			LOG.severe("OAuth code must not be null or empty");
//			redirectUser(req, resp);
//			return;
//		}
//		
//		LOG.info("Exchanging OAuth code for access token using server side call");
//		
//		String clientId = OAuthConfigHelper.getClientId();
//		String redirectUri = OAuthConfigHelper.getRedirectUri();
//
//		String clientSecret = OAuthConfigHelper.getClientSecret();
//		GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(), clientId,
//				clientSecret, code, redirectUri);
//		GoogleTokenResponse tokenResponse = tokenRequest.execute();
//		String accessToken = tokenResponse.getAccessToken();
//		
//		LOG.info("Update user profile info");
//
//		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(null);
//		GenericUrl url = new GenericUrl(USERINFO_URI + accessToken);
//		HttpRequest request = requestFactory.buildGetRequest(url);
//		HttpResponse response = request.execute();
//		String userInfoResult = response.parseAsString();
//		
//		LOG.info("Map and save new user info [" + userInfoResult +"]");
//		
//		GoogleUserInfo googleUserInfo = new Gson().fromJson(userInfoResult, GoogleUserInfo.class);
//		UserInfo userInfo = userService.getUserByGoogleUserId(googleUserInfo.getId());
//		if (userInfo == null) {
//			userInfo = new UserInfo();
//			userInfo.setGoogleUserId(googleUserInfo.getId());
//		}
//		userInfo.setEmail(googleUserInfo.getEmail());
//		userInfo.setName(googleUserInfo.getName());
//		userInfo.setPictureUrl(googleUserInfo.getPicture());
//		userService.save(userInfo);
//		
//		LOG.info("updated user info of " + googleUserInfo.getEmail());
//
//		redirectUser(req, resp);
//	}
//	
//	private void redirectUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		String state = req.getParameter("state");
//		if (state == null) {
//			state = "/";
//		}
//		Cookie[] cookies = req.getCookies();
//		for (Cookie c : cookies) {
//			resp.addCookie(c);
//		}
//		resp.sendRedirect(state);
//	}
//
//}