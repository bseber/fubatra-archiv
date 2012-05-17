package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.mvp4g.client.view.BaseCycleView;
import com.mvp4g.client.view.ReverseViewInterface;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.widgets.HTMLLabel;
import de.fubatra.archiv.client.ui.widgets.ImageHyperlink;
import de.fubatra.archiv.client.ui.widgets.ImageWidget;
import de.fubatra.archiv.client.ui.widgets.RatingWidget;
import de.fubatra.archiv.client.util.ListToStringUtil;
import de.fubatra.archiv.client.util.ViewUtil;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.TrainingSessionPostProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;

public class TrainingSessionView extends BaseCycleView implements ValueAwareEditor<TrainingSessionProxy>, ReverseViewInterface<TrainingSessionView.IPresenter>, ResizeHandler {

	private static TrainingSessionViewUiBinder uiBinder = GWT.create(TrainingSessionViewUiBinder.class);

	public interface IPresenter extends TrainingSessionPostListView.IPresenter {

		void createPost(String text);
		
		void onDeleteTrainingSessionClicked();
		
		void onOwnerNameClicked();

		void saveUserRating(Rating rating);
	}
	
	interface Driver extends RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionView> {
	}
	
	interface TrainingSessionViewUiBinder extends UiBinder<Widget, TrainingSessionView> {
	}

	@UiField
	Element title;
	
	@UiField
	@Path("owner.name")
	Label ownerDisplayName;
	
	@UiField(provided = true)
	DateLabel creationDate;
	
	@UiField
	SimplePanel ratingContainer;
	
	@UiField
	SimplePanel awesomeRatingBar;
	
	@UiField
	SimplePanel awesomeRatingCountContainer;
	
	@UiField
	SimplePanel superRatingBar;
	
	@UiField
	SimplePanel superRatingCountContainer;
	
	@UiField
	SimplePanel goodRatingBar;
	
	@UiField
	SimplePanel goodRatingCountContainer;
	
	@UiField
	SimplePanel badRatingBar;
	
	@UiField
	SimplePanel badRatingCountContainer;
	
	@UiField(provided = true)
	RatingWidget averageRating;
	
	@UiField
	SimplePanel ratingCountContainer;
	
	@UiField
	@Path("draftUrl")
	ImageWidget draft;

	@UiField
	HTMLLabel description;

	@UiField(provided = true)
	TrainingSessionPostListView posts;

	@UiField
	HTMLPanel commentWrapper;

	@UiField
	SimplePanel teamsLabelContainer;
	
	@UiField
	SimplePanel subjectsLabelContainer;

	@UiField
	HTMLPanel containerPanel;
	
	@UiField
	VerticalPanel centerPanel;
	
	@UiField
	HTMLPanel eastPanel;
	
	@UiField
	HTMLPanel eastInnerPanel;
	
	@UiField
	HTMLPanel ownerActionsContainer;
	
	private Driver driver;
	private IPresenter presenter;
	
	private RatingWidget currentUsersRating;
	private TextArea commentTextArea;
	private HTML loggedInInfoForRating;
	
	private Label subjectsLabel = new Label();
	private Label teamsLabel = new Label();
	private Label awesomeCountLabel = new Label();
	private Label superCountLabel = new Label();
	private Label goodCountLabel = new Label();
	private Label badCountLabel = new Label();
	private Label ratingCountLabel = new Label();

	private boolean editAndDeleteButtonAdded;
	private HandlerRegistration resizeHandler;
	
	private final AppResources resources;

	@Inject
	public TrainingSessionView(AppResources resources) {
		this.resources = resources;
	}

	@Override
	public void createView() {
		creationDate = new DateLabel(DateTimeFormat.getFormat("dd.MM.yyyy"));
		averageRating = new RatingWidget(resources, false);
		posts = new TrainingSessionPostListView(presenter);
		initWidget(uiBinder.createAndBindUi(this));

		subjectsLabelContainer.add(subjectsLabel);
		teamsLabelContainer.add(teamsLabel);

		awesomeRatingCountContainer.add(awesomeCountLabel);
		superRatingCountContainer.add(superCountLabel);
		goodRatingCountContainer.add(goodCountLabel);
		badRatingCountContainer.add(badCountLabel);
		ratingCountContainer.add(ratingCountLabel);
		
		draft.addDomHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				checkEastPanelPosition();
			}
		}, LoadEvent.getType());
	}

	public RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionView> editorDriver() {
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
	
	public void clearCommentTextArea() {
		commentTextArea.setValue("");
	}

	public ListEditor<TrainingSessionPostProxy, TrainingSessionPostView> postsEditor() {
		return posts.asEditor();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		resizeHandler = Window.addResizeHandler(this);
	}
	
	@Override
	public void onUnload() {
		resizeHandler.removeHandler();
		super.onUnload();
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		checkEastPanelPosition();
	}

	public void setCurrentUsersRating(Rating rating) {
		if (currentUsersRating == null) {
			currentUsersRating = new RatingWidget(resources, true);
			currentUsersRating.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.saveUserRating(currentUsersRating.getValue());
				}
			});
			ratingContainer.add(currentUsersRating);
		}
		currentUsersRating.setValue(rating);
	}
	
	public void showCommentTextArea() {
		if (commentTextArea == null) {
			HTML title = new HTML("<h2>Kommentieren</h2>");
			commentTextArea = new TextArea();
			commentTextArea.setHeight("4em");
			initCommentAreaKeyUpHandler(commentTextArea);
			commentWrapper.add(title);
			commentWrapper.add(commentTextArea);
			final Label info = new Label("Eingabetaste zum Speichern");
			info.getElement().getStyle().setFontSize(0.85, Unit.EM);
			info.getElement().getStyle().setColor("gray");
			commentWrapper.add(info);
		}
	}
	
	public void showLoggedInInfoForRating() {
		if (loggedInInfoForRating == null) {
			loggedInInfoForRating = new HTML("<i>Einloggen um bewerten zu können</i>");
			ratingContainer.add(loggedInInfoForRating);
		}
	}
	
	public void showTrainingSessionDeleteAndEditButton(String editHistoryToken) {
		if (editAndDeleteButtonAdded) {
			return;
		}
		editAndDeleteButtonAdded = true;
		
		ImageHyperlink edit = new ImageHyperlink(resources.edit24());
		edit.setTargetHistoryToken(editHistoryToken);
		edit.setText("Bearbeiten");

		Anchor delete = new Anchor();
		delete.getElement().getStyle().setCursor(Cursor.POINTER);
		delete.getElement().getStyle().setTextDecoration(TextDecoration.NONE);
		delete.setText("Löschen");
		delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				presenter.onDeleteTrainingSessionClicked();
			}
		});
		
		Image deleteIcon = AbstractImagePrototype.create(resources.delete24()).createImage();
		deleteIcon.getElement().getStyle().setFloat(Float.LEFT);
		deleteIcon.getElement().getStyle().setMarginLeft(2, Unit.EM);
		deleteIcon.getElement().getStyle().setCursor(Cursor.POINTER);
		deleteIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onDeleteTrainingSessionClicked();
			}
		});
		
		FlowPanel deleteContainer = new FlowPanel();
		deleteContainer.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		deleteContainer.add(deleteIcon);
		deleteContainer.add(delete);
		
		FlowPanel panel = new FlowPanel();
		panel.setStyleName("clearfix");
		panel.add(edit);
		panel.add(deleteContainer);
		
		ownerActionsContainer.add(panel);
	}
	
	@Override
	public void setDelegate(EditorDelegate<TrainingSessionProxy> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(TrainingSessionProxy value) {
		title.setInnerText(value.getTopic());
		setSubjectNames(value);
		setTeamNames(value);
		setRatingStatistic(value);
	}
	
	@UiHandler("ownerDisplayName")
	void onOwnerNameClicked(ClickEvent event) {
		presenter.onOwnerNameClicked();
	}
	
	private void checkEastPanelPosition() {
		ViewUtil.checkEastPanelPosition(containerPanel.getOffsetWidth(), centerPanel.getOffsetWidth(), eastPanel, eastInnerPanel);
	}
	
	private void initCommentAreaKeyUpHandler(TextArea textArea) {
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				boolean isEnter = event.getNativeKeyCode() == KeyCodes.KEY_ENTER;
				boolean shiftKeyDown = event.isShiftKeyDown();
				if (isEnter && !shiftKeyDown) {
					presenter.createPost(commentTextArea.getValue());
				}
			}
		});
		
		textArea.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				boolean isEnter = event.getNativeKeyCode() == KeyCodes.KEY_ENTER;
				boolean shiftKeyDown = event.isShiftKeyDown();
				if (isEnter && !shiftKeyDown) {
					event.preventDefault();
				}
			}
		});
	}

	private void setSubjectNames(TrainingSessionProxy value) {
		String subjects = ListToStringUtil.subjectListToString(value.getSubjects());
		subjectsLabel.setText(subjects);
	}
	
	private void setTeamNames(TrainingSessionProxy value) {
		String teams = ListToStringUtil.teamListToString(value.getTeams());
		teamsLabel.setText(teams);
	}

	private void setRatingStatistic(TrainingSessionProxy value) {
		if (value.getRatingCount() == 0) {
			awesomeCountLabel.setText("[0]");
			superCountLabel.setText("[0]");
			goodCountLabel.setText("[0]");
			badCountLabel.setText("[0]");
			awesomeRatingBar.setWidth("0%");
			superRatingBar.setWidth("0%");
			goodRatingBar.setWidth("0%");
			badRatingBar.setWidth("0%");
			averageRating.setValue(Rating.NONE);
			ratingCountLabel.setText("");
			return;
		}
		
		int ratingCountVal = value.getRatingCount();
		int awesomeCount = value.getAwesomeRatingCount();
		int superCount = value.getSuperRatingCount();
		int goodCount = value.getGoodRatingCount();
		int badCount = value.getBadRatingCount();
		
		double awesomeWidth = (1.0 * awesomeCount / ratingCountVal) * 100;
		double superWidth = (1.0 * superCount / ratingCountVal) * 100;
		double goodWidth = (1.0 * goodCount / ratingCountVal) * 100;
		double badWidth = (1.0 * badCount / ratingCountVal) * 100;

		awesomeRatingBar.setWidth(awesomeWidth + "%");
		superRatingBar.setWidth(superWidth + "%");
		goodRatingBar.setWidth(goodWidth + "%");
		badRatingBar.setWidth(badWidth + "%");
		
		awesomeCountLabel.setText("[" + awesomeCount + "]");
		superCountLabel.setText("[" + superCount + "]");
		goodCountLabel.setText("[" + goodCount + "]");
		badCountLabel.setText("[" + badCount + "]");
		
		averageRating.setValue(value.getAverageRating());
		ratingCountLabel.setText("(" + ratingCountVal + " Bewertungen)");
	}
	
}
