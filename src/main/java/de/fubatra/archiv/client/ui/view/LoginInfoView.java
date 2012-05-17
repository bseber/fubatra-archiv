package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.presenter.LoginInfoPresenter;

public class LoginInfoView extends Composite implements LoginInfoPresenter.IView {

	private static LoginInfoViewUiBinder uiBinder = GWT.create(LoginInfoViewUiBinder.class);

	interface LoginInfoViewUiBinder extends UiBinder<Widget, LoginInfoView> {
	}

	@UiField
	Anchor btnLogin;
	
	public LoginInfoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setLoginButtonHref(String href) {
		btnLogin.setHref(href);
	}
	
	@Override
	public void setLoginButtonText(String text) {
		btnLogin.setText(text);
	}
	
	@UiHandler("btnLogin")
	void onLoginClicked(ClickEvent event) {
		String href = btnLogin.getHref();
		if (href.isEmpty() || href.endsWith("#")) {
			event.preventDefault();
			event.stopPropagation();
		}
	}

}
