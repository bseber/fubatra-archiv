package de.fubatra.archiv.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.fubatra.archiv.server.ioc.FubatraArchivServiceLocator;
import de.fubatra.archiv.server.service.UserInfoService;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;

@Service(value = UserInfoService.class, locator = FubatraArchivServiceLocator.class)
public interface UserServiceRequestSecured extends RequestContext {
	
	/**
	 * @param ownerId
	 * @param seasonProxy
	 * @return
	 */
	Request<UserInfoSeasonProxy> addSeasonToUser(long ownerId, UserInfoSeasonProxy seasonProxy);
	
	/**
	 * @param name
	 * @return true if the given name is available, false if it is used already
	 */
	Request<Boolean> checkIfUsernameIsAvailable(String name);
	
	/**
	 * @param seasonProxy
	 * @return
	 */
	Request<Void> deleteSeason(UserInfoSeasonProxy season);

	/**
	 * Remember to append <code>.with("email")</code> to this request to get
	 * user's email.
	 * 
	 * @return the {@link UserInfoProxy} of the current logged in user or null
	 */
	Request<UserInfoProxy> getCurrentUser();
	
	/**
	 * @param trainingSessionId
	 * @return
	 */
	Request<Rating> getCurrentUsersRatingOfTrainingSession(long trainingSessionId);
	
	/**
	 * @param userInfo
	 * @return the saved {@link UserInfoProxy}
	 */
	Request<UserInfoProxy> save(UserInfoProxy userInfo);
	
	/**
	 * @param season
	 * @return the saved {@link UserInfoSeasonProxy}
	 */
	Request<UserInfoSeasonProxy> saveSeason(UserInfoSeasonProxy season);

}
