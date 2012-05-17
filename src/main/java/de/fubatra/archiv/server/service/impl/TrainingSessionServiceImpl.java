package de.fubatra.archiv.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.inject.Inject;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.TxnWork;
import com.googlecode.objectify.cmd.Query;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.domain.TrainingSessionPost;
import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.service.TrainingSessionService;
import de.fubatra.archiv.server.service.UserInfoService;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.SortByItem;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;
import de.fubatra.archiv.shared.domain.TrainingSessionVisiblity;

public class TrainingSessionServiceImpl implements TrainingSessionService {

	private UserInfoService userService;
	
	@Inject
	public TrainingSessionServiceImpl(UserInfoService userService) {
		this.userService = userService;
	}
	
	/**
	 * Thought for JUnit Test...
	 */
	TrainingSessionServiceImpl() {
	}
	
	@Override
	public TrainingSessionPost addPost(long trainingSessionId, String post) {
		UserInfo currentUser = userService.getCurrentUser();
		TrainingSession trainingSession = findById(trainingSessionId);
		TrainingSessionPost trainingSessionPost = new TrainingSessionPost();
		trainingSessionPost.setText(post);
		trainingSessionPost.setUserInfo(currentUser);
		trainingSessionPost.setTrainingSession(trainingSession);
		ofy().save().entity(trainingSessionPost).now();
		return trainingSessionPost;
	}
	
	@Override
	public Integer countPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects) {
		Query<TrainingSession> q = ofy().load().type(TrainingSession.class);
		if (!teams.isEmpty()) {
			q = q.filter(TrainingSession.DB_FIELD_TEAMS + " in", teams);
		}
		if (!subjects.isEmpty()) {
			q = q.filter(TrainingSession.DB_FIELD_SUBJECTS + " in", subjects);
		}
		return q.count();
	}

	@Override
	public void deletePost(TrainingSessionPost post) {
		ofy().delete().entity(post).now();
	}
	
	@Override
	public void deleteTrainingSession(final TrainingSession trainingSession) {
		ofy().transact(new TxnWork<Objectify, Void>() {
			@Override
			public Void run(Objectify ofy) {
				List<Object> children = ofy.load().ancestor(trainingSession).list();
				ofy.delete().entities(children).now();
				ofy.delete().entity(trainingSession).now();
				return null;
			}
		});
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TrainingSession> findBestRatedPublicSessionsOfOwner(long ownerId, int offset, int limit) {
		UserInfo owner = userService.findUserById(ownerId);
		if (owner == null) {
			return Collections.EMPTY_LIST;
		}
		Query<TrainingSession> q = ofy().load().type(TrainingSession.class);
		q = q.filter(TrainingSession.DB_FIELD_OWNER, owner);
		q = q.filter(TrainingSession.DB_FIELD_VISIBILITY, TrainingSessionVisiblity.PUBLIC);
		q = q.offset(offset);
		q = q.limit(limit);
		q = q.order("-" + TrainingSession.DB_FIELD_AVG_RATING);
		return q.list();
	}
	
	@Override
	public List<TrainingSession> findPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects, SortByItem sortedBy, int offset, int limit) {
		Query<TrainingSession> q = ofy().load().type(TrainingSession.class);
		q = q.filter(TrainingSession.DB_FIELD_VISIBILITY, TrainingSessionVisiblity.PUBLIC);
		if (sortedBy != null) {
			switch (sortedBy) {
			// case NONE -> nothing to order...
			case CREATION_DATE:
				q = q.order("-" + TrainingSession.DB_FIELD_CREATION_DATE);
				break;
			case RATING:
				q = q.order("-" + TrainingSession.DB_FIELD_AVG_RATING);
				break;
			case RATING_COUNT:
				q = q.order("-" + TrainingSession.DB_FIELD_RATING_COUNT);
				break;
			}
		}
		
		int currentOffset = 0;
		if (limit == 0) {
			// no limit
			limit = Integer.MAX_VALUE;
		}
		
		QueryResultIterator<TrainingSession> iterator = q.iterator();
		ArrayList<TrainingSession> result = new ArrayList<TrainingSession>(limit == Integer.MAX_VALUE ? 10 : limit);
		
		while (iterator.hasNext() && result.size() != limit) {
			TrainingSession session = iterator.next();
			
			boolean hasTeam = teams.isEmpty() || !Collections.disjoint(teams, session.getTeams());
			boolean hasSubject = subjects.isEmpty() || !Collections.disjoint(subjects, session.getSubjects());
			boolean inRange = currentOffset >= offset;
			
			if (hasTeam && hasSubject && inRange) {
				result.add(session);
			}
			
			currentOffset++;
		}
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TrainingSession> findRecentPublicSessionsOfOwner(long ownerId, int offset, int limit) {
		UserInfo owner = userService.findUserById(ownerId);
		if (owner == null) {
			return Collections.EMPTY_LIST;
		}
		
		Query<TrainingSession> q = ofy().load().type(TrainingSession.class);
		q = q.filter(TrainingSession.DB_FIELD_OWNER, owner);
		q = q.order("-" + TrainingSession.DB_FIELD_CREATION_DATE);
		q = q.offset(offset);
		q = q.limit(limit);
		return q.list();
	}
	
	@Override
	public TrainingSession findById(long id) {
		TrainingSession session = ofy().load().type(TrainingSession.class).id(id).get();
		return session;
	}

	@Override
	public Long save(final TrainingSession trainingSession) {
		return ofy().save().entity(trainingSession).now().getId();
	}
	
	@Override
	public TrainingSession setCurrentUsersRatingOfTrainingSession(Long trainingSessionId, Rating rating) {
		UserInfo currentUser = userService.getCurrentUser();
		TrainingSession session = findById(trainingSessionId);
		if (session == null) {
			return null;
		}
		session.setRating(currentUser, rating);
		ofy().save().entity(session).now();
		return session;
	}
	
	@Override
	public TrainingSessionPost updatePost(TrainingSession trainingSession, long postId, String text) {
		UserInfo currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return null;
		}
		
		QueryResultIterator<TrainingSessionPost> iterator = ofy().load().type(TrainingSessionPost.class).ancestor(trainingSession).iterator();
		while (iterator.hasNext()) {
			TrainingSessionPost post = iterator.next();
			
			Long ownerId = post.getUserInfo().getId();
			Long curUserId = currentUser.getId();
			
			boolean sameUser = ownerId.equals(curUserId);
			boolean samePost = post.getId().equals(postId);
			
			if (sameUser && samePost) {
				post.setText(text);
				ofy().save().entity(post).now();
				return post;
			}
		}
		
		return null;
	}

	/**
	 * Thought for JUnit Test...
	 */
	void setUserService(UserInfoService userService) {
		this.userService = userService;
	}
	
	private Objectify ofy() {
		return ObjectifyService.begin();
	}

}
