package de.fubatra.archiv.server.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class AppServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new FubatraArchivServletModule(), new FubatraArchivModule(), new ObjectifyModule());
	}

}
