package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.presenter.SiteNoticePresenter;

public class SiteNoticeView extends Composite implements SiteNoticePresenter.IView {

	private static SiteNoticeViewUiBinder uiBinder = GWT.create(SiteNoticeViewUiBinder.class);

	interface SiteNoticeViewUiBinder extends UiBinder<Widget, SiteNoticeView> {
	}

	public SiteNoticeView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
