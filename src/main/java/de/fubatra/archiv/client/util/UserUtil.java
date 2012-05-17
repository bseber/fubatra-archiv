package de.fubatra.archiv.client.util;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;

import de.fubatra.archiv.shared.domain.UserInfoProxy;

public class UserUtil {

	public static void clearStorage() {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage == null) {
			return;
		}
		storage.clear();
	}
	
	/**
	 * @return the email which was set with {@link #setUserInfo(UserInfoProxy)}
	 */
	public static String getUserEmail() {
		Storage storage = Storage.getLocalStorageIfSupported();
		return storage == null ? Cookies.getCookie("email") : storage.getItem("email");
	}
	
	/**
	 * @return -1 if no user is logged in, otherwise it's id
	 */
	public static long getUserId() {
		if (!isUserLoggedIn()) {
			return -1;
		}
		Storage storage = Storage.getLocalStorageIfSupported();
		String id;
		if (storage == null) {
			id = Cookies.getCookie("id");
		} else {
			id = storage.getItem("id");
		}
		return Long.parseLong(id);
	}

	/**
	 * @return true if an email is available in the local storage or in a
	 *         cookie, otherwise false. <b>It does not check if this is the
	 *         valid email of the logged in google user</b>
	 */
	public static boolean isUserLoggedIn() {
		String email = getUserEmail();
		return email != null && !email.isEmpty();
	}
	
	/**
	 * Saves the given email address in the local storage or in a cookie
	 * 
	 * @param email
	 */
	public static void setUserInfo(UserInfoProxy userInfo) {
		Storage storage = Storage.getLocalStorageIfSupported();
		if (storage == null) {
			Cookies.setCookie("email", userInfo.getEmail());
			Cookies.setCookie("id", userInfo.getId() + "");
		} else {
			storage.setItem("email", userInfo.getEmail());
			storage.setItem("id", userInfo.getId() + "");
		}
	}

}
