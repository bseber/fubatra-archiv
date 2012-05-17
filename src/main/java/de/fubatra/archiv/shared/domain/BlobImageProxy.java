package de.fubatra.archiv.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import de.fubatra.archiv.server.domain.BlobImage;

@ProxyFor(value = BlobImage.class)
public interface BlobImageProxy extends ValueProxy {
	
	String getBlobKey();
	
	void setBlobKey(String blobKey);
	
	String getPictureUrl();
	
	void setPictureUrl(String pictureUrl);

}
