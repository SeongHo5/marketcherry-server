package com.cherrydev.cherrymarketbe.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
@SpringBootTest
@DisabledInAotMode
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @Autowired
    protected TestHelper testHelper;

}
