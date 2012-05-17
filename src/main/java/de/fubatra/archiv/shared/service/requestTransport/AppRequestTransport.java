package de.fubatra.archiv.shared.service.requestTransport;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

import de.fubatra.archiv.server.ioc.FubatraArchivServletModule;

public class AppRequestTransport extends DefaultRequestTransport {
	
	public AppRequestTransport() {
		String requesturl = GWT.getHostPageBaseURL() + FubatraArchivServletModule.APP_REQUEST_FACTORY_URI;
		setRequestUrl(requesturl);
	}
	
}
