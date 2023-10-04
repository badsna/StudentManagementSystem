package com.example.studentmanagementsystem.config;

import com.example.studentmanagementsystem.dto.StudentResponseDto;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.ArrayList;

@org.springframework.context.annotation.Configuration
@EnableCaching
public class EchacheConfig {

    @Bean
    public CacheManager cacheManager1() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        // Configure a cache for StudentResponseDto
        configureCache(cacheManager, "students", Integer.class, StudentResponseDto.class);

        // Configure a cache for ArrayList (List of StudentResponseDto)
        configureCache(cacheManager, "studentLists", String.class, ArrayList.class);

        return cacheManager;
    }

    private <K, V> void configureCache(CacheManager cacheManager, String cacheName, Class<K> keyType, Class<V> valueType) {
        CacheConfiguration<K, V> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(keyType, valueType,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        Configuration<K, V> configuration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
        cacheManager.createCache(cacheName, configuration);
    }
}
