package de.fubatra.archiv.server.service;

import java.util.List;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.domain.TrainingSessionPost;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.SortByItem;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

public interface TrainingSessionService {

	/**
	 * Adds a comment to the given {@link TrainingSession}. The owner of the
	 * comment will be the current logged in user.
	 * 
	 * @param trainingSessionId
	 * @param comment
	 * @return the created comment
	 */
	TrainingSessionPost addPost(long trainingSessionId, String comment);

	/**
	 * Counts all {@link TrainingSession}s that matches the given attributes and
	 * returns the number.
	 * 
	 * @param teams
	 * @param subjects
	 * @return
	 */
	Integer countPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects);

	/**
	 * @param post
	 */
	void deletePost(TrainingSessionPost post);
	
	/**
	 * @param trainingSession
	 */
	void deleteTrainingSession(TrainingSession trainingSession);
	
	/**
	 * @param ownId
	 * @param sortedField
	 * @param desc
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<TrainingSession> findBestRatedPublicSessionsOfOwner(long ownerId, int offset, int limit);
	
	/**
	 * @param teams
	 * @param subjects
	 * @param offset
	 * @param limit
	 * @return a list of all {@link TrainingSession}s that matches the given
	 *         attributes
	 */
	List<TrainingSession> findPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects, SortByItem sortedBy, int offset, int limit);

	/**
	 * @param ownerId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<TrainingSession> findRecentPublicSessionsOfOwner(long ownerId, int offset, int limit);
	
	/**
	 * @param id
	 * @return
	 */
	TrainingSession findById(long id);

	/**
	 * @param trainingSessionId
	 * @param rating
	 * @return the updated {@link TrainingSession}
	 */
	TrainingSession setCurrentUsersRatingOfTrainingSession(Long trainingSessionId, Rating rating);

	/**
	 * Saves the trainingSession and returns the id.
	 * 
	 * @param trainingSession
	 * @return
	 */
	Long save(TrainingSession trainingSession);

	/**
	 * @param trainingSession 
	 * @param postId
	 * @param text
	 *            the new text to set
	 * @return the updated {@link TrainingSessionPost}
	 */
	TrainingSessionPost updatePost(TrainingSession trainingSession, long postId, String text);

}
