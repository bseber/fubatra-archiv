package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.LazyView;
import com.mvp4g.client.view.ReverseViewInterface;

public class UsersTrainingSessionsView extends Composite implements LazyView, ReverseViewInterface<UsersTrainingSessionsView.IPresenter> {

	private static UsersTrainingSessionsViewUiBinder uiBinder = GWT.create(UsersTrainingSessionsViewUiBinder.class);

	public interface IPresenter {
		
	}
	
	interface UsersTrainingSessionsViewUiBinder extends UiBinder<Widget, UsersTrainingSessionsView> {
	}

	private IPresenter presenter;
	
	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IPresenter getPresenter() {
		return presenter;
	}

}
