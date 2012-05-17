package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.shared.service.UploadService;
import de.fubatra.archiv.shared.service.UploadServiceAsync;

public class PictureUploadPopup extends PopupPanel {

	private static final String ACCEPTABLE_IMAGES = "image/gif,image/jpg,image/jpeg,image/png";
	private static PictureUploadPopupUiBinder uiBinder = GWT.create(PictureUploadPopupUiBinder.class);

	interface PictureUploadPopupUiBinder extends UiBinder<Widget, PictureUploadPopup> {
	}

	@UiField
	FormPanel formPanel;

	@UiField
	FileUpload fileUpload;

	@UiField
	TextBox tbBlobKey;
	
	@UiField
	ImageWidget picture;
	
	@UiField
	Button btnSave;
	
	private UploadServiceAsync uploadService = GWT.create(UploadService.class);
	private Command saveCommand;
	
	boolean pictureChosen = false;

	public PictureUploadPopup() {
		setWidget(uiBinder.createAndBindUi(this));
		
		setGlassEnabled(true);
		
		fileUpload.setName("image");
		fileUpload.getElement().setId("btnSelect");
		fileUpload.getElement().setAttribute("accept", ACCEPTABLE_IMAGES);
		
		fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(final ChangeEvent event) {
				final NativeEvent nativeEvent = event.getNativeEvent();
				onFileSelected(PictureUploadPopup.this, nativeEvent);
			}
		});
		
		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				startNewBlobstoreSession();
				
				String image = event.getResults();
				
				if (image == null) {
					tbBlobKey.setText("");
					picture.setValue(null);
				} else {
					int idx = image.indexOf("src=\"");
					// 5 because of "src=\"".length
					// img.length - 2 since last chars are "\">"
					String src = image.substring(idx + 5, image.length() - 2);
					int keyIdx = src.indexOf("blob-key=");
					String blobKey = src.substring(keyIdx + 9);
					picture.setValue(src);
					tbBlobKey.setText(blobKey);
					
				}
				
				pictureChosen = image != null;
			}
			
		});
	}
	
	public String getBlobKey() {
		return tbBlobKey.getValue();
	}
	
	public String getPictureUrl() {
		return picture.getValue();
	}

	public void setSaveCommand(Command saveCommand) {
		this.saveCommand = saveCommand;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		startNewBlobstoreSession();
	}

	@UiHandler({ "btnCancel", "btnClose" })
	void onCancelClicked(ClickEvent event) {
		hide();
	}

	@UiHandler("btnSave")
	void onSaveClicked(ClickEvent event) {
		if (!pictureChosen) {
			return;
		}
		saveCommand.execute();
		hide();
	}
	
	private native void onFileSelected(Object view, Object evt) /*-{
		var f = evt.target.files[0];
		var reader = new FileReader();
		reader.onload = (function(file) {
			return function(e) {
				if (file.size < 1024 * 1024) {
					view.@de.fubatra.archiv.client.ui.widgets.PictureUploadPopup::submit()();
				} else {
					$wnd.alert("Bitte ein Bild auswählen, welches kleiner 1MB ist.");
				}
			};
		})(f);
		reader.readAsDataURL(f);
	}-*/;

	private void submit() {
		formPanel.submit();
	}

	private void startNewBlobstoreSession() {
		
		System.out.println("disable");
		fileUpload.setEnabled(false);
		
		
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
			}
		});
	}

}
