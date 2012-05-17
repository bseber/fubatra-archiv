package de.fubatra.archiv.shared.domain;

import java.util.Date;
import java.util.List;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.ioc.ObjectifyEntityLocator;

@ProxyFor(value = TrainingSession.class, locator = ObjectifyEntityLocator.class)
public interface TrainingSessionProxy extends EntityProxy {

	public static class KeyProvider implements ProvidesKey<TrainingSessionProxy> {
		@Override
		public Object getKey(TrainingSessionProxy item) {
			return item.getId();
		}
		
	}
	
	Long getId();

	UserInfoProxy getOwner();
	
	void setOwner(UserInfoProxy owner);

	TrainingSessionVisiblity getVisibility();
	
	void setVisibility(TrainingSessionVisiblity visibility);
	
	String getDraftBlobKey();
	
	void setDraftBlobKey(String blobKeyString);
	
	String getDraftUrl();
	
	void setDraftUrl(String draftUrl);
	
	String getDescription();
	
	void setDescription(String description);
	
	Date getCreationDate();

	List<TrainingSessionPostProxy> getPosts();
	
	String getTopic();
	
	void setTopic(String topic);
	
	List<TrainingSessionSubject> getSubjects();
	
	void setSubjects(List<TrainingSessionSubject> subjects);
	
	List<Team> getTeams();
	
	void setTeams(List<Team> teams);
	
	Rating getAverageRating();
	
	int getAwesomeRatingCount();
	
	int getBadRatingCount();
	
	int getGoodRatingCount();
	
	int getRatingCount();
	
	int getSuperRatingCount();

}
