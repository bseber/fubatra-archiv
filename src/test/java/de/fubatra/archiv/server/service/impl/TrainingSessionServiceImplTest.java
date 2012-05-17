package de.fubatra.archiv.server.service.impl;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.objectify.ObjectifyService;

import de.fubatra.archiv.server.domain.TrainingSession;
import de.fubatra.archiv.server.domain.UserInfo;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.SortByItem;
import de.fubatra.archiv.shared.domain.TrainingSessionVisiblity;
import de.fubatra.archiv.test.AbstractTest;

public class TrainingSessionServiceImplTest extends AbstractTest {

	private static TrainingSessionServiceImpl trainingSessionService;
	private static UserInfoServiceImpl userInfoService;

	@BeforeClass
	public static void registerEntities() {
		ObjectifyService.register(TrainingSession.class);
		ObjectifyService.register(UserInfo.class);

		trainingSessionService = new TrainingSessionServiceImpl();
		userInfoService = new UserInfoServiceImpl();
		trainingSessionService.setUserService(userInfoService);
		userInfoService.setTrainingSessionService(trainingSessionService);
	}

	@Test
	public void saveAndFindById() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		try {
			TrainingSession sessionOne = new TrainingSession();
			sessionOne.setTopic("Mein Thema Eins");
			sessionOne.setCreationDate(sdf.parse("01.01.1999 12:12:12"));

			TrainingSession sessionTwo = new TrainingSession();
			sessionTwo.setTopic("Mein Thema Zwei");

			trainingSessionService.save(sessionOne);
			trainingSessionService.save(sessionTwo);

			TrainingSession fetchedOne = trainingSessionService.findById(1);
			TrainingSession fetchedTwo = trainingSessionService.findById(2);

			assertNotNull(fetchedOne);
			assertNotNull(fetchedTwo);
			assertEquals("Mein Thema Eins", fetchedOne.getTopic());
			assertEquals("Mein Thema Zwei", fetchedTwo.getTopic());
			assertEquals(sdf.parse("01.01.1999 12:12:12"), sessionOne.getCreationDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findWithOffsetAndLimit() {
		UserInfo max = new UserInfo();
		userInfoService.save(max);

		TrainingSession one = createSession(max, "1", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(one);
		TrainingSession two = createSession(max, "2", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(two);
		TrainingSession three = createSession(max, "3", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(three);
		TrainingSession four = createSession(max, "4", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(four);
		TrainingSession five = createSession(max, "5", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(five);
		TrainingSession six = createSession(max, "6", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(six);
		TrainingSession seven = createSession(max, "7", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(seven);
		TrainingSession eight = createSession(max, "8", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(eight);
		TrainingSession nine = createSession(max, "9", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(nine);
		TrainingSession ten = createSession(max, "10", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(ten);

		List<TrainingSession> zeroToFour = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				SortByItem.NONE, 0, 4);
		List<TrainingSession> fourToEight = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				SortByItem.NONE, 4, 4);
		List<TrainingSession> eightToTwelve = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				SortByItem.NONE, 8, 4);

		assertEquals(4, zeroToFour.size());
		assertEquals(4, fourToEight.size());
		assertEquals(2, eightToTwelve.size());
	}

	@Test
	public void findSessionsSortedByNewest() {
		UserInfo max = new UserInfo();
		userInfoService.save(max);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		try {
			TrainingSession one = createSession(max, "Eins", TrainingSessionVisiblity.PUBLIC);
			one.setCreationDate(sdf.parse("05.01.1994 10:22:07"));
			trainingSessionService.save(one);

			TrainingSession two = createSession(max, "Zwei", TrainingSessionVisiblity.PUBLIC);
			two.setCreationDate(sdf.parse("12.01.2012 10:22:07"));
			trainingSessionService.save(two);

			TrainingSession three = createSession(max, "Drei", TrainingSessionVisiblity.PUBLIC);
			three.setCreationDate(sdf.parse("12.01.1999 10:22:07"));
			trainingSessionService.save(three);

			TrainingSession four = createSession(max, "Vier", TrainingSessionVisiblity.PUBLIC);
			four.setCreationDate(sdf.parse("12.01.1999 09:22:07"));
			trainingSessionService.save(four);

			@SuppressWarnings("unchecked")
			List<TrainingSession> fetched = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
					SortByItem.CREATION_DATE, 0, 0);

			assertEquals(4, fetched.size());
			assertEquals("Zwei", fetched.get(0).getTopic());
			assertEquals("Drei", fetched.get(1).getTopic());
			assertEquals("Vier", fetched.get(2).getTopic());
			assertEquals("Eins", fetched.get(3).getTopic());

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void findSessionsSortedByBestRating() {
		UserInfo max = new UserInfo();
		userInfoService.save(max);

		TrainingSession one = createSession(max, "Eins", TrainingSessionVisiblity.PUBLIC);
		one.setRating(max, Rating.SUPER);
		trainingSessionService.save(one);

		TrainingSession two = createSession(max, "Zwei", TrainingSessionVisiblity.PUBLIC);
		two.setRating(max, Rating.GOOD);
		trainingSessionService.save(two);

		TrainingSession three = createSession(max, "Drei", TrainingSessionVisiblity.PUBLIC);
		three.setRating(max, Rating.AWESOME);
		trainingSessionService.save(three);

		TrainingSession four = createSession(max, "Vier", TrainingSessionVisiblity.PUBLIC);
		four.setRating(max, Rating.BAD);
		trainingSessionService.save(four);

		@SuppressWarnings("unchecked")
		List<TrainingSession> fetched = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				SortByItem.RATING, 0, 0);

		assertEquals(4, fetched.size());
		assertEquals("Drei", fetched.get(0).getTopic());
		assertEquals("Eins", fetched.get(1).getTopic());
		assertEquals("Zwei", fetched.get(2).getTopic());
		assertEquals("Vier", fetched.get(3).getTopic());
	}

	@Test
	public void findSessionsSortedByRatingCount() {
		UserInfo max = new UserInfo();
		userInfoService.save(max);
		UserInfo otto = new UserInfo();
		userInfoService.save(otto);
		UserInfo hans = new UserInfo();
		userInfoService.save(hans);

		TrainingSession one = createSession(max, "Eins", TrainingSessionVisiblity.PUBLIC);
		one.setRating(max, Rating.SUPER);
		trainingSessionService.save(one);

		TrainingSession two = createSession(max, "Zwei", TrainingSessionVisiblity.PUBLIC);
		two.setRating(max, Rating.GOOD);
		two.setRating(otto, Rating.GOOD);
		two.setRating(hans, Rating.GOOD);
		trainingSessionService.save(two);

		TrainingSession three = createSession(max, "Drei", TrainingSessionVisiblity.PUBLIC);
		trainingSessionService.save(three);

		TrainingSession four = createSession(max, "Vier", TrainingSessionVisiblity.PUBLIC);
		four.setRating(max, Rating.BAD);
		four.setRating(otto, Rating.BAD);
		trainingSessionService.save(four);

		@SuppressWarnings("unchecked")
		List<TrainingSession> fetched = trainingSessionService.findPublicSessionsByAttributes(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				SortByItem.RATING_COUNT, 0, 0);

		assertEquals(4, fetched.size());
		assertEquals("Zwei", fetched.get(0).getTopic());
		assertEquals("Vier", fetched.get(1).getTopic());
		assertEquals("Eins", fetched.get(2).getTopic());
		assertEquals("Drei", fetched.get(3).getTopic());
	}

	@Test
	public void findRecentPublicSessionsOfOwner() {
		UserInfo max = new UserInfo();
		UserInfo maxSaved = userInfoService.save(max);
		Long maxId = maxSaved.getId();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		try {
			TrainingSession one = createSession(max, "Eins", TrainingSessionVisiblity.PUBLIC);
			one.setCreationDate(sdf.parse("05.05.2012 15:00:00"));
			trainingSessionService.save(one);

			TrainingSession two = createSession(max, "Zwei", TrainingSessionVisiblity.PUBLIC);
			two.setCreationDate(sdf.parse("05.05.2012 14:00:00"));
			trainingSessionService.save(two);

			TrainingSession three = createSession(max, "Drei", TrainingSessionVisiblity.PUBLIC);
			three.setCreationDate(sdf.parse("05.05.1999 12:12:12"));
			trainingSessionService.save(three);

			TrainingSession four = createSession(max, "Vier", TrainingSessionVisiblity.PUBLIC);
			four.setCreationDate(sdf.parse("05.05.2042 12:12:12"));
			trainingSessionService.save(four);

			List<TrainingSession> fetched = trainingSessionService.findRecentPublicSessionsOfOwner(maxId, 0, 5);
			assertEquals(4, fetched.size());
			assertEquals("Vier", fetched.get(0).getTopic());
			assertEquals("Eins", fetched.get(1).getTopic());
			assertEquals("Zwei", fetched.get(2).getTopic());
			assertEquals("Drei", fetched.get(3).getTopic());

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void findBestRatedPublicSessionsOfOwner() {
		UserInfo owner = new UserInfo();
		long ownerId = userInfoService.save(owner).getId();

		UserInfo max = new UserInfo();
		userInfoService.save(max);
		UserInfo otto = new UserInfo();
		userInfoService.save(otto);
		UserInfo hans = new UserInfo();
		userInfoService.save(hans);

		TrainingSession privateSession = createSession(owner, "", TrainingSessionVisiblity.PRIVATE);
		trainingSessionService.save(privateSession);

		TrainingSession one = createSession(owner, "Eins", TrainingSessionVisiblity.PUBLIC);
		one.setRating(max, Rating.AWESOME);
		one.setRating(otto, Rating.AWESOME);
		one.setRating(hans, Rating.AWESOME);
		trainingSessionService.save(one);

		TrainingSession two = createSession(owner, "Zwei", TrainingSessionVisiblity.PUBLIC);
		two.setRating(max, Rating.BAD);
		two.setRating(otto, Rating.BAD);
		two.setRating(hans, Rating.BAD);
		trainingSessionService.save(two);

		TrainingSession three = createSession(owner, "Drei", TrainingSessionVisiblity.PUBLIC);
		three.setRating(max, Rating.SUPER);
		three.setRating(otto, Rating.SUPER);
		three.setRating(hans, Rating.AWESOME);
		trainingSessionService.save(three);

		TrainingSession four = createSession(owner, "Vier", TrainingSessionVisiblity.PUBLIC);
		four.setRating(max, Rating.AWESOME);
		four.setRating(otto, Rating.SUPER);
		four.setRating(hans, Rating.AWESOME);
		trainingSessionService.save(four);

		List<TrainingSession> fetched = trainingSessionService.findBestRatedPublicSessionsOfOwner(ownerId, 0, 5);
		assertEquals(4, fetched.size());
		assertEquals("Eins", fetched.get(0).getTopic());
		assertEquals("Vier", fetched.get(1).getTopic());
		assertEquals("Drei", fetched.get(2).getTopic());
		assertEquals("Zwei", fetched.get(3).getTopic());
	}

	private TrainingSession createSession(UserInfo owner, String topic, TrainingSessionVisiblity visibility) {
		TrainingSession session = new TrainingSession();
		session.setOwner(owner);
		session.setTopic(topic);
		session.setVisibility(visibility);
		return session;
	}

}
