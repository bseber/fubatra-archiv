package de.fubatra.archiv.client.ui.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.mvp4g.client.view.BaseCycleView;
import com.mvp4g.client.view.ReverseViewInterface;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.widgets.ImageWidget;
import de.fubatra.archiv.client.ui.widgets.RichTextAreaEditor;
import de.fubatra.archiv.client.ui.widgets.TeamSelector;
import de.fubatra.archiv.client.ui.widgets.TrainingSessionSubjectsEditor;
import de.fubatra.archiv.client.ui.widgets.TrainingSessionVisiblityEditor;
import de.fubatra.archiv.client.ui.widgets.text.RichTextToolbar;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.service.UploadService;
import de.fubatra.archiv.shared.service.UploadServiceAsync;

public class TrainingSessionEditView extends BaseCycleView implements Editor<TrainingSessionProxy>, HasEditorErrors<TrainingSessionProxy>, ReverseViewInterface<TrainingSessionEditView.IPresenter> {

	private static final String ACCEPTABLE_IMAGES = "image/gif,image/jpg,image/jpeg,image/png";
	private static final TrainingSessionEditViewUiBinder uiBinder = GWT.create(TrainingSessionEditViewUiBinder.class);

	public interface IPresenter {
		
		void onSaveClicked();
	}
	
	interface Driver extends RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionEditView> {
	}

	interface TrainingSessionEditViewUiBinder extends UiBinder<Widget, TrainingSessionEditView> {
	}

	@UiField
	FormPanel formPanel;

	@UiField
	FileUpload fileUpload;

	@UiField
	VerticalPanel descriptionContainer;

	@UiField
	Button btnSave;

	@UiField
	TextBox draftBlobKey;

	@UiField
	ImageWidget draftUrl;

	@UiField
	TrainingSessionVisiblityEditor visibility;
	
	@UiField
	TextBox topic;
	
	@UiField
	TeamSelector teams;
	
	@UiField
	HTMLPanel saveButtonContainer;
	
	@UiField
	TrainingSessionSubjectsEditor subjects;
	
	private InlineLabel lblSavedInfo;
	private Image loadingCircle;
	private RichTextAreaEditor description;

	private Driver driver;
	private UploadServiceAsync uploadService = GWT.create(UploadService.class);
	private IPresenter presenter;

	private final AppResources resources;
	
	@Inject
	public TrainingSessionEditView(AppResources resources) {
		this.resources = resources;
	}
	
	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));

		loadingCircle = new Image(resources.loadingCircle());
		lblSavedInfo = new InlineLabel();
		saveButtonContainer.add(lblSavedInfo);
		
		description = new RichTextAreaEditor();
		RichTextToolbar toolbar = new RichTextToolbar(description);
		descriptionContainer.add(toolbar);
		descriptionContainer.add(description);

		fileUpload.setName("image");
		fileUpload.getElement().setAttribute("accept", ACCEPTABLE_IMAGES);
		fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(final ChangeEvent event) {
				final NativeEvent nativeEvent = event.getNativeEvent();
				onFileSelected(TrainingSessionEditView.this, nativeEvent);
				btnSave.setEnabled(true);
			}
		});
		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				startNewBlobstoreSession();

				String image = event.getResults();
				if (image != null) {
					int i1 = image.indexOf("src=\"");
					// 5 because of "src=\"".length
					// img.length - 2 since last chars are "\">"
					String src = image.substring(i1 + 5, image.length() - 2);
					int keyIdx = src.indexOf("blob-key=");
					String blobKey = src.substring(keyIdx + 9);
					
					draftUrl.setValue(src);
					draftBlobKey.setText(blobKey);
				} else {
					draftUrl.setValue("");
					draftBlobKey.setText("");
				}
			}

		});
	}

	public RequestFactoryEditorDriver<TrainingSessionProxy, TrainingSessionEditView> editorDriver() {
		if (driver == null) {
			driver = GWT.create(Driver.class);
		}
		driver.initialize(this);
		return driver;
	}

	@UiHandler("btnSave")
	void onSaveClicked(ClickEvent event) {
		presenter.onSaveClicked();
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		startNewBlobstoreSession();
	}
	
	public void setLoadingCircleVisible(boolean visible) {
		if (visible) {
			saveButtonContainer.add(loadingCircle);
		} else {
			saveButtonContainer.remove(loadingCircle);
		}
	}
	
	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IPresenter getPresenter() {
		return presenter;
	}
	
	public void setSavedInfoText(String text) {
		lblSavedInfo.setText(text);
	}
	
	private native void onFileSelected(Object view, Object evt) /*-{
		var f = evt.target.files[0];
		var reader = new FileReader();
		reader.onload = (function(file) {
			return function(e) {
				if (file.size < 1024 * 1024) {
					view.@de.fubatra.archiv.client.ui.view.TrainingSessionEditView::submit()();
				} else {
					$wnd.alert("Bitte ein Bild auswählen, welches kleiner 1MB ist.");
				}
			};
		})(f);
		reader.readAsDataURL(f);
	}-*/;

	private void startNewBlobstoreSession() {
		uploadService.getBlobstoreUploadUrl(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				formPanel.setAction(result);
				formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
				formPanel.setMethod(FormPanel.METHOD_POST);
				fileUpload.setEnabled(true);
			}

			@Override
			public void onFailure(Throwable caught) {
				fileUpload.setEnabled(false);
			}
		});
	}
	
	private void submit() {
		formPanel.submit();
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		for (EditorError e : errors) {
			saveButtonContainer.add(new Label(e.getMessage()));
		}
	}

}
