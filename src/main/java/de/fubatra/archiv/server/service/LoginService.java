package de.fubatra.archiv.server.service;

import java.io.IOException;

public interface LoginService {
	
	boolean isUserLoggedIn();
	
	/**
	 * @param destinationURL
	 *            where the user will be redirected after they log in.
	 * @return The URL that will present a login prompt.
	 * @throws IOException 
	 */
	String createLoginUrl(String destinationURL);

	/**
	 * @returns The URL that will log the user out.
	 */
	String createLogoutUrl();

}
