package de.fubatra.archiv.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TrainingFilterChangedEvent extends GwtEvent<TrainingFilterChangedEvent.Handler> {
	
	public static Type<TrainingFilterChangedEvent.Handler> TYPE = new Type<TrainingFilterChangedEvent.Handler>();
	
	public static void fire(HasTrainingFilterChangedEventHandlers source, TrainingFilterChangedEvent event) {
		source.fireEvent(event);
	}
	
	public interface Handler extends EventHandler {
		
		void onTrainingSessionFilterChanged(TrainingFilterChangedEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onTrainingSessionFilterChanged(this);
	}
	
}
