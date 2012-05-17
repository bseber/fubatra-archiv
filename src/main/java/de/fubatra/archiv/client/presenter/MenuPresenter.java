package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.MenuView;
import de.fubatra.archiv.client.util.UserUtil;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.UserServiceRequestSecured;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<MenuPresenter.IView, AppEventBus> {

	public interface IView extends IsWidget {
		
		void setSiteNoticeHistoryToken(String historyToken);
		
		void showCreateTrainingSessionButton(String historyToken);
		
		void showListUsersTrainingSessionsButton(String historyToken);
		
		void showLoginButton(String historyToken);
		
		void showLogoutButton(String historyToken);
		
		void showProfileButton(String userEmail, String historyToken);
	}

	private final AppRequestFactory requestFactory;
	private final AppRequestFactorySecured requestFactorySecured;

	@Inject
	public MenuPresenter(AppRequestFactorySecured requestFactorySecured, AppRequestFactory requestFactory, EventBus gwtEventBus) {
		this.requestFactorySecured = requestFactorySecured;
		this.requestFactory = requestFactory;
	}

	@Override
	public void bind() {
		view.setSiteNoticeHistoryToken(getTokenGenerator().siteNotice());
	}

	public void onInitMenuView(SimplePanel parent) {
		parent.add(view);
	}

	public void onStart() {
		requestFactory.userServiceRequest().isUserLoggedIn().fire(new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean loggedIn) {
				if (loggedIn) {
					view.showLogoutButton(getTokenGenerator().logout());
					UserServiceRequestSecured userService = requestFactorySecured.userServiceRequestSecured();
					userService.getCurrentUser().with("email").fire(new Receiver<UserInfoProxy>() {
						@Override
						public void onSuccess(UserInfoProxy userInfo) {
							UserUtil.setUserInfo(userInfo);
							
							// user info must be available here
							eventBus.initFeedbackWidget();
							
							view.showListUsersTrainingSessionsButton(getTokenGenerator().listTrainingSessionsOfUser(userInfo.getId()));
							view.showCreateTrainingSessionButton(getTokenGenerator().createTrainingSession());
							view.showProfileButton(userInfo.getEmail(), getTokenGenerator().profile(userInfo.getId()));
							eventBus.displayApplication();
							if (!userInfo.isProfileInitiated()) {
								eventBus.profile(userInfo.getId());
							}
						}
					});
				} else {
					UserUtil.clearStorage();
					view.showLoginButton(getTokenGenerator().login());
					eventBus.displayApplication();
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
				super.onFailure(error);
			}
		});
	}
	
}
