package org.jboss.seam.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CachedScopeTest {

	@Inject
	private Instance<Simple> instance;

	@BeforeClass
	public static void setUp() throws CacheException {
		CacheManager singletonManager = CacheManager.getInstance();
		CacheFactory cacheFactory = singletonManager.getCacheFactory();
		assertNotNull(cacheFactory);

		Map<String, String> config = new HashMap<String, String>();
		config.put("name", "default");
		config.put("maxElementsInMemory", "10");
		config.put("memoryStoreEvictionPolicy", "LFU");
		config.put("overflowToDisk", "false");
		config.put("eternal", "false");
		config.put("timeToLiveSeconds", "2");
		config.put("timeToIdleSeconds", "2");
		config.put("diskPersistent", "false");
		config.put("diskExpiryThreadIntervalSeconds", "120");

		Cache cache = cacheFactory.createCache(config);
		singletonManager.registerCache("default", cache);

	}

	@Deployment
	public static JavaArchive createArchive() throws Exception {
		setUp();
		return ShrinkWrap.create(JavaArchive.class)
				.addPackage(Package.getPackage("org.jboss.seam.cache"))
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
	}

	@Test
	public void testCache() throws Exception {
		Simple firstInstance = instance.get();
		Simple secondInstance = instance.get();
		assertNotNull(firstInstance);
		assertNotNull(secondInstance);
		assertEquals(firstInstance.getNanos(), secondInstance.getNanos());
	}
	
	@Test
	public void testInvalidCache() throws Exception {
		Simple firstInstance = instance.get();
		Thread.sleep(3000);
		Simple secondInstance = instance.get();
		assertNotNull(firstInstance);
		assertNotNull(secondInstance);
		assertNotSame(firstInstance.getNanos(), secondInstance.getNanos());
	}
}
