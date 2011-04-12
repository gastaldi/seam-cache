package org.jboss.seam.cache;


/**
 * A simple class on {@link CachedScope}
 * 
 * @author george
 * 
 */
@CacheScoped
public class Simple {

	private long nanos;
	public Simple() {
		this.nanos = System.nanoTime();
	}
	
	public long getNanos() {
		return nanos;
	}
}