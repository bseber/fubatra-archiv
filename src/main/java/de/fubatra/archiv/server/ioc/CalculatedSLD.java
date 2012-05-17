package de.fubatra.archiv.server.ioc;

import java.lang.reflect.Method;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

import de.fubatra.archiv.server.annotations.Calculated;

public class CalculatedSLD extends ServiceLayerDecorator {
	
	@Override
	public void setProperty(Object domainObject, String property, Class<?> expectedType, Object value) {
		Method getter = getGetter(domainObject.getClass(), property);
		boolean annotationPresent = getter.isAnnotationPresent(Calculated.class);
		if (!annotationPresent) {
			super.setProperty(domainObject, property, expectedType, value);
		}
	}
	
}
