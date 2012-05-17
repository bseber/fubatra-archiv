package de.fubatra.archiv.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * Guiced {@link ServiceLayerDecorator} for the {@link RequestFactory}
 *
 */
public class PublicRequestFactorySLD extends ServiceLayerDecorator {
	
	private final Injector injector;
	
	@Inject
	protected PublicRequestFactorySLD(Injector injector) {
		super();
		this.injector = injector;
	}

	@Override
	public <T extends ServiceLocator> T createServiceLocator(Class<T> clazz) {
		return injector.getInstance(clazz);
	}
	
}
