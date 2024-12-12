package com.oycm;

import com.oycm.config.AcmeProperties;
import com.oycm.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SpringBootExampleTests {


    @Autowired
    MockMvc mvc;

    @Test
    void contextLoads() {

    }

    @Test
    void exampleTest(@Autowired AcmeProperties acmeProperties) throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.remoteAddress").exists())
                .andExpect(content().json(JsonUtils.objToString(acmeProperties)));
    }

}
