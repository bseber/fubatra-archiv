package de.fubatra.archiv.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.web.bindery.event.shared.EventBus;

import de.fubatra.archiv.client.ioc.provider.AppRequestFactoryProvider;
import de.fubatra.archiv.client.ioc.provider.PublicRequestFactoryProvider;
import de.fubatra.archiv.client.ioc.provider.GwtEventBusProvider;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;

public class AppClientGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(EventBus.class).toProvider(GwtEventBusProvider.class);
		bind(AppRequestFactorySecured.class).toProvider(AppRequestFactoryProvider.class);
		bind(AppRequestFactory.class).toProvider(PublicRequestFactoryProvider.class);
	}
	
}
