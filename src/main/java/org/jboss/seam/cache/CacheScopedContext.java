package org.jboss.seam.cache;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheManager;

public class CacheScopedContext implements Context {

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
			obj = bean.create(creationalContext);
			putInCache(cacheName,key,obj);
		}
		return obj;
	}

	private <T> void putInCache(String cacheName, String key, T obj) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new IllegalStateException("Cache "+cacheName + " does not exists");
		}
		cache.put(key, obj);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getFromCache(String cacheName, String key) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new IllegalStateException("Cache "+cacheName + " does not exists");
		}
		return (T) cache.get(key);
	}

	private String getCacheName(Bean<?> bean) {
		CacheScoped cs = bean.getBeanClass().getAnnotation(CacheScoped.class);
		return cs.cacheName();
	}

	private <T> String getName(Bean<T> bean) {
		String name = bean.getName();
		if (name == null || name.isEmpty()) {
			name = bean.getBeanClass().getName();
		}
		return name;
	}

}
