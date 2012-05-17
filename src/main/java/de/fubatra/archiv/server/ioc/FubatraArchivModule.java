package de.fubatra.archiv.server.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

import de.fubatra.archiv.server.service.FeedbackService;
import de.fubatra.archiv.server.service.LoginService;
import de.fubatra.archiv.server.service.TrainingSessionService;
import de.fubatra.archiv.server.service.UserInfoService;
import de.fubatra.archiv.server.service.impl.FeedbackServiceImpl;
import de.fubatra.archiv.server.service.impl.LoginServiceImpl;
import de.fubatra.archiv.server.service.impl.TrainingSessionServiceImpl;
import de.fubatra.archiv.server.service.impl.UserInfoServiceImpl;

public class FubatraArchivModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
		bind(ServiceLayerDecorator.class).to(PublicRequestFactorySLD.class);
		bind(FubatraArchivServiceLocator.class);
		
		bind(FeedbackService.class).to(FeedbackServiceImpl.class).in(Singleton.class);
		bind(LoginService.class).to(LoginServiceImpl.class).in(Singleton.class);
		bind(TrainingSessionService.class).to(TrainingSessionServiceImpl.class).in(Singleton.class);
		bind(UserInfoService.class).to(UserInfoServiceImpl.class).in(Singleton.class);
	}

}
