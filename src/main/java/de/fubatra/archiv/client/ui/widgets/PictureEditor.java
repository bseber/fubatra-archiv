package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import de.fubatra.archiv.client.events.HasSavePictureEventHandlers;
import de.fubatra.archiv.client.events.SavePictureEvent;
import de.fubatra.archiv.client.events.SavePictureEvent.Handler;
import de.fubatra.archiv.shared.domain.BlobImageProxy;

public class PictureEditor extends Composite implements LeafValueEditor<BlobImageProxy>, HasSavePictureEventHandlers, HasRequestContext<BlobImageProxy> {

	private static PictureEditorUiBinder uiBinder = GWT.create(PictureEditorUiBinder.class);

	interface PictureEditorUiBinder extends UiBinder<Widget, PictureEditor> {
	}

	@UiField
	Label btnEdit;
	
	@UiField
	TextBox blobKey;
	
	@UiField
	ImageWidget image;

	private BlobImageProxy value;
	private RequestContext requestContext;

	private boolean editable;
	private boolean isPlaceholder;
	
	private final ImageResource placeholder;
	
	public PictureEditor(ImageResource placeholder) {
		this.placeholder = placeholder;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openImageUploadDialog();
			}
		}, ClickEvent.getType());
		
		addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (isPlaceholder) {
					return;
				}
				btnEdit.setVisible(true);
			}
		}, MouseOverEvent.getType());
		
		addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (isPlaceholder) {
					return;
				}
				btnEdit.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
	}

	@Override
	public HandlerRegistration addSavePictureEventHandler(Handler handler) {
		return addHandler(handler, SavePictureEvent.TYPE);
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
		// TODO hide/show "change image" info
	}
	
	@Override
	public void setRequestContext(RequestContext ctx) {
		this.requestContext = ctx;
	}
	
	@Override
	public void setValue(BlobImageProxy value) {
		this.value = value;
		
		isPlaceholder = value == null || value.getPictureUrl() == null || value.getPictureUrl().trim().isEmpty();
		btnEdit.setVisible(editable && isPlaceholder);
		
		if (isPlaceholder) {
			AbstractImagePrototype prototype = AbstractImagePrototype.create(placeholder);
			prototype.applyTo(image);
			blobKey.setValue("");
			return;
		}
		
		blobKey.setValue(value.getBlobKey());
		image.setValue(value.getPictureUrl());
	}

	@Override
	public BlobImageProxy getValue() {
		if (value == null) {
			return null;
		}
		value.setBlobKey(blobKey.getValue());
		value.setPictureUrl(image.getValue());
		return value;
	}
	
	@UiHandler("image")
	void onImageMouseOut(MouseOutEvent event) {
		btnEdit.setVisible(editable && image.getValue() == null);
	}
	
	@UiHandler("image")
	void onImageMouseOver(MouseOverEvent event) {
		btnEdit.setVisible(editable);
	}

	private void openImageUploadDialog() {
		if (!editable) {
			return;
		}
		
		final PictureUploadPopup uploadPopup = new PictureUploadPopup();
		
		uploadPopup.setSaveCommand(new Command() {
			@Override
			public void execute() {
				if (value == null) {
					// create a new proxy, that #getValue can fill in the new values
					value = requestContext.create(BlobImageProxy.class);
				}
				blobKey.setValue(uploadPopup.getBlobKey());
				image.setValue(uploadPopup.getPictureUrl());
				SavePictureEvent.fire(PictureEditor.this, new SavePictureEvent());
			}
		});
		
		uploadPopup.center();
	}

}
