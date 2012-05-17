package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.shared.domain.TrainingSessionPostProxy;
import de.fubatra.archiv.shared.domain.UserInfoProxy;

public class TrainingSessionPostView extends Composite implements Editor<TrainingSessionPostProxy>, ValueAwareEditor<TrainingSessionPostProxy>, HasMouseOutHandlers, HasMouseOverHandlers {

	private static TrainingSessionPostViewUiBinder uiBinder = GWT.create(TrainingSessionPostViewUiBinder.class);

	public interface IPresenter {
		
		void onEditPostClicked(TrainingSessionPostView postView);
		
		void onDeletePostClicked(TrainingSessionPostProxy post);
		
		void onUserNameClicked(UserInfoProxy user);

		void updatePost(long postId, String text);
	}
	
	interface TrainingSessionPostViewUiBinder extends UiBinder<Widget, TrainingSessionPostView> {
	}

	@UiField
	@Path("userInfo.name")
	Label user;
	
	@UiField(provided = true)
	DateLabel creationDate;
	
	@UiField
	HTMLPanel buttonPanel;

	@UiField
	HTMLPanel textPanel;
	
	@UiField
	Label text;

	private TrainingSessionPostProxy postProxy;
	
	private final IPresenter presenter;

	private boolean editMode;

	private TextArea textArea;

	public TrainingSessionPostView(IPresenter presenter) {
		this.presenter = presenter;
		creationDate = new DateLabel(DateTimeFormat.getFormat("dd.MM.yyyy, HH:mm"));
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}
	
	public void setButtonPanelVisible(boolean visible) {
		buttonPanel.setVisible(visible);
	}
	
	@Override
	public void setDelegate(EditorDelegate<TrainingSessionPostProxy> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(TrainingSessionPostProxy value) {
		this.postProxy = value;
	}
	
	public UserInfoProxy getPostOwnerProxy() {
		return postProxy.getUserInfo();
	}
	
	@UiHandler("btnEdit")
	void onEditClicked(ClickEvent event) {
		presenter.onEditPostClicked(this);
	}
	
	@UiHandler("btnDelete")
	void onDeleteClicked(ClickEvent event) {
		presenter.onDeletePostClicked(postProxy);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		buttonPanel.setVisible(false);
		editMode = false;
	}

	public void toggleEditMode() {
		editMode = !editMode;
		if (editMode) {
			if (textArea == null) {
				initEditTextArea();
			}
			textArea.setText(text.getText());
			textPanel.add(textArea);
			text.setVisible(false);
		} else {
			textArea.removeFromParent();
			text.setVisible(true);
		}
	}

	@UiHandler("user")
	void onUsernameClicked(ClickEvent event) {
		presenter.onUserNameClicked(postProxy.getUserInfo());
	}
	
	private void initEditTextArea() {
		textArea = new TextArea();
		textArea.setWidth("97%");
		textArea.setHeight("2em");
		textArea.getElement().getStyle().setProperty("resize", "none");
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				boolean isEnter = event.getNativeKeyCode() == KeyCodes.KEY_ENTER;
				boolean shiftKeyDown = event.isShiftKeyDown();
				if (isEnter && !shiftKeyDown) {
					presenter.updatePost(postProxy.getId(), textArea.getValue());
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

}
