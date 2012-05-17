package de.fubatra.archiv.server.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;

import de.fubatra.archiv.shared.domain.Team;

@Entity
public class UserInfoSeason extends ObjectifyEntity {

	@Parent
	private Key<UserInfo> ownerKey;
	
	private String season;
	
	private String club;
	
	private Team team;

	public UserInfo getOwner() {
		if (ownerKey == null) {
			return null;
		}
		return ObjectifyService.begin().load().key(ownerKey).get();
	}
	
	public void setOwner(UserInfo owner) {
		this.ownerKey = ObjectifyService.factory().getKey(owner);
	}
	
	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Override
	protected void doBeforeSave(Objectify ofy) {
		if (team == null) {
			team = Team.NONE;
		}
	}

}
