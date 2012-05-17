package de.fubatra.archiv.shared.service;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * Public services
 */
public interface AppRequestFactory extends RequestFactory {
	
	LoginServiceRequest loginServiceRequest();
	
	TrainingSessionServiceRequest trainingSessionServiceRequest();
	
	UserServiceRequest userServiceRequest();

}
