package de.fubatra.archiv.client.ioc;

import java.util.ArrayList;

import com.mvp4g.client.event.EventFilter;

import de.fubatra.archiv.client.util.UserUtil;

public class AppEventFilter implements EventFilter<AppEventBus> {

	private static final ArrayList<String> protectedEvents;
	
	static {
		protectedEvents = new ArrayList<String>();
		protectedEvents.add("createNewSeason");
		protectedEvents.add("createTrainingSession");
		protectedEvents.add("initFeedbackWidget");
		protectedEvents.add("listTrainingSessionsOfUser");
	}
	
	@Override
	public boolean filterEvent(String eventName, Object[] params, AppEventBus eventBus) {
		boolean forward = true;
		
		if (protectedEvents.contains(eventName)) {
			forward = UserUtil.isUserLoggedIn();
		}
		
		if (!forward) {
			eventBus.setFilteringEnabled(false);
			eventBus.notAuthorized();
			eventBus.setFilteringEnabled(true);
		}
		
		return forward;
	}

}
