package com.cherrydev.cherrymarketbe.server.application.common.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j(topic = "cherryLogger")
public final class CherryLogger {

    public static void logServerStart() {
        log.info("============================================================================");
        log.info("*********************** SERVER SUCCESSFULLY STARTED ***********************");
        log.info("============================================================================");
    }

    public static void logWarmUpStart() {
        log.info("*********************** WARM UP START ***********************");
    }

    public static void logWarmUpEnd() {
        log.info("*********************** WARM UP END ***********************");
    }

}
