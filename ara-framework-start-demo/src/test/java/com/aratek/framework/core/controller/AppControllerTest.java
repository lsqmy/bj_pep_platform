package com.aratek.framework.core.controller;


import com.aratek.framework.start.demo.DemoApplication;
import com.aratek.framework.test.utils.BaseTest;
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

/**
 * @Author 姜寄羽
 * APP控制接口测试
 * @Date 2018/5/28 11:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@AutoConfigureMockMvc
@WebAppConfiguration
public class AppControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        setUp(context);
    }

    @Test
    public void testSelectAppInfoList() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/app/info/selectAppInfoList")
                .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String s1 = result.getResponse().getContentAsString();
        log.info(s1);
    }
}