package de.fubatra.archiv.server.service.impl;

import java.io.IOException;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import de.fubatra.archiv.server.service.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService {

	private static final String SENDER = "bennyseber@googlemail.com";
	private static final String RECEIVER = "bennyseber@googlemail.com";
	
	@Override
	public boolean sendFeedback(String message) {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			return false; 
		}
		
		boolean success = false;
		
		try {
			String subject = "Fubatra Archiv | Feedback von " + user.getEmail();
			Message msg = new MailService.Message(SENDER, RECEIVER, subject, message);
			MailServiceFactory.getMailService().send(msg);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}

}
