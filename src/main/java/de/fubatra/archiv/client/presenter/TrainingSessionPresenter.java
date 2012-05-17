package de.fubatra.archiv.client.presenter;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.CyclePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.TrainingSessionPostView;
import de.fubatra.archiv.client.ui.view.TrainingSessionView;
import de.fubatra.archiv.client.util.UserUtil;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.TrainingSessionPostProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.TrainingSessionServiceRequest;
import de.fubatra.archiv.shared.service.UserServiceRequestSecured;

@Presenter(view = TrainingSessionView.class)
public class TrainingSessionPresenter extends CyclePresenter<TrainingSessionView, AppEventBus> implements TrainingSessionView.IPresenter {

	private final AppRequestFactorySecured requestFactorySecured;
	private final AppRequestFactory requestFactory;
	
	private TrainingSessionProxy trainingSessionProxy;
	private UserInfoProxy currentUser;
	private RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionView> editorDriver;

	@Inject
	public TrainingSessionPresenter(AppRequestFactorySecured requestFactorySecured, AppRequestFactory requestFactory) {
		this.requestFactorySecured = requestFactorySecured;
		this.requestFactory = requestFactory;
	}

	@Override
	public void createPost(String text) {
		if (text == null || text.trim().isEmpty()) {
			return;
		}
		TrainingSessionServiceRequest service = requestFactory.trainingSessionServiceRequest();
		Request<TrainingSessionPostProxy> request = service.addPost(trainingSessionProxy.getId(), text).with("userInfo");
		request.fire(new Receiver<TrainingSessionPostProxy>() {
			@Override
			public void onSuccess(TrainingSessionPostProxy response) {
				view.clearCommentTextArea();
				view.postsEditor().getList().add(response);
			}
		});
	}
	
	@Override
	public void onDeletePostClicked(final TrainingSessionPostProxy post) {
		if (isSameUser(currentUser, post.getUserInfo())) {
			// TODO use customized confirm dialog
			if (Window.confirm("Kommentar [" + post.getText() + "] löschen?")) {
				requestFactory.trainingSessionServiceRequest().deletePost(post).fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						view.postsEditor().getList().remove(post);
					}
				});
			}
		}
	}
	
	@Override
	public void onDeleteTrainingSessionClicked() {
		if (isSameUser(currentUser, trainingSessionProxy.getOwner())) {
			// TODO use customized confirm dialog
			if (Window.confirm("Trainingseinheit wirklich löschen?")) {
				requestFactory.trainingSessionServiceRequest().deleteTrainingSession(trainingSessionProxy).fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						eventBus.startPage();
					}
				});
			}
		}
	}
	
	@Override
	public void onEditPostClicked(TrainingSessionPostView postView) {
		if (isSameUser(currentUser, postView.getPostOwnerProxy())) {
			postView.toggleEditMode();
		}
	}
	
	@Override
	public void onOwnerNameClicked() {
		Long ownerId = trainingSessionProxy.getOwner().getId();
		eventBus.profile(ownerId);
	}
	
	@Override
	public void onPostViewMouseOut(TrainingSessionPostView postView) {
		postView.setButtonPanelVisible(false);
	}

	@Override
	public void onPostViewMouseOver(TrainingSessionPostView postView) {
		if (isSameUser(currentUser, postView.getPostOwnerProxy())) {
			postView.setButtonPanelVisible(true);
		}
	}
	
	public void onShowTrainingSession(final long trainingSessionId) {
		editorDriver = view.editorDriver();
		
		TrainingSessionServiceRequest trainingService = requestFactory.trainingSessionServiceRequest();
		String[] paths = editorDriver.getPaths();
		trainingService.findById(trainingSessionId).with(paths).fire(new Receiver<TrainingSessionProxy>() {
			@Override
			public void onSuccess(TrainingSessionProxy response) {
				trainingSessionProxy = response;
				
				if (response == null) {
					eventBus.entityNotFound();
					return;
				}
				
				if (UserUtil.isUserLoggedIn()) {
					UserServiceRequestSecured userService = requestFactorySecured.userServiceRequestSecured();
					userService.getCurrentUsersRatingOfTrainingSession(trainingSessionId).to(new Receiver<Rating>() {
						@Override
						public void onSuccess(Rating response) {
							view.setCurrentUsersRating(response);
						}
					});
					userService.getCurrentUser().to(new Receiver<UserInfoProxy>() {
						@Override
						public void onSuccess(UserInfoProxy response) {
							currentUser = response;
							boolean currentUserIsOwner = response.getId() == trainingSessionProxy.getOwner().getId();
							if (currentUserIsOwner) {
								view.showTrainingSessionDeleteAndEditButton(getTokenGenerator().editTrainingSession(trainingSessionProxy.getId()));
							}
						}
					});
					userService.fire();
					
					view.showCommentTextArea();
				} else {
					view.showLoggedInInfoForRating();
				}
				
				editorDriver.display(trainingSessionProxy);
				eventBus.setContent(view);
				eventBus.hideInitialLoadingIndicator();
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
				super.onFailure(error);
			}
		});
	}
	
	@Override
	public void onUserNameClicked(UserInfoProxy user) {
		eventBus.profile(user.getId());
	}
	
	@Override
	public void saveUserRating(Rating rating) {
		TrainingSessionServiceRequest service = requestFactory.trainingSessionServiceRequest();
		String[] paths = editorDriver.getPaths();
		service.setCurrentUsersRatingOfTrainingSession(trainingSessionProxy.getId(), rating).with(paths).fire(new Receiver<TrainingSessionProxy>() {
			@Override
			public void onSuccess(TrainingSessionProxy response) {
				trainingSessionProxy = response;
				editorDriver.display(trainingSessionProxy);
			}
		});
	}

	@Override
	public void updatePost(long postId, String text) {
		TrainingSessionServiceRequest service = requestFactory.trainingSessionServiceRequest();
		Request<TrainingSessionPostProxy> request = service.updatePost(trainingSessionProxy, postId, text).with("userInfo");
		request.fire(new Receiver<TrainingSessionPostProxy>() {
			@Override
			public void onSuccess(TrainingSessionPostProxy response) {
				if (response == null) {
					// TODO error message
					return;
				}
				view.clearCommentTextArea();
				List<TrainingSessionPostProxy> list = view.postsEditor().getList();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId().equals(response.getId())) {
						view.postsEditor().getEditors().get(i).toggleEditMode();
						list.set(i, response);
						return;
					}
				}
			}
		});
	}
	
	private boolean isSameUser(UserInfoProxy one, UserInfoProxy two) {
		if (one == null || two == null) {
			return false;
		}
		return one.getId().equals(two.getId());
	}

}
