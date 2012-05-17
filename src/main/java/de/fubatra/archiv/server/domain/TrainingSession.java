package de.fubatra.archiv.server.domain;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import de.fubatra.archiv.server.annotations.Calculated;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;
import de.fubatra.archiv.shared.domain.TrainingSessionVisiblity;

@Entity
public class TrainingSession extends ObjectifyEntity {

	public static final String DB_FIELD_CREATION_DATE = "creationDate";
	public static final String DB_FIELD_OWNER = "ownerKey";
	public static final String DB_FIELD_AVG_RATING = "averageRatingOrdinal";
	public static final String DB_FIELD_RATING_COUNT = "ratingCount";
	public static final String DB_FIELD_SUBJECTS = "subjects";
	public static final String DB_FIELD_TEAMS = "teams";
	public static final String DB_FIELD_VISIBILITY = "visibility";
	
	@Index
	private Date creationDate;
	
	private Text description;
	
	private BlobKey draftBlobKey;
	
	private String draftUrl;
	
	@Index
	private Key<UserInfo> ownerKey;
	
	@Index
	@NotEmpty
	private List<TrainingSessionSubject> subjects;

	@Index
	@NotEmpty
	private List<Team> teams;

	@NotNull
	private String topic;
	
	@Index
	@NotNull
	private TrainingSessionVisiblity visibility;
	
	private Set<Key<UserInfo>> awesomeRating;
	
	private Set<Key<UserInfo>> superRating;
	
	private Set<Key<UserInfo>> goodRating;
	
	private Set<Key<UserInfo>> badRating;
	
	@Index
	private int averageRatingOrdinal;
	
	@Index
	private int ratingCount;
	
	public TrainingSession() {
		awesomeRating = new HashSet<Key<UserInfo>>();
		superRating = new HashSet<Key<UserInfo>>();
		goodRating = new HashSet<Key<UserInfo>>();
		badRating = new HashSet<Key<UserInfo>>();
	}

	public UserInfo getOwner() {
		return ObjectifyService.begin().load().key(ownerKey).get();
	}

	public void setOwner(UserInfo owner) {
		this.ownerKey = ObjectifyService.factory().getKey(owner);
	}

	public TrainingSessionVisiblity getVisibility() {
		return visibility;
	}
	
	public void setVisibility(TrainingSessionVisiblity visibility) {
		this.visibility = visibility;
	}
	
	public String getDraftBlobKey() {
		return draftBlobKey.getKeyString();
	}

	public void setDraftBlobKey(String blobKeyString) {
		this.draftBlobKey = new BlobKey(blobKeyString);
	}

	public String getDraftUrl() {
		return draftUrl;
	}
	
	public void setDraftUrl(String draftUrl) {
		this.draftUrl = draftUrl;
	}
	
	public String getDescription() {
		return description == null ? "" :description.getValue();
	}

	public void setDescription(String description) {
		if (description == null) {
			description = "";
		}
		this.description = new Text(description);
	}

	@Calculated
	public Date getCreationDate() {
		return creationDate;
	}

	@Calculated
	public List<TrainingSessionPost> getPosts() {
		return ObjectifyService.begin().load().type(TrainingSessionPost.class).ancestor(this).list();
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@SuppressWarnings("unchecked")
	public List<TrainingSessionSubject> getSubjects() {
		return subjects == null ? Collections.EMPTY_LIST : subjects;
	}
	
	public void setSubjects(List<TrainingSessionSubject> subjects) {
		this.subjects = subjects;
	}
	
	@SuppressWarnings("unchecked")
	public List<Team> getTeams() {
		return teams == null ? Collections.EMPTY_LIST : teams;
	}
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	@Calculated
	public Rating getAverageRating() {
		updateAverageRating();
		return Rating.values()[averageRatingOrdinal];
	}

	@Calculated
	public int getAwesomeRatingCount() {
		return awesomeRating.size();
	}

	@Calculated
	public int getBadRatingCount() {
		return badRating.size();
	}

	@Calculated
	public int getGoodRatingCount() {
		return goodRating.size();
	}

	@Calculated
	public int getRatingCount() {
		return ratingCount;
	}

	@Calculated
	public int getSuperRatingCount() {
		return superRating.size();
	}
	
	public void setRating(UserInfo user, Rating rating) {
		Key<UserInfo> key = getKeyOfUser(user);
		removeRating(key);
		switch (rating) {
		case AWESOME:
			awesomeRating.add(key);
			break;
		case SUPER:
			superRating.add(key);
			break;
		case GOOD:
			goodRating.add(key);
			break;
		case BAD:
			badRating.add(key);
			break;
		}
		updateAverageRating();
	}

	public Rating getRatingOfUser(UserInfo user) {
		Key<UserInfo> key = getKeyOfUser(user);
		if (awesomeRating.contains(key)) {
			return Rating.AWESOME;
		}
		if (superRating.contains(key)) {
			return Rating.SUPER;
		}
		if (goodRating.contains(key)) {
			return Rating.GOOD;
		}
		if (badRating.contains(key)) {
			return Rating.BAD;
		}
		return Rating.NONE;
	}
	
	@Override
	protected void doBeforeSave(Objectify ofy) {
		if (creationDate == null) {
			creationDate = new Date();
		}
		ratingCount = getAwesomeRatingCount() + getBadRatingCount() + getGoodRatingCount() + getSuperRatingCount();
		updateAverageRating();
	}
	
	/**
	 * Thought only for Junit Test!
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	private Key<UserInfo> getKeyOfUser(UserInfo user) {
		return ObjectifyService.factory().getKey(user);
	}
	
	private void removeRating(Key<UserInfo> userKey) {
		if (awesomeRating.contains(userKey)) {
			awesomeRating.remove(userKey);
		} else if (superRating.contains(userKey)) {
			superRating.remove(userKey);
		} else if (goodRating.contains(userKey)) {
			goodRating.remove(userKey);
		} else if (badRating.contains(userKey)) {
			badRating.remove(userKey);
		}
	}
	
	private void updateAverageRating() {
		int ratingCount = getRatingCount();
		if (ratingCount == 0) {
			averageRatingOrdinal = 0;
			return;
		}
		
		int awesome = getAwesomeRatingCount() * 4;
		int zuper = getSuperRatingCount() * 3;
		int good = getGoodRatingCount() * 2;
		int bad = getBadRatingCount();
		
		int sum = awesome + zuper + good + bad;
		float avg = (1F * sum) / ratingCount;
		averageRatingOrdinal = Math.round(avg);
	}

}
