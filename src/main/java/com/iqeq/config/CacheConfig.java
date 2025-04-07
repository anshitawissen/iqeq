package com.iqeq.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;

import javax.cache.Caching;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
 
public class CacheConfig {

	private static final Logger logger = LogManager.getLogger(CacheConfig.class);
	private static final String MY_CACHE = "MY_CACHE";

	@Bean
	public CacheManager jCacheCacheManager() {

		Map<String, CacheConfiguration<?, ?>> cacheMap = new HashMap<>();
		ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.heap(3).offheap(1, MemoryUnit.MB); // min value
																											// is 1MB
		ExpiryPolicy<Object, Object> expiryPolicy = createExpiryPolicy(Duration.ofMinutes(10), Duration.ofMinutes(5));
		CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Object.class, Object.class, resourcePoolsBuilder).withExpiry(expiryPolicy)
				.build();

		cacheMap.put(MY_CACHE, cacheConfiguration);
		EhcacheCachingProvider ehcacheCachingProvider = (EhcacheCachingProvider) Caching
				.getCachingProvider(EhcacheCachingProvider.class.getName());
		DefaultConfiguration defaultConfiguration = new DefaultConfiguration(cacheMap,
				ehcacheCachingProvider.getDefaultClassLoader());
		javax.cache.CacheManager cacheManager = ehcacheCachingProvider
				.getCacheManager(ehcacheCachingProvider.getDefaultURI(), defaultConfiguration);
		return new JCacheCacheManager(cacheManager);
	}

	private static ExpiryPolicy<Object, Object> createExpiryPolicy(Duration timeToLive, Duration timeToIdle) { 
		return ExpiryPolicyBuilder.expiry().create(timeToLive).access(timeToIdle).build(); 
	}

}
