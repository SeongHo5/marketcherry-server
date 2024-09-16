package com.cherrydev.cherrymarketbe;

import static com.cherrydev.cherrymarketbe.server.application.common.log.CherryLogger.logServerStart;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CherrymarketBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CherrymarketBeApplication.class, args);
    logServerStart();
  }

  /** TimeZone을 한국 시간으로 설정 */
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
