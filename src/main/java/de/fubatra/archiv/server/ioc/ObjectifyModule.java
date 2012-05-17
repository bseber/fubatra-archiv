package de.fubatra.archiv.server.ioc;

import com.google.inject.AbstractModule;
import com.googlecode.objectify.ObjectifyService;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.domain.TrainingSessionPost;
import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.domain.UserInfoSeason;

public class ObjectifyModule extends AbstractModule {

	@Override
	protected void configure() {
		ObjectifyService.register(UserInfo.class);
		ObjectifyService.register(UserInfoSeason.class);
		ObjectifyService.register(TrainingSession.class);
		ObjectifyService.register(TrainingSessionPost.class);
	}

}
