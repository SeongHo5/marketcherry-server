package com.cherrydev.cherrymarketbe.server.application.config.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * REDIS 설정
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    public static final String REDISSON_HOST_PREFIX = "redis://";

    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = REDISSON_HOST_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort();
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
