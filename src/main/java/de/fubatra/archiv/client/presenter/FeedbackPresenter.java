package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.FeedbackView;
import de.fubatra.archiv.client.util.UserUtil;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;

@Presenter(view = FeedbackView.class)
public class FeedbackPresenter extends LazyPresenter<FeedbackView, AppEventBus> implements FeedbackView.IPresenter {

	private final AppRequestFactorySecured requestFactory;
	
	@Inject
	public FeedbackPresenter(AppRequestFactorySecured requestFactory) {
		this.requestFactory = requestFactory;
	}

	@Override
	public void bindView() {
		String email = UserUtil.getUserEmail();
		view.setUserEmail(email);
	}
	
	@Override
	public void onCancelClicked() {
		view.resetAndHide();
	}
	
	public void onInitFeedbackWidget() {
		RootPanel.get().add(view);
	}
	
	@Override
	public void onSendFeedbackClicked() {
		String text = view.getMessage().getText();
		requestFactory.feedbackService().sendFeedback(text).fire(new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean success) {
				if (success) {
					view.resetAndHide();
				} else {
					Window.alert("Das tägliche Limit an versendeten Emails ist leider erreicht. Versuceh es einfach morgen wieder.");
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Hm... Da ist auf dem Server was schiefgelaufen...");
				super.onFailure(error);
			}
		});
	}
	
}
