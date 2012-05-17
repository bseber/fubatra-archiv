package de.fubatra.archiv.server.ioc;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import de.fubatra.archiv.server.service.impl.UploadServiceImpl;
import de.fubatra.archiv.server.servlet.ServeServlet;
import de.fubatra.archiv.server.servlet.UploadServlet;

public class FubatraArchivServletModule extends ServletModule {
	
	public static final String APP_REQUEST_FACTORY_URI = "app/gwtRequest";
	public static final String PUBLIC_REQUEST_FACTORY_URI = "web/gwtRequest";
	
	@Override
	protected void configureServlets() {
		
//		bind(OAuth2CallbackServlet.class).in(Singleton.class);
//		serve("/oauthcallback").with(OAuth2CallbackServlet.class);
		
		bind(PublicRequestFactoryServlet.class).in(Singleton.class);
		serve("/" + PUBLIC_REQUEST_FACTORY_URI).with(PublicRequestFactoryServlet.class);
		
		bind(AppRequestFactoryServlet.class).in(Singleton.class);
		serve("/" + APP_REQUEST_FACTORY_URI).with(AppRequestFactoryServlet.class);
		
		bind(UploadServiceImpl.class).in(Singleton.class);
		serve("/fubatraarchiv/upload").with(UploadServiceImpl.class);
		
		bind(UploadServlet.class).in(Singleton.class);
		serve("/upload").with(UploadServlet.class);
		
		bind(ServeServlet.class).in(Singleton.class);
		serve("/serve").with(ServeServlet.class);
	}

}
