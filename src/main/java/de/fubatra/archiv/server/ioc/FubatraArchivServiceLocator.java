package de.fubatra.archiv.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * Guiced {@link ServiceLocator} for the {@link RequestFactory}
 *
 */
public class FubatraArchivServiceLocator implements ServiceLocator {

	private final Injector injector;
	
	@Inject
	public FubatraArchivServiceLocator(Injector injector) {
		this.injector = injector;
	}

	@Override
	public Object getInstance(Class<?> clazz) {
		return injector.getInstance(clazz);
	}

}
