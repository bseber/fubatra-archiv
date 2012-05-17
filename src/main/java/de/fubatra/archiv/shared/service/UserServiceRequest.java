package de.fubatra.archiv.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.fubatra.archiv.server.ioc.FubatraArchivServiceLocator;
import de.fubatra.archiv.server.service.UserInfoService;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;

@Service(value = UserInfoService.class, locator = FubatraArchivServiceLocator.class)
public interface UserServiceRequest extends RequestContext {
	
	/**
	 * @param id
	 * @return
	 */
	Request<UserInfoProxy> findUserById(long userId);
	
	/**
	 * @param userId
	 * @return a list of the user's {@link UserInfoSeasonProxy}
	 */
	Request<List<UserInfoSeasonProxy>> findSeasonsByUserId(long userId);

	/**
	 * @return true if the user is logged in, false otherwise
	 */
	Request<Boolean> isUserLoggedIn();
	
}
