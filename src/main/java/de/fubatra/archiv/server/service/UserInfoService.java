package de.fubatra.archiv.server.service;

import java.util.List;

import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.domain.UserInfoSeason;
import de.fubatra.archiv.shared.domain.Rating;

public interface UserInfoService {

	/**
	 * @param userId
	 * @param season
	 * @return the saved {@link UserInfoSeason}
	 */
	UserInfoSeason addSeasonToUser(long userId, UserInfoSeason season);

	/**
	 * @param name
	 * @return true if the given name is the name of the current logged in user
	 *         or if it is not used yet, false otherwise
	 */
	boolean checkIfUsernameIsAvailable(String name);

	/**
	 * @param season
	 */
	void deleteSeason(UserInfoSeason season);

	/**
	 * @param id
	 * @return
	 */
	UserInfo findUserById(long id);;

	/**
	 * @param userId
	 * @return a list of the user's seasons
	 */
	List<UserInfoSeason> findSeasonsByUserId(long userId);

	/**
	 * @return the {@link UserInfo} of the current logged in user or null
	 */
	UserInfo getCurrentUser();

	/**
	 * @param trainingSessionId
	 * @return
	 */
	Rating getCurrentUsersRatingOfTrainingSession(long trainingSessionId);

	/**
	 * @param id
	 * @return
	 */
	UserInfo getUserByGoogleUserId(String id);

	boolean isUserLoggedIn();

	/**
	 * @param userInfo
	 * @return the saved {@link UserInfo}
	 */
	UserInfo save(UserInfo userInfo);

	/**
	 * @param season
	 * @return the saved {@link UserInfoSeason}
	 */
	UserInfoSeason saveSeason(UserInfoSeason season);

}
