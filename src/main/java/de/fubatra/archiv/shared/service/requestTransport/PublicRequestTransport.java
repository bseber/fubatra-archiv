package de.fubatra.archiv.shared.service.requestTransport;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

import de.fubatra.archiv.server.ioc.FubatraArchivServletModule;

public class PublicRequestTransport extends DefaultRequestTransport {
	
	public PublicRequestTransport() {
		String requesturl = GWT.getHostPageBaseURL() + FubatraArchivServletModule.PUBLIC_REQUEST_FACTORY_URI;
		setRequestUrl(requesturl);
	}

}
