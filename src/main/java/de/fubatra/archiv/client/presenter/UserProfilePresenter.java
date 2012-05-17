package de.fubatra.archiv.client.presenter;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.CyclePresenter;

import de.fubatra.archiv.client.events.SavePictureEvent;
import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.UserProfileView;
import de.fubatra.archiv.client.util.UserUtil;
import de.fubatra.archiv.client.util.ViewUtil;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.TrainingSessionServiceRequest;
import de.fubatra.archiv.shared.service.UserServiceRequest;
import de.fubatra.archiv.shared.service.UserServiceRequestSecured;

@Presenter(view = UserProfileView.class)
public class UserProfilePresenter extends CyclePresenter<UserProfileView, AppEventBus> implements UserProfileView.IPresenter, NavigationConfirmationInterface {

	private boolean editable;
	private String originalUserName;
	private UserInfoProxy editedUserInfo;
	
	private RequestFactoryEditorDriver<UserInfoProxy, UserProfileView> editorDriver;
	private HandlerRegistration savePictureEventHandler;
	private UserServiceRequestSecured editRequestContext;
	
	private final AppRequestFactory requestFactory;
	private final AppRequestFactorySecured requestFactorySecured;

	private final ListDataProvider<TrainingSessionProxy> bestRatedSessionsProvider;
	private final NoSelectionModel<TrainingSessionProxy> bestRatedSessionsSelectionModel;
	private final ListDataProvider<TrainingSessionProxy> recentAddedSessionsProvider;
	private final NoSelectionModel<TrainingSessionProxy> recentAddedSessionsSelectionModel;

	@Inject
	public UserProfilePresenter(AppRequestFactorySecured requestFactorySecured, AppRequestFactory requestFactory) {
		this.requestFactorySecured = requestFactorySecured;
		this.requestFactory = requestFactory;

		bestRatedSessionsProvider = new ListDataProvider<TrainingSessionProxy>(new TrainingSessionProxy.KeyProvider());
		bestRatedSessionsSelectionModel = new NoSelectionModel<TrainingSessionProxy>();
		
		recentAddedSessionsProvider = new ListDataProvider<TrainingSessionProxy>(new TrainingSessionProxy.KeyProvider());
		recentAddedSessionsSelectionModel = new NoSelectionModel<TrainingSessionProxy>();
	}

	@Override
	public void bindView() {
		bestRatedSessionsSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Long sessionId = bestRatedSessionsSelectionModel.getLastSelectedObject().getId();
				eventBus.showTrainingSession(sessionId);
			}
		});
		recentAddedSessionsSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Long sessionId = recentAddedSessionsSelectionModel.getLastSelectedObject().getId();
				eventBus.showTrainingSession(sessionId);
			}
		});
		view.getBestRatedSessionsTable().setSelectionModel(bestRatedSessionsSelectionModel);
		view.getRecentAddedSessionsTable().setSelectionModel(recentAddedSessionsSelectionModel);
	}
	
	@Override
	public void confirm(NavigationEventCommand event) {
		if (!editable || editedUserInfo.isProfileInitiated()) {
			event.fireEvent();
		} else {
			view.setUserNameError(true);
			// TODO replace with customized dialog
			Window.alert("Bitte einen Benutzernamen eingeben");
		}
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		savePictureEventHandler = view.addSavePictureEventHandler(new SavePictureEvent.Handler() {
			@Override
			public void onSavePicture(SavePictureEvent event) {
				save();
			}
		});
	}
	
	@Override
	public void onUnload() {
		super.onUnload();
		savePictureEventHandler.removeHandler();
	}
	
	@Override
	public void onAddSeasonClicked() {
		if (editable) {
			eventBus.createNewSeason(view.getSeasonHistoryEditorContainer(), editedUserInfo.getId());
		}
	}
	
	public void onProfile(final long userId) {
		editable = false;
		view.setEditMode(false);

		Receiver<UserInfoProxy> userInfoReceiver = new Receiver<UserInfoProxy>() {
			@Override
			public void onSuccess(UserInfoProxy response) {
				if (response == null) {
					eventBus.entityNotFound();
					return;
				}
				
				editable = response.getId() == UserUtil.getUserId();
				view.setEditMode(editable);
				
				if (editable) {
					eventBus.setNavigationConfirmation(UserProfilePresenter.this);
				}
				
				startEditing(response);
				
				eventBus.setContent(view);
				eventBus.hideInitialLoadingIndicator();
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
				super.onFailure(error);
			}
		};
		
		Receiver<List<UserInfoSeasonProxy>> seasonsReceiver = new Receiver<List<UserInfoSeasonProxy>>() {
			@Override
			public void onSuccess(List<UserInfoSeasonProxy> response) {
				boolean editable = userId == UserUtil.getUserId();
				FlowPanel editorContainer = view.getSeasonHistoryEditorContainer();
				for (UserInfoSeasonProxy p : response) {
					eventBus.displaySeason(editorContainer, p, editable);
				}
			}
		};

		ViewUtil.removeDataDisplay(recentAddedSessionsProvider, view.getRecentAddedSessionsTable());
		Receiver<List<TrainingSessionProxy>> recentSessionsReceiver = new Receiver<List<TrainingSessionProxy>>() {
			@Override
			public void onSuccess(List<TrainingSessionProxy> response) {
				recentAddedSessionsProvider.setList(response);
				ViewUtil.addDataDisplay(recentAddedSessionsProvider, view.getRecentAddedSessionsTable());
			}
		};
		
		ViewUtil.removeDataDisplay(bestRatedSessionsProvider, view.getBestRatedSessionsTable());
		Receiver<List<TrainingSessionProxy>> bestRatedSessionsReceiver = new Receiver<List<TrainingSessionProxy>>() {
			@Override
			public void onSuccess(List<TrainingSessionProxy> response) {
				bestRatedSessionsProvider.setList(response);
				ViewUtil.addDataDisplay(bestRatedSessionsProvider, view.getBestRatedSessionsTable());
			}
		};
		
		String[] editorPaths = view.editorDriver().getPaths();
		UserServiceRequest userService = requestFactory.userServiceRequest();
		userService.findUserById(userId).with(editorPaths).to(userInfoReceiver);
		userService.findSeasonsByUserId(userId).to(seasonsReceiver);
		
		TrainingSessionServiceRequest sessionService = userService.append(requestFactory.trainingSessionServiceRequest());
		sessionService.findRecentPublicSessionsOfOwner(userId, 0, 5).to(recentSessionsReceiver);
		sessionService.findBestRatedPublicSessionsOfOwner(userId, 0, 5).to(bestRatedSessionsReceiver);
		
		userService.fire();
	}
	
	public void save() {
		if (!editable) {
			return;
		}

		final RequestContext saveRequest = editorDriver.flush();

		if (editorDriver.hasErrors()) {
			return;
		}
		if (!saveRequest.isChanged()) {
			return;
		}

		// originalUserName is null when user is logged in the first time and has to edit his profile
		boolean userNameChanged = originalUserName == null || !originalUserName.equals(view.getNameValue());
		if (userNameChanged) {
			requestFactorySecured.userServiceRequestSecured().checkIfUsernameIsAvailable(view.getNameValue()).fire(new Receiver<Boolean>() {
				@Override
				public void onSuccess(Boolean available) {
					view.setUserNameError(!available);
					if (available) {
						saveRequest.fire();
					}
				}
			});
		} else {
			saveRequest.fire();
		}

	}
	
	private void onSaveSuccess(UserInfoProxy response) {
		startEditing(response);
	}
	
	private void startEditing(UserInfoProxy proxy) {
		originalUserName = proxy.getName();
		
		editRequestContext = requestFactorySecured.userServiceRequestSecured();
		editedUserInfo = editRequestContext.edit(proxy);
		
		editorDriver = view.editorDriver();
		
		if (editable) {
			String[] paths = editorDriver.getPaths();
			editRequestContext.save(editedUserInfo).with(paths).to(new Receiver<UserInfoProxy>() {
				@Override
				public void onSuccess(UserInfoProxy response) {
					onSaveSuccess(response);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
					super.onFailure(error);
				}
			});
			editorDriver.edit(editedUserInfo, editRequestContext);
		} else {
			editorDriver.display(editedUserInfo);
		}
	}

}
