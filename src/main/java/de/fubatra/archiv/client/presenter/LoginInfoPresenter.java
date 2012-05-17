package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.LoginInfoView;
import de.fubatra.archiv.shared.service.AppRequestFactory;

@Presenter(view = LoginInfoView.class)
public class LoginInfoPresenter extends BasePresenter<LoginInfoPresenter.IView, AppEventBus> {
	
	public interface IView extends IsWidget {
		
		void setLoginButtonText(String text);
		
		void setLoginButtonHref(String href);
	}
	
	private final AppRequestFactory requestFactory;

	@Inject
	public LoginInfoPresenter(AppRequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}

	public void onLogin() {
		eventBus.setContent(view);
		requestFactory.userServiceRequest().isUserLoggedIn().fire(new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean loggedIn) {
				if (loggedIn) {
					eventBus.startPage();
				} else {
					createLoginUrl();
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
				super.onFailure(error);
			}
		});
	}

	private void createLoginUrl() {
		requestFactory.loginServiceRequest().createLoginUrl(Location.getHref()).fire(new Receiver<String>() {
			@Override
			public void onSuccess(String loginUrl) {
				view.setLoginButtonHref(loginUrl);
			}
		});
	}

}
