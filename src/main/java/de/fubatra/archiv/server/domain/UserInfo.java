package de.fubatra.archiv.server.domain;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UserInfo extends ObjectifyEntity {

	public static final String DB_FIELD_NAME = "name";
	public static final String DB_FIELD_USERID = "googleUserId";
	
	// prevent sending email to client by default
	public static class EmailHelper {
		
		private String email;

		public EmailHelper() {
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
	}

	@Index
	private String googleUserId;

	@Embed
	private EmailHelper email;
	
	@Index
	private String name;
	
	private String googlePlusUrl;
	
	private String facebookUrl;
	
	private boolean profileInitiated;

	@Embed
	private BlobImage picture;
	
	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String userId) {
		this.googleUserId = userId;
	}

	public String getEmail() {
		return email.getEmail();
	}

	public void setEmail(String email) {
		if (this.email == null) {
			this.email = new EmailHelper();
		}
		this.email.setEmail(email);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGooglePlusUrl() {
		return googlePlusUrl;
	}
	
	public void setGooglePlusUrl(String googlePlusUrl) {
		this.googlePlusUrl = googlePlusUrl;
	}
	
	public String getFacebookUrl() {
		return facebookUrl;
	}
	
	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}
	
	public BlobImage getPicture() {
		return picture;
	}
	
	public void setPicture(BlobImage picture) {
		this.picture = picture;
	}
	
	public boolean isProfileInitiated() {
		return profileInitiated;
	}
	
	public void setProfileInitiated(boolean profileEdited) {
		this.profileInitiated = profileEdited;
	}

	
}
