package de.fubatra.archiv.server.ioc;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public class AppRequestFactoryServlet extends RequestFactoryServlet {

	private static final long serialVersionUID = -3185626083476001438L;

	@Inject
	public AppRequestFactoryServlet(DefaultExceptionHandler exceptionHandler, AppRequestFactorySLD serviceHandler, CalculatedSLD calculatedHandler) {
		super(exceptionHandler, serviceHandler, calculatedHandler);
	}

}
