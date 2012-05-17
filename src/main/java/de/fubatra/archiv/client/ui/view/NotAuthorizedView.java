package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.presenter.NotAuthorizedPresenter;

public class NotAuthorizedView extends Composite implements NotAuthorizedPresenter.IView {

	private static NotAuthorizedViewUiBinder uiBinder = GWT.create(NotAuthorizedViewUiBinder.class);

	interface NotAuthorizedViewUiBinder extends UiBinder<Widget, NotAuthorizedView> {
	}

	public NotAuthorizedView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
