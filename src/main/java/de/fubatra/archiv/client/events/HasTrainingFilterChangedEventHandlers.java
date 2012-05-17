package de.fubatra.archiv.client.events;

import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface HasTrainingFilterChangedEventHandlers extends HasHandlers {
	
	HandlerRegistration addTrainingFilterChangedHandler(TrainingFilterChangedEvent.Handler handler);

}
