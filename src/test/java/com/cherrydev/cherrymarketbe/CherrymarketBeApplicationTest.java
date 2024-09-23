package com.cherrydev.cherrymarketbe;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.aot.DisabledInAotMode;

@SpringBootTest
@AutoConfigureMockMvc
@DisabledInAotMode
class CherrymarketBeApplicationTest {

  @Autowired private Clock clock;

  @Test
  @DisplayName("모든 Context가 정상적으로 로드되어야 한다.")
  void contextLoads() {}

  @Test
  @DisplayName("서버 시간은 Asia/Seoul 시간대와 동일해야 한다.")
  void timeZoneTest() {
    // Given
    ZoneId koreaZoneId = TimeZone.getTimeZone("Asia/Seoul").toZoneId();
    ZoneId serverZoneId = clock.getZone();

    // When
    LocalDateTime koreaTime = LocalDateTime.now(koreaZoneId);
    LocalDateTime serverTime = LocalDateTime.now(serverZoneId);

    // Then
    String formattedKoreaTime = koreaTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    String formattedServerTime = serverTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    assertThat(formattedKoreaTime).isNotNull();
    assertThat(formattedServerTime).isNotNull();
    assertThat(formattedKoreaTime).isEqualTo(formattedServerTime);
  }
}
