package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.NotAuthorizedView;

@Presenter(view = NotAuthorizedView.class)
public class NotAuthorizedPresenter extends BasePresenter<NotAuthorizedPresenter.IView, AppEventBus> {
	
	public interface IView extends IsWidget {
		
	}
	
	public void onNotAuthorized() {
		eventBus.setContent(view);
	}

}
