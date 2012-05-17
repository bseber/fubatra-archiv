package de.fubatra.archiv.server.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.domain.UserInfoSeason;
import de.fubatra.archiv.server.service.TrainingSessionService;
import de.fubatra.archiv.server.service.UserInfoService;
import de.fubatra.archiv.shared.domain.Rating;

public class UserInfoServiceImpl implements UserInfoService {
	
	private static final Logger LOG = Logger.getLogger(UploadServiceImpl.class.getName());
	
	private TrainingSessionService trainingSessionService;
	
//	@Inject
//	public UserInfoServiceImpl(TrainingSessionService trainingSessionService) {
//		this.trainingSessionService = trainingSessionService;
//	}
	
	/**
	 * Thought for JUnit Test...
	 */
	UserInfoServiceImpl() {
	}

	@Override
	public UserInfoSeason addSeasonToUser(long userId, UserInfoSeason season) {
		UserInfo user = findUserById(userId);
		if (user == null) {
			return season;
		}
		
		UserInfo owner = season.getOwner();
		boolean seasonHasAnotherOwner = owner != null && owner.getId() != userId;

		if (seasonHasAnotherOwner) {
			UserInfoSeason toSave = new UserInfoSeason();
			toSave.setClub(season.getClub());
			toSave.setOwner(user);
			toSave.setSeason(season.getSeason());
			toSave.setTeam(season.getTeam());
			deleteSeason(season);
			ofy().save().entity(toSave).now();
			return toSave;
		} else {
			season.setOwner(user);
			ofy().save().entity(season).now();
			return season;
		}
	}
	
	@Override
	public boolean checkIfUsernameIsAvailable(String name) {
		name = name.trim();
		if (name.isEmpty()) {
			return false;
		}
		UserInfo userInfo = ofy().load().type(UserInfo.class).filter(UserInfo.DB_FIELD_NAME, name).first().get();
		boolean available = userInfo == null;
		return available;
	}
	
	@Override
	public void deleteSeason(UserInfoSeason season) {
		ofy().delete().entity(season).now();
	}
	
	@Override
	public UserInfo findUserById(long id) {
		UserInfo user = ofy().load().type(UserInfo.class).id(id).get();
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserInfoSeason> findSeasonsByUserId(long userId) {
		UserInfo user = findUserById(userId);
		if (user == null) {
			return Collections.EMPTY_LIST;
		}
		return ofy().load().type(UserInfoSeason.class).ancestor(user).list();
	}
	
	@Override
	public UserInfo getCurrentUser() {
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currentUser == null) {
			LOG.info("no user is logged in");
			return null;
		}
		String userId = currentUser.getUserId();
		UserInfo userInfo = getUserByGoogleUserId(userId);
		if (userInfo == null) {
			LOG.info(currentUser.getEmail() + " is not yet registered. Create new UserInfo entity with googleUserId " + userId);
			userInfo = new UserInfo();
			userInfo.setProfileInitiated(false);
			userInfo.setEmail(currentUser.getEmail());
			userInfo.setGoogleUserId(userId);
			// save user with 'profileInitiated' flag = false
			// user will be prompt to set his desired name
			userInfo = save(userInfo, false);
		}
		return userInfo;
	}
	
	@Override
	public Rating getCurrentUsersRatingOfTrainingSession(long trainingSessionId) {
		UserInfo currentUser = getCurrentUser();
		if (currentUser == null) {
			return null;
		}
		TrainingSession trainingSession = trainingSessionService.findById(trainingSessionId);
		if (trainingSession == null) {
			return null;
		}
		TrainingSession session = trainingSessionService.findById(trainingSessionId);
		return session.getRatingOfUser(currentUser);
	}
	
	@Override
	public UserInfo getUserByGoogleUserId(String userId) {
		Query<UserInfo> q = ofy().load().type(UserInfo.class);
		q = q.filter(UserInfo.DB_FIELD_USERID, userId);
		UserInfo userInfo = q.first().get();
		return userInfo;
	}

	@Override
	public boolean isUserLoggedIn() {
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		return currentUser != null;
	}
	
	@Override
	public UserInfo save(UserInfo userInfo) {
		userInfo.setName(userInfo.getName().trim());
		return save(userInfo, true);
	}
	
	@Override
	public UserInfoSeason saveSeason(UserInfoSeason season) {
		ofy().save().entity(season).now();
		return season;
	}
	
	/**
	 * Only for JUnit Test...
	 */
	@Inject
	void setTrainingSessionService(TrainingSessionService trainingSessionService) {
		this.trainingSessionService = trainingSessionService;
	}
	
	private Objectify ofy() {
		return ObjectifyService.begin();
	}
	
	private UserInfo save(UserInfo userInfo, boolean profileEdited) {
		userInfo.setProfileInitiated(profileEdited);
		ofy().save().entity(userInfo).now();
		return userInfo;
	}

}
