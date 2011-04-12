package org.jboss.seam.cache;

import static org.junit.Assert.*;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CachedScopeTest {

	@Inject
	private Instance<Simple> instance;

	@Deployment
	public static JavaArchive createArchive() {
		return ShrinkWrap.create(JavaArchive.class)
				.addPackage(Package.getPackage("org.jboss.seam.cache"))
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
	}

	@Test
	public void testCache() throws Exception {
		Simple firstInstance = instance.get();
		System.out.println(firstInstance);
		Simple secondInstance = instance.get();
		System.out.println(secondInstance);
		assertNotNull(firstInstance);
		assertNotNull(secondInstance);
		assertNotSame(firstInstance, secondInstance);
	}
}
