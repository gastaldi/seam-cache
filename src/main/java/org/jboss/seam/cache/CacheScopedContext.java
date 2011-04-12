package org.jboss.seam.cache;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

public class CacheScopedContext implements Context {

	@Override
	public Class<? extends Annotation> getScope() {
		return CacheScoped.class;
	}

	@Override
	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
		return contextual.create(creationalContext);
	}

	@Override
	public <T> T get(Contextual<T> contextual) {
		return null;
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
