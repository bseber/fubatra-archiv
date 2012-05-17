package de.fubatra.archiv.server.domain;

import com.google.appengine.api.blobstore.BlobKey;

public class BlobImage {
	
	private BlobKey blobKey;
	private String pictureUrl;
	
	public BlobImage() {
	}
	
	public String getBlobKey() {
		return blobKey == null ? null : blobKey.getKeyString();
	}
	
	public void setBlobKey(String blobKey) {
		if (blobKey == null) {
			this.blobKey = null;
		} else {
			this.blobKey = new BlobKey(blobKey);
		}
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
}
