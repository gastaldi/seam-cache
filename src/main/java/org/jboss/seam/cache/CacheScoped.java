/**
 * 
 */
package org.jboss.seam.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;
import javax.enterprise.util.Nonbinding;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NormalScope
@Inherited
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Documented
/**
 * 
 * 
 * @author george
 *
 */
public @interface CacheScoped {
	@Nonbinding String regionName() default "default";
}
