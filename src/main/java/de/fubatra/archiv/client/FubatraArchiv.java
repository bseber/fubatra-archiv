package de.fubatra.archiv.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.mvp4g.client.Mvp4gModule;

public class FubatraArchiv implements EntryPoint {

	public void onModuleLoad() {
		Mvp4gModule module = (Mvp4gModule) GWT.create( Mvp4gModule.class );
		module.createAndStartModule();
		// view will be set when knowing if user is logged in or not
		// (checked in MenuPresenter)
	}
	
}
