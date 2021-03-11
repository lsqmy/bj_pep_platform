package com.aratek.framework.core.controller;

import com.aratek.framework.start.demo.DemoApplication;
import com.aratek.framework.test.utils.BaseTest;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

/**
 * @Author 姜寄羽
 * 代码生成器测试
 * @Date 2018/5/31 9:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@AutoConfigureMockMvc
@WebAppConfiguration
public class GeneratorControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        setUp(context);
    }

    @Test
    public void testGen() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/gen/code/t_bd_appinfo")
                .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        byte[] data = result.getResponse().getContentAsByteArray();
        FileUtils.writeByteArrayToFile(new File("C:\\Users\\jiang\\Desktop\\code4test.zip"),data);
        log.info("complete");
    }
}
