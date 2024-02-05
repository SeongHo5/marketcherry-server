package com.cherrydev.cherrymarketbe.server.application.common.service;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthTokenResponse;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.*;


@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에서 데이터를 가져옵니다.<br>
     *
     * @param key  가져올 데이터의 Key
     * @param type 가져올 데이터의 타입
     * @return 가져온 데이터
     * @throws RedisException Redis에 저장된 데이터의 타입과 요청한 타입이 일치하지 않을 경우 발생
     */
    public <T> T getData(String key, Class<T> type) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object result = valueOperations.get(key);
        return castToType(result, type);
    }

    /**
     * Redis에 데이터 저장
     * <p>
     * <em>만료 시간을 설정하지 않으면 영구적으로 저장되니 신중하게 사용해야 한다.</em>
     *
     * @param key   저장할 데이터의 Key
     * @param value 저장할 데이터
     */
    public <T> void setData(String key, T value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * Redis에 데이터 저장 및 만료 시간 설정
     *
     * @param key      저장할 데이터의 Key
     * @param value    저장할 데이터
     * @param duration 만료 시간
     */
    public <T> void setDataExpire(String key, T value, Duration duration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    /**
     * Redis에 해당 Key를 가진 데이터가 존재하는지 확인
     *
     * @param key 확인할 데이터의 Key
     * @return 데이터 존재 여부
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Redis에 저장된 데이터 삭제
     *
     * @param key 삭제할 데이터의 Key
     */
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Redis에 저장된 특정 Key의 Value를 1만큼 증가시킨다.
     */
    public void incrementValueByKey(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(key);
    }

    public void extendExpireTime(String key, int duration) {
        redisTemplate.expire(key, duration, TimeUnit.SECONDS);
    }

    // ==================== REDIS - OAUTH ==================== //

    public void saveNaverTokenToRedis(
            final OAuthTokenResponse oAuthTokenResponse,
            final String email
    ) {
        String accessToken = oAuthTokenResponse.getAccessToken();
        Long expiresIn = oAuthTokenResponse.getExpiresIn();

        String refreshToken = oAuthTokenResponse.getRefreshToken();

        setDataExpire(OAUTH_NAVER_PREFIX + email, accessToken, Duration.ofMillis(expiresIn));
        setDataExpire(OAUTH_NAVER_REFRESH_PREFIX + email, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public void deleteNaverTokenFromRedis(final String email) {
        deleteData(OAUTH_NAVER_PREFIX + email);
        deleteData(OAUTH_NAVER_REFRESH_PREFIX + email);
    }

    // ========== PRIVATE METHODS ========== //
    private <T> T castToType(Object result, Class<T> type) {
        checkBeforeCastToType(result, type);
        return type.cast(result);
    }

    private void checkBeforeCastToType(Object result, Class<?> type) {
        if (result == null) {
            throw new RedisException("Data Not Found - Key : " + type.getName());
        }
        if (!type.isInstance(result)) {
            String message = "Expected :" + type.getName() + ", Actual :" + result.getClass().getName();
            throw new RedisException("Data Type Mismatch - " + message);
        }
    }
}
