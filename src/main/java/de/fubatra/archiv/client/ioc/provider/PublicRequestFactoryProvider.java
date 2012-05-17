package de.fubatra.archiv.client.ioc.provider;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.requestTransport.PublicRequestTransport;

public class PublicRequestFactoryProvider implements Provider<AppRequestFactory> {

	private static AppRequestFactory requestFactory;

	private final EventBus gwtEventBus;
	private final PublicRequestTransport transport;
	
	@Inject
	public PublicRequestFactoryProvider(EventBus gwtEventBus, PublicRequestTransport transport) {
		this.gwtEventBus = gwtEventBus;
		this.transport = transport;
	}

	@Override
	public AppRequestFactory get() {
		if (requestFactory == null) {
			requestFactory = GWT.create(AppRequestFactory.class);
			requestFactory.initialize(gwtEventBus, transport);
		}
		return requestFactory;
	}
	
}
