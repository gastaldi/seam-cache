package org.jboss.seam.cache;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheManager;

import org.jboss.weld.serialization.ContextualStoreImpl;
import org.jboss.weld.serialization.spi.ContextualStore;

public class CacheScopedContext implements Context {

	//TODO: Remove this dependency
	private ContextualStore store;

	public CacheScopedContext() {
		store = new ContextualStoreImpl();
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return CacheScoped.class;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
		Bean<T> bean = (Bean<T>) contextual;
		return getCache(creationalContext, bean);
	}

	@Override
	public <T> T get(Contextual<T> contextual) {
		Bean<T> bean = (Bean<T>) contextual;
		String key = getName(bean);
		String cacheName = getCacheName(bean);
		T obj = getFromCache(cacheName, key);
		return obj;
	}

	private <T> T getCache(CreationalContext<T> creationalContext, Bean<T> bean) {
		String key = getName(bean);
		String cacheName = getCacheName(bean);
		T obj = getFromCache(cacheName, key);
		if (obj == null) {
			synchronized (this) {
				// Double checked locking
				obj = getFromCache(cacheName, key);
				if (obj == null) {
					obj = bean.create(creationalContext);
					putInCache(cacheName, key, obj);
				}
			}
		}
		return obj;
	}

	private <T> void putInCache(String cacheName, String key, T obj) {
		Cache cache = CacheManager.getInstance().getCache(cacheName);
		if (cache == null) {
			throw new IllegalStateException("Cache " + cacheName + " does not exists");
		}
		cache.put(key, obj);
	}

	@SuppressWarnings("unchecked")
	private <T> T getFromCache(String cacheName, String key) {
		Cache cache = CacheManager.getInstance().getCache(cacheName);
		if (cache == null) {
			throw new IllegalStateException("Cache " + cacheName + " does not exists");
		}
		return (T) cache.get(key);
	}

	private String getCacheName(Bean<?> bean) {
		CacheScoped cs = bean.getBeanClass().getAnnotation(CacheScoped.class);
		return cs.cacheName();
	}

	private String getName(Bean<?> bean) {
		return store.putIfAbsent(bean);
	}
}
