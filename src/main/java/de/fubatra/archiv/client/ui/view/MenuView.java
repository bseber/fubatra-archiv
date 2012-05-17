package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import de.fubatra.archiv.client.presenter.MenuPresenter;
import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.widgets.ImageHyperlink;

public class MenuView extends Composite implements MenuPresenter.IView {

	private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

	interface MenuViewUiBinder extends UiBinder<Widget, MenuView> {
	}
	
	@UiField
	HTMLPanel container;
	
	@UiField
	HTMLPanel leftPanel;
	
	@UiField
	HTMLPanel rightPanel;

//	@UiField
//	SimplePanel usersTrainingSessionsContainer;
	
	@UiField
	SimplePanel createTrainingSessionContainer;
	
	@UiField
	ImageHyperlink btnSiteNotice;
	
	@UiField
	SimplePanel profileContainer;
	
	@UiField
	SimplePanel logoutContainer;
	
	@UiField
	SimplePanel loginContainer;
	
	private ImageHyperlink btnCreateNewTrainingSession;
	private ImageHyperlink btnListUsersSessions;
	private ImageHyperlink btnLogin;
	private ImageHyperlink btnLogout;
	private ImageHyperlink btnProfile;
	
	private final AppResources resources;
	
	@Inject
	public MenuView(AppResources resources) {
		this.resources = resources;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void showCreateTrainingSessionButton(String historyToken) {
		if (btnCreateNewTrainingSession == null) {
			btnCreateNewTrainingSession = new ImageHyperlink(resources.document());
			btnCreateNewTrainingSession.setTargetHistoryToken(historyToken);
			btnCreateNewTrainingSession.setText("Trainingseinheit erstellen");
			createTrainingSessionContainer.add(btnCreateNewTrainingSession);
		}
	}
	
	@Override
	public void showListUsersTrainingSessionsButton(String targetHistoryToken) {
		if (btnListUsersSessions == null) {
			btnListUsersSessions = new ImageHyperlink(resources.currentUsersTrainingSessions());
			btnListUsersSessions.setTargetHistoryToken(targetHistoryToken);
			btnListUsersSessions.setText("Meine Trainingseinheiten");
//			usersTrainingSessionsContainer.add(btnListUsersSessions);
		}
	}
	
	@Override
	public void showLoginButton(String targetHistoryToken) {
		if (btnLogin == null) {
			btnLogin = new ImageHyperlink(resources.login());
			btnLogin.setText("Einloggen");
			btnLogin.setTargetHistoryToken(targetHistoryToken);
			loginContainer.add(btnLogin);
		}
	}

	@Override
	public void showLogoutButton(String targetHistoryToken) {
		if (btnLogout == null) {
			btnLogout = new ImageHyperlink(resources.logout());
			btnLogout.setText("Ausloggen");
			btnLogout.setTargetHistoryToken(targetHistoryToken);
			logoutContainer.add(btnLogout);
		}
	}

	@Override
	public void showProfileButton(String userEmail, String targetHistoryToken) {
		if (btnProfile == null) {
			btnProfile = new ImageHyperlink(resources.man());
			btnProfile.setTargetHistoryToken(targetHistoryToken);
			btnProfile.setText(userEmail);
			profileContainer.add(btnProfile);
		}
	}
	
	@Override
	public void setSiteNoticeHistoryToken(String historyToken) {
		btnSiteNotice.setTargetHistoryToken(historyToken);
	}
	
}