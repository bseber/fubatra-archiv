package de.fubatra.archiv.client.presenter;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.UserInfoSeasonView;
import de.fubatra.archiv.client.ui.view.UserInfoSeasonView.Driver;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;
import de.fubatra.archiv.shared.service.AppRequestFactorySecured;
import de.fubatra.archiv.shared.service.UserServiceRequestSecured;

@Presenter(view = UserInfoSeasonView.class, multiple = true)
public class UserInfoSeasonPresenter extends BasePresenter<UserInfoSeasonView, AppEventBus> implements UserInfoSeasonView.IPresenter {
	
	private Driver editorDriver;
	
	private UserInfoSeasonProxy seasonProxy;
	private boolean editing;
	
	private final AppRequestFactorySecured requestFactorySecured;

	@Inject
	public UserInfoSeasonPresenter(AppRequestFactorySecured requestFactorySecured) {
		this.requestFactorySecured = requestFactorySecured;
	}
	
	@Override
	public void bind() {
		view.setPresenter(this);
	}
	
	public void onCreateNewSeason(FlowPanel parent, long ownerId) {
		UserServiceRequestSecured service = requestFactorySecured.userServiceRequestSecured();
		seasonProxy = service.create(UserInfoSeasonProxy.class);
		service.addSeasonToUser(ownerId, seasonProxy).to(new Receiver<UserInfoSeasonProxy>() {
			@Override
			public void onSuccess(UserInfoSeasonProxy response) {
				startEditing(response);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
				super.onFailure(error);
			}
		});
		editorDriver = view.getEditorDriver();
		editorDriver.edit(seasonProxy, service);
		
		initMouseHandlers();
		view.createEditAndDeleteButton();
		view.createSaveAndCancelButton();
		view.showEditAndDeleteButton(false);
		view.showSaveAndCancelButton(true);
		view.setInputFieldsEditable(true);
		parent.insert(view, 0);
	}
	
	public void onDisplaySeason(FlowPanel parent, UserInfoSeasonProxy seasonProxy, boolean edit) {
		if (edit) {
			initMouseHandlers();
			startEditing(seasonProxy);
		} else {
			editorDriver = view.getEditorDriver();
			editorDriver.display(seasonProxy);
		}
		parent.insert(view, 0);
	}

	@Override
	public void onSaveClicked() {
		RequestContext context = editorDriver.flush();
		if (editorDriver.hasErrors()) {
			return;
		}
		if (!context.isChanged()) {
			return;
		}
		context.fire();
	}

	@Override
	public void onDeleteClicked() {
		String seasonValue = seasonProxy.getSeason();
		// TODO create custom confirm dialog
		if (Window.confirm("Saison " + seasonValue + " wirklich löschen?")) {
			requestFactorySecured.userServiceRequestSecured().deleteSeason(seasonProxy).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					view.removeFromParent();
					eventBus.removeHandler(UserInfoSeasonPresenter.this);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
					super.onFailure(error);
				}
			});
		}
	}

	@Override
	public void onCancelClicked() {
		if (editing) {
			setEditing(false);
		}
	}

	@Override
	public void onEditClicked() {
		if (!editing) {
			setEditing(true);
		}
	}
	
	private void initMouseHandlers() {
		view.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				view.setEditAndDeleteButtonVisible(true);
			}
		}, MouseOverEvent.getType());
		
		view.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				view.setEditAndDeleteButtonVisible(editing);
			}
		}, MouseOutEvent.getType());
	}
	
	private void setEditing(boolean editing) {
		this.editing = editing;
		view.showEditAndDeleteButton(!editing);
		view.showSaveAndCancelButton(editing);
		view.setInputFieldsEditable(editing);
	}
	
	private void startEditing(UserInfoSeasonProxy seasonProxy) {
		this.seasonProxy = seasonProxy;
		
		UserServiceRequestSecured service = requestFactorySecured.userServiceRequestSecured();
		UserInfoSeasonProxy toEdit = service.edit(seasonProxy);
		
		service.saveSeason(toEdit).to(new Receiver<UserInfoSeasonProxy>() {
			@Override
			public void onSuccess(UserInfoSeasonProxy response) {
				startEditing(response);
			}
		});
		editorDriver = view.getEditorDriver();
		editorDriver.edit(toEdit, service);
		
		view.createEditAndDeleteButton();
		view.createSaveAndCancelButton();
		view.setEditAndDeleteButtonVisible(false);
		setEditing(false);
	}
	
}
