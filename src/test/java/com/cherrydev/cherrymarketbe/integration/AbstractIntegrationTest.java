package com.cherrydev.cherrymarketbe.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@DisabledInAotMode
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired
    protected TestHelper testHelper;

}
