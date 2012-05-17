package de.fubatra.archiv.shared.service;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * This services are only accessible for logged in users. For public services
 * use {@link AppRequestFactory}.
 * 
 */
public interface AppRequestFactorySecured extends RequestFactory {

	FeedbackServiceSecured feedbackService();
	
	UserServiceRequestSecured userServiceRequestSecured();
	
}
