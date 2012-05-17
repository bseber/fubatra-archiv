package de.fubatra.archiv.client.ioc;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Filters;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import de.fubatra.archiv.client.presenter.FeedbackPresenter;
import de.fubatra.archiv.client.presenter.LoginInfoPresenter;
import de.fubatra.archiv.client.presenter.LogoutInfoPresenter;
import de.fubatra.archiv.client.presenter.MainPresenter;
import de.fubatra.archiv.client.presenter.MenuPresenter;
import de.fubatra.archiv.client.presenter.NotAuthorizedPresenter;
import de.fubatra.archiv.client.presenter.SiteNoticePresenter;
import de.fubatra.archiv.client.presenter.TrainingSessionEditPresenter;
import de.fubatra.archiv.client.presenter.TrainingSessionListPresenter;
import de.fubatra.archiv.client.presenter.TrainingSessionPresenter;
import de.fubatra.archiv.client.presenter.UserInfoSeasonPresenter;
import de.fubatra.archiv.client.presenter.UserProfilePresenter;
import de.fubatra.archiv.client.presenter.UsersTrainingSessionPresenter;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;

@Filters(filterClasses = AppEventFilter.class, filterStart = false)
@Events(startPresenter = MainPresenter.class, ginModules = AppClientGinModule.class, historyOnStart = true)
public interface AppEventBus extends EventBus {
	
	@Start
	@Event(handlers = { MainPresenter.class, MenuPresenter.class })
	void start();
	
	@InitHistory
	@Event(handlers = TrainingSessionListPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String startPage();

	/*
	 * =============================================================================
	 * UI Events
	 */
	
	@Event(handlers = MainPresenter.class)
	void setContent(IsWidget widget);

	@Event(handlers = MenuPresenter.class)
	void initMenuView(SimplePanel parent);
	
	/*
	 * =============================================================================
	 * Authorized Events (user must be logged in)
	 */
	
	@Event(generate = UserInfoSeasonPresenter.class)
	void createNewSeason(FlowPanel parent, long ownerId);
	
	@Event(handlers = TrainingSessionEditPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String createTrainingSession();
	
	@Event(handlers = TrainingSessionEditPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String editTrainingSession(long id);
	
	@Event(handlers = FeedbackPresenter.class)
	void initFeedbackWidget();
	
	@Event(handlers = UserProfilePresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String profile(long userId);
	
	/*
	 * =============================================================================
	 * Common Events
	 */
	
	@Event(handlers = MainPresenter.class)
	void displayApplication();
	
	@Event(generate = UserInfoSeasonPresenter.class)
	void displaySeason(FlowPanel parent, UserInfoSeasonProxy seasonProxy, boolean edit);
	
	@Event(handlers = MainPresenter.class)
	void hideInitialLoadingIndicator();
	
	@Event(handlers = MainPresenter.class)
	void entityNotFound();
	
	@Event(handlers = UsersTrainingSessionPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String listTrainingSessionsOfUser(long userId);
	
	@Event(handlers = LoginInfoPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String login();
	
	@Event(handlers = LogoutInfoPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String logout();
	
	@Event(handlers = NotAuthorizedPresenter.class)
	void notAuthorized();

	@Event(handlers = TrainingSessionPresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String showTrainingSession(long id);
	
	@Event(handlers = SiteNoticePresenter.class, historyConverter = AppHistoryConverter.class, navigationEvent = true)
	String siteNotice();
	
}