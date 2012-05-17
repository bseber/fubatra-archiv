package de.fubatra.archiv.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.fubatra.archiv.server.ioc.FubatraArchivServiceLocator;
import de.fubatra.archiv.server.service.FeedbackService;

@Service(value = FeedbackService.class, locator = FubatraArchivServiceLocator.class)
public interface FeedbackServiceSecured extends RequestContext {
	
	Request<Boolean> sendFeedback(String message);
	
}
