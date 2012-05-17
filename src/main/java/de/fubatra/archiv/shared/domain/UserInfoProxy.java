package de.fubatra.archiv.shared.domain;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.ioc.ObjectifyEntityLocator;

@ProxyFor(value = UserInfo.class, locator = ObjectifyEntityLocator.class)
public interface UserInfoProxy extends EntityProxy {
	
	public class KeyProvider implements ProvidesKey<UserInfoProxy> {

		@Override
		public Object getKey(UserInfoProxy item) {
			return item.getId();
		}
		
	}
	
	Long getId();

	String getEmail();
	
	String getGoogleUserId();
	
	void setGoogleUserId(String userId);
	
	String getName();
	
	void setName(String name);
	
	String getGooglePlusUrl();
	
	void setGooglePlusUrl(String url);
	
	String getFacebookUrl();
	
	void setFacebookUrl(String url);

	BlobImageProxy getPicture();
	
	void setPicture(BlobImageProxy picture);
	
	boolean isProfileInitiated();
	
}
