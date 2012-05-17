package de.fubatra.archiv.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import de.fubatra.archiv.shared.domain.BlobImageProxy;

public class SavePictureEvent extends GwtEvent<SavePictureEvent.Handler> {
	
	public static final Type<SavePictureEvent.Handler> TYPE = new Type<SavePictureEvent.Handler>();

	public static void fire(HasSavePictureEventHandlers source, SavePictureEvent event) {
		source.fireEvent(event);
	}
	
	public interface Handler extends EventHandler {
		
		void onSavePicture(SavePictureEvent event);
	}
	
	private final BlobImageProxy blobImageProxy;

	public SavePictureEvent() {
		this(null);
	}
	
	public SavePictureEvent(BlobImageProxy blobImageProxy) {
		this.blobImageProxy = blobImageProxy;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	public BlobImageProxy getBlobImageProxy() {
		return blobImageProxy;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSavePicture(this);
	}

}
