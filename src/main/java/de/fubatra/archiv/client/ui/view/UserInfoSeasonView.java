package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.mvp4g.client.view.ReverseViewInterface;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.widgets.EditableLabel;
import de.fubatra.archiv.client.ui.widgets.EditableTeamListBox;
import de.fubatra.archiv.shared.domain.UserInfoSeasonProxy;

public class UserInfoSeasonView extends Composite implements Editor<UserInfoSeasonProxy>, ReverseViewInterface<UserInfoSeasonView.IPresenter> {

	private static UserInfoSeasonUiBinder uiBinder = GWT.create(UserInfoSeasonUiBinder.class);

	public interface IPresenter {

		void onSaveClicked();

		void onDeleteClicked();

		void onCancelClicked();

		void onEditClicked();
	}

	public interface Driver extends RequestFactoryEditorDriver<UserInfoSeasonProxy, UserInfoSeasonView> {
	}
	
	interface UserInfoSeasonUiBinder extends UiBinder<Widget, UserInfoSeasonView> {
	}

	@UiField
	Grid grid;
	
	@UiField
	EditableLabel season;
	
	@UiField
	EditableLabel club;
	
	@UiField
	EditableTeamListBox team;
	
	@UiField
	FlowPanel buttonContainer;

	private Image btnEdit;
	private Image btnDelete;
	private Image btnSave;
	private Image btnCancel;

	private Driver driver;
	private IPresenter presenter;
	
	private final AppResources resources;
	
	@Inject
	public UserInfoSeasonView(AppResources resources) {
		this.resources = resources;
		initWidget(uiBinder.createAndBindUi(this));
		grid.setWidth("100%");
		grid.getColumnFormatter().getElement(0).getStyle().setWidth(7, Unit.EM);
		grid.getColumnFormatter().getElement(1).getStyle().setWidth(20, Unit.EM);
		grid.getColumnFormatter().getElement(2).getStyle().setWidth(10, Unit.EM);
	}

	public Driver getEditorDriver() {
		if (driver == null) {
			driver = GWT.create(Driver.class);
		}
		driver.initialize(this);
		return driver;
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IPresenter getPresenter() {
		return presenter;
	}

	private Image createImageButton(ImageResource imageResource, String title) {
		Image image = AbstractImagePrototype.create(imageResource).createImage();
		image.setTitle(title);
		image.getElement().getStyle().setCursor(Cursor.POINTER);
		return image;
	}
	
	public void createEditAndDeleteButton() {
		btnEdit = createImageButton(resources.edit16(), "Bearbeiten");
		btnEdit.getElement().getStyle().setMarginRight(0.25, Unit.EM);
		btnEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onEditClicked();
			}
		});
		
		btnDelete = createImageButton(resources.delete16(), "Löschen");
		btnDelete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onDeleteClicked();
			}
		});
		
		buttonContainer.add(btnEdit);
		buttonContainer.add(btnDelete);
	}
	
	public void createSaveAndCancelButton() {
		if (btnSave == null) {
			btnSave = createImageButton(resources.save16(), "Speichern");
			btnSave.getElement().getStyle().setMarginRight(0.25, Unit.EM);
			btnSave.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.onSaveClicked();
				}
			});
		}
		if (btnCancel == null) {
			btnCancel = createImageButton(resources.cancel16(), "Abbrechen");
			btnCancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.onCancelClicked();
				}
			});
		}
	}

	public void showEditAndDeleteButton(boolean show) {
		buttonContainer.clear();
		if (show) {
			buttonContainer.add(btnEdit);
			buttonContainer.add(btnDelete);
		} else {
			buttonContainer.add(btnSave);
			buttonContainer.add(btnCancel);
		}
	}
	
	public void showSaveAndCancelButton(boolean show) {
		buttonContainer.clear();
		if (show) {
			buttonContainer.add(btnSave);
			buttonContainer.add(btnCancel);
		} else {
			buttonContainer.add(btnEdit);
			buttonContainer.add(btnDelete);
		}
	}

	public void setEditAndDeleteButtonVisible(boolean visible) {
		btnEdit.setVisible(visible);
		btnDelete.setVisible(visible);
	}

	public void setInputFieldsEditable(boolean editable) {
		season.setEditable(editable);
		club.setEditable(editable);
		team.setEditable(editable);
	}
	
}