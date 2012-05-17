package de.fubatra.archiv.shared.domain;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.fubatra.archiv.server.domain.UserInfoSeason;
import de.fubatra.archiv.server.ioc.ObjectifyEntityLocator;

@ProxyFor(value = UserInfoSeason.class, locator = ObjectifyEntityLocator.class)
public interface UserInfoSeasonProxy extends EntityProxy {
	
	public class KeyProvider implements ProvidesKey<UserInfoSeasonProxy> {

		@Override
		public Object getKey(UserInfoSeasonProxy item) {
			return item.getId();
		}
		
	}
	
	Long getId();
	
	UserInfoProxy getOwner();
	
	void setOwner(UserInfoProxy owner);
	
	String getSeason();
	
	void setSeason(String season);
	
	String getClub();
	
	void setClub(String club);
	
	Team getTeam();
	
	void setTeam(Team team);

}
