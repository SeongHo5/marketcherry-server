package com.cherrydev.cherrymarketbe.server.application.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 키 직렬화 설정
        RedisSerializationContext.SerializationPair<String> keySerialization =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());

        // 값 직렬화 설정
        RedisSerializationContext.SerializationPair<Object> valueSerialization =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(keySerialization)
                .serializeValuesWith(valueSerialization)
                .disableCachingNullValues();

        RedisCacheConfiguration accountCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(keySerialization)
                .serializeValuesWith(valueSerialization)
                .disableCachingNullValues();

        return RedisCacheManager.builder()
                .transactionAware()
                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .withCacheConfiguration("accountCache", accountCacheConfig)
                .cacheDefaults(defaultCacheConfig)
                .build();
    }


}
