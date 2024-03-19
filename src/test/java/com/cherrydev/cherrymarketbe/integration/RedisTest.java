package com.cherrydev.cherrymarketbe.integration;

import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@DataRedisTest
@Testcontainers
@DisabledInAotMode
class RedisTest {

    @Container
    private static final RedisContainer redis = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void setRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("데이터를 저장하고, 가져오는데 성공해야 한다.")
    void shouldSetAndGetData() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When & Then
        assertThat(redisService.getData(key, String.class)).isEqualTo(value);
    }

    @Test
    @DisplayName("저장된 데이터는 지정된 시간 후 만료되어야 한다.")
    void shouldExpireDataAfterSpecifiedDuration() {
        // Given
        String key = "testKey";
        String value = "testValue";
        long duration = 1L;

        // When
        redisService.setDataExpire(key, value, Duration.ofSeconds(duration));

        // Then
        assertThat(redisService.getData(key, String.class)).isEqualTo(value);
        await().atMost(duration + 1, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(redisService.getData(key, String.class)).isNull());
        assertThat(redisService.getData(key, String.class)).isNull();
    }

    @Test
    @DisplayName("키가 존재하는지 확인하는데 성공해야 한다.")
    void shouldCheckIfKeyExists() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When & Then
        assertThat(redisService.hasKey(key)).isTrue();
    }

    @Test
    @DisplayName("데이터를 삭제하는데 성공해야 한다.")
    void shouldDeleteData() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When
        redisService.deleteData(key);

        // Then
        assertThat(redisService.getData(key, String.class)).isNull();
    }

    /**
     * 테스트 종료 후 Redis 데이터 초기화
     */
    @AfterEach
    void tearDown() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .serverCommands()
                .flushDb();
    }

}
