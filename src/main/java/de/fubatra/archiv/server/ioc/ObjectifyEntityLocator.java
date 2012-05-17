package de.fubatra.archiv.server.ioc;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import de.fubatra.archiv.server.domain.ObjectifyEntity;

public class ObjectifyEntityLocator extends Locator<ObjectifyEntity, String> {

	@Override
	public ObjectifyEntity create(Class<? extends ObjectifyEntity> clazz) {
		return ObjectifyService.factory().construct(clazz);
	}

	@Override
	public ObjectifyEntity find(Class<? extends ObjectifyEntity> clazz, String keyString) {
		Key<Object> key = Key.create(keyString);
		return (ObjectifyEntity) ObjectifyService.begin().load().key(key).getValue();
	}

	@Override
	public Class<ObjectifyEntity> getDomainType() {
		// never called
		return null;
	}

	@Override
	public String getId(ObjectifyEntity domainObject) {
		if (domainObject.getId() == null) {
			return null;
		}
		Key<Object> key = ObjectifyService.factory().getKey(domainObject);
		return key == null ? null : key.getString();
	}

	@Override
	public Class<String> getIdType() {
		return String.class;
	}

	@Override
	public Object getVersion(ObjectifyEntity domainObject) {
		return domainObject.getVersion();
	}

}
