package de.fubatra.archiv.server.service.impl;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.fubatra.archiv.shared.service.UploadService;

public class UploadServiceImpl extends RemoteServiceServlet implements UploadService {

	private static final long serialVersionUID = -8488169834072388460L;

	@Override
	public String getBlobstoreUploadUrl() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		return blobstoreService.createUploadUrl("/upload");
	}
	
}
