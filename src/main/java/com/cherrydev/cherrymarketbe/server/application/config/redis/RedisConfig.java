package com.cherrydev.cherrymarketbe.server.application.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * REDIS 설정
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private static final String REDISSON_HOST_PREFIX = "redis://";
    private static final String ADDRESS_SEPARATOR = ":";

    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        String host = redisProperties.getHost();
        Integer port = redisProperties.getPort();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = REDISSON_HOST_PREFIX + redisProperties.getHost() + ADDRESS_SEPARATOR + redisProperties.getPort();
        config.useSingleServer()
                .setDatabase(1)
                .setAddress(address);

        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
