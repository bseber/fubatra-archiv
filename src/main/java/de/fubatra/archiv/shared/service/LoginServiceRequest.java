package de.fubatra.archiv.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.fubatra.archiv.server.ioc.FubatraArchivServiceLocator;
import de.fubatra.archiv.server.service.LoginService;

@Service(value = LoginService.class, locator = FubatraArchivServiceLocator.class)
public interface LoginServiceRequest extends RequestContext {
	
	Request<String> createLoginUrl(String destinationUrl);
	
	Request<String> createLogoutUrl();

}
