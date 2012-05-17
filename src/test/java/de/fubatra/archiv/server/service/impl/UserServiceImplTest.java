package de.fubatra.archiv.server.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.objectify.ObjectifyService;

import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.server.domain.UserInfoSeason;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.test.AbstractTest;

public class UserServiceImplTest extends AbstractTest {
	
	private static UserInfoServiceImpl userInfoService;

	@BeforeClass
	public static void registerEntities() {
		ObjectifyService.register(UserInfo.class);
		ObjectifyService.register(UserInfoSeason.class);
		
		userInfoService = new UserInfoServiceImpl();
	}

	@Test
	public void saveUserInfo() {
		UserInfo max = new UserInfo();
		max.setEmail("max@mail.de");
		max.setGoogleUserId("1");
		max.setName("max");
		
		userInfoService.save(max);
		
		UserInfo fetched = userInfoService.findUserById(1);
		assertEquals("max@mail.de", fetched.getEmail());
	}
	
	@Test
	public void addNewSeasonToUser() {
		UserInfo max = new UserInfo();
		max.setEmail("max@mail.de");
		max.setGoogleUserId("1");
		max.setName("max");
		UserInfo savedMax = userInfoService.save(max);
		
		UserInfoSeason season = new UserInfoSeason();
		season.setClub("Verein");
		season.setSeason("Saison");
		season.setTeam(Team.Senioren);
		userInfoService.addSeasonToUser(1, season);

		List<UserInfoSeason> fetched = userInfoService.findSeasonsByUserId(savedMax.getId());
		assertEquals(1, fetched.size());
		assertEquals("Verein", fetched.get(0).getClub());
	}

}
