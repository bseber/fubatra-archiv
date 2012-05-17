package de.fubatra.archiv.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.ioc.FubatraArchivServiceLocator;
import de.fubatra.archiv.server.service.TrainingSessionService;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.SortByItem;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionPostProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

@Service(value = TrainingSessionService.class, locator = FubatraArchivServiceLocator.class)
public interface TrainingSessionServiceRequest extends RequestContext {

	/**
	 * @param trainingSessionId
	 * @param comment
	 * @return
	 */
	Request<TrainingSessionPostProxy> addPost(long trainingSessionId, String post);
	
	/**
	 * Counts all {@link TrainingSession}s that matches the given attributes and
	 * returns the number.
	 * 
	 * @param teams
	 * @param subjects
	 * @return
	 */
	Request<Integer> countPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects);

	/**
	 * @param post
	 * @return
	 */
	Request<Void> deletePost(TrainingSessionPostProxy post);
	
	/**
	 * @param trainingSessionProxy
	 * @return
	 */
	Request<Void> deleteTrainingSession(TrainingSessionProxy trainingSessionProxy);
	
	/**
	 * @param ownerId
	 * @param offset
	 * @param limit
	 * @return
	 */
	Request<List<TrainingSessionProxy>> findBestRatedPublicSessionsOfOwner(long ownerId, int offset, int limit);
	
	/**
	 * @param teams
	 * @param subjects
	 * @param offset
	 * @param limit
	 * @return a list of all {@link TrainingSessionSubjectsProxy}s that
	 *         matches the given attributes
	 */
	Request<List<TrainingSessionProxy>> findPublicSessionsByAttributes(List<Team> teams, List<TrainingSessionSubject> subjects, SortByItem sortedBy, int offset, int limit);

	/**
	 * @param ownerId
	 * @param offset
	 * @param limit
	 * @return
	 */
	Request<List<TrainingSessionProxy>> findRecentPublicSessionsOfOwner(long ownerId, int offset, int limit);
	
	/**
	 * @param id
	 * @return
	 */
	Request<TrainingSessionProxy> findById(long id);
	
	/**
	 * @param trainingSession
	 * @return
	 */
	Request<Long> save(TrainingSessionProxy trainingSession);
	
	/**
	 * @param trainingSessionId
	 * @param rating
	 * @return the updated {@link TrainingSessionProxy}
	 */
	Request<TrainingSessionProxy> setCurrentUsersRatingOfTrainingSession(Long trainingSessionId, Rating rating);

	/**
	 * @param trainingSession
	 * @param postId
	 * @param text
	 *            the new text to set
	 * @return the updated {@link TrainingSessionPostProxy}
	 */
	Request<TrainingSessionPostProxy> updatePost(TrainingSessionProxy trianingSession, long postId, String text);


}
