package de.fubatra.archiv.shared.domain;

import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.fubatra.archiv.server.domain.TrainingSessionPost;
import de.fubatra.archiv.server.ioc.ObjectifyEntityLocator;

@ProxyFor(value = TrainingSessionPost.class, locator = ObjectifyEntityLocator.class)
public interface TrainingSessionPostProxy extends EntityProxy {
	
	public static class KeyProvider implements ProvidesKey<TrainingSessionPostProxy> {
		@Override
		public Object getKey(TrainingSessionPostProxy item) {
			return item.getId();
		}
		
	}
	
	Long getId();
	
	TrainingSessionProxy getTrainingSession();
	
	void setTrainingSession(TrainingSessionProxy trainingSession);
	
	UserInfoProxy getUserInfo();
	
	void setUserInfo(UserInfoProxy user);
	
	String getText();
	
	void setText(String text);
	
	Date getCreationDate();
	
	Date getLastEditedDate();

}
