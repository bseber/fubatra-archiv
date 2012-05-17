package de.fubatra.archiv.server.domain;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class TrainingSessionPost extends ObjectifyEntity {
	
	@Parent
	private Key<TrainingSession> trainingSessionKey;
	
	private Key<UserInfo> userInfoKey;
	
	private Text text;
	
	private Date creationDate;
	
	private Date lastEditedDate;

	public TrainingSession getTrainingSession() {
		return ObjectifyService.begin().load().key(trainingSessionKey).get();
	}
	
	public void setTrainingSession(TrainingSession trainingSession) {
		trainingSessionKey = ObjectifyService.factory().getKey(trainingSession);
	}
	
	public UserInfo getUserInfo() {
		return ObjectifyService.begin().load().key(userInfoKey).get();
	}
	
	public void setUserInfo(UserInfo user) {
		this.userInfoKey = ObjectifyService.factory().getKey(user);
	}
	
	public String getText() {
		return text.getValue();
	}
	
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		this.text = new Text(text);
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public Date getLastEditedDate() {
		return lastEditedDate;
	}
	
	@Override
	protected void doBeforeSave(Objectify ofy) {
		Date now = new Date();
		if (creationDate == null) {
			creationDate = now;
		}
		lastEditedDate = now;
	}
	
}
