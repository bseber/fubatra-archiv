package de.fubatra.archiv.server.service;

public interface FeedbackService {

	/**
	 * Sends the given message to my humble self. Sender will be the current
	 * logged in user.
	 * 
	 * @param message
	 * @return true if the message was sent successfully, false otherwise
	 */
	boolean sendFeedback(String message);

}
