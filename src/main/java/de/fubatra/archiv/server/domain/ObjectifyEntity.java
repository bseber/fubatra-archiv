package de.fubatra.archiv.server.domain;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

public abstract class ObjectifyEntity {
	
	@Id
	private Long id;

	@Index
	private Integer version = 0;
	
	public Long getId() {
		return id;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * Called prior to being saved in the datastore. Default implementation does
	 * nothing.
	 * 
	 * @param ofy
	 */
	protected void doBeforeSave(Objectify ofy) {
	}
	
	@OnSave
	void onSave(Objectify ofy) {
		version++;
		doBeforeSave(ofy);
	}
	
}
