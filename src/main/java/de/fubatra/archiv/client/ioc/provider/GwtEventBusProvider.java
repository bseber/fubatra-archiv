package de.fubatra.archiv.client.ioc.provider;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class GwtEventBusProvider implements Provider<EventBus> {
	
	private static EventBus gwtEventBus;

	@Override
	public EventBus get() {
		if (gwtEventBus == null) {
			gwtEventBus = GWT.create(SimpleEventBus.class);
		}
		return gwtEventBus;
	}
	
	

}
