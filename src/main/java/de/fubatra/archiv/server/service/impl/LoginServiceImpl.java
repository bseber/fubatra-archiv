package de.fubatra.archiv.server.service.impl;

import com.google.appengine.api.users.UserServiceFactory;

import de.fubatra.archiv.server.service.LoginService;

public class LoginServiceImpl implements LoginService {

	@Override
	public boolean isUserLoggedIn() {
		return UserServiceFactory.getUserService().isUserLoggedIn();
	}

	@Override
	public String createLoginUrl(String destinationUrl) {
		return UserServiceFactory.getUserService().createLoginURL(destinationUrl);
	}

	@Override
	public String createLogoutUrl() {
		return UserServiceFactory.getUserService().createLogoutURL("/");
	}

}
