package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.mvp4g.client.view.BaseCycleView;
import com.mvp4g.client.view.ReverseViewInterface;

import de.fubatra.archiv.client.events.HasSavePictureEventHandlers;
import de.fubatra.archiv.client.events.SavePictureEvent.Handler;
import de.fubatra.archiv.client.ui.resources.AppCellTableRecources;
import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.widgets.EditableAnchor;
import de.fubatra.archiv.client.ui.widgets.EditableLabel;
import de.fubatra.archiv.client.ui.widgets.PictureEditor;
import de.fubatra.archiv.client.ui.widgets.cell.TrainingSessionRatingCell;
import de.fubatra.archiv.client.util.ListToStringUtil;
import de.fubatra.archiv.client.util.ViewUtil;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.UserInfoProxy;

public class UserProfileView extends BaseCycleView implements Editor<UserInfoProxy>, HasSavePictureEventHandlers,
		ReverseViewInterface<UserProfileView.IPresenter>, ResizeHandler {

	private static UserProfileViewUiBinder uiBinder = GWT.create(UserProfileViewUiBinder.class);
	private static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");

	public interface IPresenter {

		void save();
		
		void onAddSeasonClicked();
	}

	interface Driver extends RequestFactoryEditorDriver<UserInfoProxy, UserProfileView> {
	}

	interface UserProfileViewUiBinder extends UiBinder<Widget, UserProfileView> {
	}
	
	@UiField
	HTMLPanel containerPanel;
	
	@UiField
	HTMLPanel centerPanel;
	
	@UiField
	HTMLPanel pictureContainer;

	@UiField(provided = true)
	PictureEditor picture;
	
	@UiField
	HTMLPanel inputContainer;

	@UiField
	EditableLabel name;
	
	@UiField
	EditableAnchor googlePlusUrl;
	
	@UiField
	EditableAnchor facebookUrl;

	@UiField
	FlowPanel seasonHistoryButtonContainer;
	
	@UiField
	FlowPanel seasonHistoryEditorContainer;
	
	@UiField
	HTMLPanel eastPanel;
	
	@UiField
	HTMLPanel eastInnerPanel;
	
	@UiField
	SimplePanel bestRatedContainer;
	
	@UiField
	SimplePanel usersRecentAddedContainer;
	
	private Button btnAddSeason;
	private CellTable<TrainingSessionProxy> recentPublicSessionsTable;
	private CellTable<TrainingSessionProxy> bestRatedTable;
	
	private Driver driver;
	private IPresenter presenter;
	private HandlerRegistration resizeHandler;
	
	private final AppResources resources;
	private final AppCellTableRecources tableRecources;

	@Inject
	public UserProfileView(AppResources resources, AppCellTableRecources tableRecources) {
		this.resources = resources;
		this.tableRecources = tableRecources;
	}

	@Override
	public void createView() {
		picture = new PictureEditor(resources.placeholderMan());
		initWidget(uiBinder.createAndBindUi(this));
		
		bestRatedTable = createBestRatedSessionsTable();
		bestRatedContainer.add(bestRatedTable);

		recentPublicSessionsTable = createRecentAddedSessionsTable();
		usersRecentAddedContainer.add(recentPublicSessionsTable);
	}

	@Override
	public HandlerRegistration addSavePictureEventHandler(Handler handler) {
		return picture.addSavePictureEventHandler(handler);
	}

	public RequestFactoryEditorDriver<UserInfoProxy, UserProfileView> editorDriver() {
		if (driver == null) {
			driver = GWT.create(Driver.class);
		}
		driver.initialize(this);
		return driver;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		resizeHandler = Window.addResizeHandler(this);
		checkEastPanelPosition();
	}
	
	@Override
	public void onUnload() {
		super.onUnload();
		resizeHandler.removeHandler();
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IPresenter getPresenter() {
		return presenter;
	}
	
	public String getNameValue() {
		return name.getValue();
	}

	public HasData<TrainingSessionProxy> getBestRatedSessionsTable() {
		return bestRatedTable;
	}
	
	public HasData<TrainingSessionProxy> getRecentAddedSessionsTable() {
		return recentPublicSessionsTable;
	}

	@Override
	public void onResize(ResizeEvent event) {
		checkEastPanelPosition();
	}

	public void setEditMode(boolean edit) {
		name.setEditable(edit);
		googlePlusUrl.setEditable(edit);
		facebookUrl.setEditable(edit);
		picture.setEditable(edit);
		if (edit) {
			initAddSeasonButton();
			if (!btnAddSeason.isAttached()) {
				seasonHistoryButtonContainer.add(btnAddSeason);
			}
		} else {
			if (btnAddSeason != null && btnAddSeason.isAttached()) {
				btnAddSeason.removeFromParent();
			}
		}
	}

	public FlowPanel getSeasonHistoryEditorContainer() {
		return seasonHistoryEditorContainer;
	}
	
	@UiHandler({"name", "googlePlusUrl", "facebookUrl"})
	void onInputFieldBlur(BlurEvent event) {
		presenter.save();
	}
	
	private void checkEastPanelPosition() {
		ViewUtil.checkEastPanelPosition(containerPanel.getOffsetWidth(), centerPanel.getOffsetWidth(), eastPanel, eastInnerPanel);
	}
	
	private CellTable<TrainingSessionProxy> createRecentAddedSessionsTable() {
		TextColumn<TrainingSessionProxy> createdColumn = new TextColumn<TrainingSessionProxy>() {
			@Override
			public String getValue(TrainingSessionProxy object) {
				return dateTimeFormat.format(object.getCreationDate());
			}
		};

		TextColumn<TrainingSessionProxy> topicColumn = new TextColumn<TrainingSessionProxy>() {
			@Override
			public String getValue(TrainingSessionProxy object) {
				return object.getTopic();
			}
		};
		
		TextColumn<TrainingSessionProxy> teamsColumn = new TextColumn<TrainingSessionProxy>() {
			@Override
			public String getValue(TrainingSessionProxy object) {
				return ListToStringUtil.teamListToString(object.getTeams());
			}
		};
		
		CellTable<TrainingSessionProxy> table = new CellTable<TrainingSessionProxy>(50, tableRecources, new TrainingSessionProxy.KeyProvider());
		table.setEmptyTableWidget(new Label("Noch keine Trainingseinheit veröffentlicht"));
		table.addColumn(createdColumn, "Datum");
		table.addColumn(topicColumn, "Titel");
		table.addColumn(teamsColumn, "Altersklasse");
		table.setColumnWidth(createdColumn, 7, Unit.EM);
		table.setColumnWidth(topicColumn, 15, Unit.EM);
		table.setColumnWidth(teamsColumn, 20, Unit.EM);
		table.setSelectionModel(new NoSelectionModel<TrainingSessionProxy>());
		return table;
	}
	
	private CellTable<TrainingSessionProxy> createBestRatedSessionsTable() {
		Column<TrainingSessionProxy, Rating> ratingColumn = new Column<TrainingSessionProxy, Rating>(new TrainingSessionRatingCell(resources)) {
			@Override
			public Rating getValue(TrainingSessionProxy object) {
				return object.getAverageRating();
			}
		};
		
		TextColumn<TrainingSessionProxy> topicColumn = new TextColumn<TrainingSessionProxy>() {
			@Override
			public String getValue(TrainingSessionProxy object) {
				return object.getTopic();
			}
		};
		
		TextColumn<TrainingSessionProxy> teamsColumn = new TextColumn<TrainingSessionProxy>() {
			@Override
			public String getValue(TrainingSessionProxy object) {
				return ListToStringUtil.teamListToString(object.getTeams());
			}
		};
		
		CellTable<TrainingSessionProxy> table = new CellTable<TrainingSessionProxy>(50, tableRecources, new TrainingSessionProxy.KeyProvider());
		table.setEmptyTableWidget(new Label("Noch keine Trainingseinheit veröffentlicht"));
		table.addColumn(ratingColumn, "Bewertung");
		table.addColumn(topicColumn, "Titel");
		table.addColumn(teamsColumn, "Altersklasse");
		table.setColumnWidth(ratingColumn, 7, Unit.EM);
		table.setColumnWidth(topicColumn, 15, Unit.EM);
		table.setColumnWidth(teamsColumn, 20, Unit.EM);
		table.setSelectionModel(new NoSelectionModel<TrainingSessionProxy>());
		return table;
	}
	
	private void initAddSeasonButton() {
		if (btnAddSeason == null) {
			btnAddSeason = new Button("Neue Saison hinzufügen");
			btnAddSeason.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.onAddSeasonClicked();
				}
			});
		}
	}

	public void setUserNameError(boolean error) {
		if (error) {
			Element el = name.getElement().getChild(0).cast();
			el.getStyle().setBackgroundColor(AppResources.ERROR_BG_COLOR);
			// TODO text info about used name
		} else {
			Element el = name.getElement().getChild(0).cast();
			el.getStyle().clearBackgroundColor();
		}
	}

}
