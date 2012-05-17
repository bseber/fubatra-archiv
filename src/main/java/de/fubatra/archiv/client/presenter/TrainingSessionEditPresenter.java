package de.fubatra.archiv.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.CyclePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.TrainingSessionEditView;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.UserInfoProxy;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.TrainingSessionServiceRequest;

@Presenter(view = TrainingSessionEditView.class)
public class TrainingSessionEditPresenter extends CyclePresenter<TrainingSessionEditView, AppEventBus> implements TrainingSessionEditView.IPresenter {
	
	private RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionEditView> editorDriver;
	
	private final AppRequestFactorySecured authRequestFactory;
	private final AppRequestFactory requestFactory;

	@Inject
	public TrainingSessionEditPresenter(AppRequestFactorySecured authRequestFactory, AppRequestFactory requestFactory) {
		this.authRequestFactory = authRequestFactory;
		this.requestFactory = requestFactory;
	}

	public void onCreateTrainingSession() {
		startEditing();
		eventBus.setContent(view);
		eventBus.hideInitialLoadingIndicator();
	}
	
	public void onEditTrainingSession(long id) {
		requestFactory.trainingSessionServiceRequest().findById(id).with(view.editorDriver().getPaths()).fire(new Receiver<TrainingSessionProxy>() {
			@Override
			public void onSuccess(TrainingSessionProxy response) {
				if (response == null) {
					eventBus.entityNotFound();
					return;
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
		});
	}
	
	@Override
	public void onUnload() {
		// clear all input fields
		startEditing();
		view.setLoadingCircleVisible(false);
		view.setSavedInfoText("");
	}
	
	@Override
	public void onSaveClicked() {
		save();
	}
	
	private void startEditing() {
		startEditing(null);
	}
	
	private void startEditing(TrainingSessionProxy proxy) {
		TrainingSessionServiceRequest saveContext = requestFactory.trainingSessionServiceRequest();
		if (proxy == null) {
			proxy = createNewProxy(saveContext);
		}
		editorDriver = view.editorDriver();
		saveContext.save(proxy).with(editorDriver.getPaths()).to(createSaveReceiver(proxy));
		editorDriver.edit(proxy, saveContext);
		editorDriver.flush();
	}
	
	private TrainingSessionProxy createNewProxy(TrainingSessionServiceRequest context) {
		final TrainingSessionProxy session = context.create(TrainingSessionProxy.class);
		
		authRequestFactory.userServiceRequestSecured().getCurrentUser().fire(new Receiver<UserInfoProxy>() {
			@Override
			public void onSuccess(UserInfoProxy response) {
				session.setOwner(response);
			}
		});
		
		return session;
	}

	private Receiver<Long> createSaveReceiver(final TrainingSessionProxy editProxy) {
		return new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				view.setLoadingCircleVisible(false);
				view.setSavedInfoText("Trainingseinheit gespeichert");
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				view.setLoadingCircleVisible(false);
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen...");
				super.onFailure(error);
			}
		};
	}

	private void save() {
		RequestContext context = editorDriver.flush();
		if (editorDriver.hasErrors()) {
			return;
		}
		if (!context.isChanged()) {
			return;
		}
		view.setLoadingCircleVisible(true);
		view.setSavedInfoText("");
		context.fire();
	}

}
