package de.fubatra.archiv.client.presenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.UsersTrainingSessionsView;

@Presenter(view = UsersTrainingSessionsView.class)
public class UsersTrainingSessionPresenter extends LazyPresenter<UsersTrainingSessionsView, AppEventBus> implements UsersTrainingSessionsView.IPresenter {
	
	public void onListTrainingSessionsOfUser(long userId) {
		eventBus.setContent(view);
	}

}
