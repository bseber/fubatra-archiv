package de.fubatra.archiv.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("upload")
public interface UploadService extends RemoteService {

	/**
	 * @return a url to upload a file to Blobstore
	 */
	public String getBlobstoreUploadUrl();
	
}
