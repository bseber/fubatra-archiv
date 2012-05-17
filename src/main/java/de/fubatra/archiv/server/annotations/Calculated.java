package de.fubatra.archiv.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

import de.fubatra.archiv.server.ioc.CalculatedSLD;

/**
 * Indicates a method that it's return type is calculated while runtime. Use
 * this annotation to avoid {@link RequestFactory} errors about missing setter.
 * This annotation will be checked by {@link CalculatedSLD}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Calculated {

}
