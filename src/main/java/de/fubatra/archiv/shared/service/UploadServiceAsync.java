package de.fubatra.archiv.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UploadServiceAsync {

	/**
	 * @param callback
	 *            contains a url to upload a file to the Blobstore
	 */
	void getBlobstoreUploadUrl(AsyncCallback<String> callback);

}
