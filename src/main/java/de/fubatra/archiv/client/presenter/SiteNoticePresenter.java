package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.SiteNoticeView;

@Presenter(view = SiteNoticeView.class)
public class SiteNoticePresenter extends BasePresenter<SiteNoticePresenter.IView, AppEventBus> {

	public interface IView extends IsWidget {
		
	}
	
	public void onSiteNotice() {
		eventBus.setContent(view);
		eventBus.hideInitialLoadingIndicator();
	}
	
}
