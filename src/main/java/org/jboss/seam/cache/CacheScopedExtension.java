package org.jboss.seam.cache;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class CacheScopedExtension implements Extension {

	public void addScope(@Observes final BeforeBeanDiscovery event) {
		event.addScope(CacheScoped.class, true, true);
	}

	public void registerContext(@Observes final AfterBeanDiscovery event) {
		event.addContext(new CacheScopedContext());
	}
}
