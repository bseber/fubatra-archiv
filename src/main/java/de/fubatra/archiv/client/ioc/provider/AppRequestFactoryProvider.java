package de.fubatra.archiv.client.ioc.provider;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.requestTransport.AppRequestTransport;

public class AppRequestFactoryProvider implements Provider<AppRequestFactorySecured> {

	private static AppRequestFactorySecured authRequestFactory;

	private final EventBus gwtEventBus;
	private final AppRequestTransport requestTransport;
	
	@Inject
	public AppRequestFactoryProvider(EventBus gwtEventBus, AppRequestTransport requestTransport) {
		this.gwtEventBus = gwtEventBus;
		this.requestTransport = requestTransport;
	}

	@Override
	public AppRequestFactorySecured get() {
		if (authRequestFactory == null) {
			authRequestFactory = GWT.create(AppRequestFactorySecured.class);
			authRequestFactory.initialize(gwtEventBus, requestTransport);
		}
		return authRequestFactory;
	}
	
}
