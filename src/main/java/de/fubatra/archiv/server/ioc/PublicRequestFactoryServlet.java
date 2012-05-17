package de.fubatra.archiv.server.ioc;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public class PublicRequestFactoryServlet extends RequestFactoryServlet {

	private static final long serialVersionUID = -4011059080051620220L;

	@Inject
	public PublicRequestFactoryServlet(DefaultExceptionHandler exceptionHandler, PublicRequestFactorySLD serviceHandler, CalculatedSLD calculatedHandler) {
		super(exceptionHandler, serviceHandler, calculatedHandler);
	}
	
}
