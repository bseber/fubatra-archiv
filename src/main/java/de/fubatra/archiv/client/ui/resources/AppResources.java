package de.fubatra.archiv.client.ui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AppResources extends ClientBundle {
	
	public static final String ERROR_BG_COLOR = "#F88";
	
	@Source("icons/cancel16.png")
	ImageResource cancel16();
	
	@Source("icons/folder.png")
	ImageResource currentUsersTrainingSessions();
	
	@Source("icons/delete16.png")
	ImageResource delete16();
	
	@Source("icons/delete24.png")
	ImageResource delete24();
	
	@Source("icons/create.png")
	ImageResource document();
	
	@Source("icons/edit16.png")
	ImageResource edit16();
	
	@Source("icons/edit24.png")
	ImageResource edit24();
	
	@Source("icons/star_empty_24.png")
	ImageResource emptyStar();
	
	@Source("icons/star_empty_16.png")
	ImageResource emptyStar16();
	
	@Source("icons/home.png")
	ImageResource home();
	
	@Source("icons/info.png")
	ImageResource info();

	@Source("loading-circle.gif")
	ImageResource loadingCircle();
	
	@Source("icons/login.png")
	ImageResource login();
	
	@Source("icons/logout.png")
	ImageResource logout();
	
	@Source("icons/user.png")
	ImageResource man();
	
	@Source("placeholder-man.jpg")
	ImageResource placeholderMan();
	
	@Source("icons/save16.png")
	ImageResource save16();
	
	@Source("icons/star_24.png")
	ImageResource star();
	
	@Source("icons/star_16.png")
	ImageResource star16();
	
}
