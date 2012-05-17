package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.presenter.LogoutInfoPresenter;

public class LogoutInfoView extends Composite implements LogoutInfoPresenter.IView {

	private static LogoutInfoViewUiBinder uiBinder = GWT.create(LogoutInfoViewUiBinder.class);

	interface LogoutInfoViewUiBinder extends UiBinder<Widget, LogoutInfoView> {
	}

	@UiField
	Anchor btnLogout;
	
	public LogoutInfoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setLogoutUrl(String href) {
		btnLogout.setHref(href);
	}
	
	@UiHandler("btnLogout")
	void onLoginClicked(ClickEvent event) {
		String href = btnLogout.getHref();
		if (href.isEmpty() || href.endsWith("#")) {
			event.preventDefault();
			event.stopPropagation();
		}
	}

}
