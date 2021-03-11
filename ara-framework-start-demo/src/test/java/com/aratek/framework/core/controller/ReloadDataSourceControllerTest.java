package com.aratek.framework.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.aratek.framework.core.util.AraFileUtils;
import com.aratek.framework.start.demo.DemoApplication;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.hamcrest.core.Is.is;

/**
 * @Author 姜寄羽
 * 测试动态加载数据源
 * @Date 2018/5/4 15:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@AutoConfigureMockMvc
@WebAppConfiguration
public class ReloadDataSourceControllerTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @Value("${datasource.configFileName:ara-datasource.json}")
    private String configFileName;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() throws Exception {
        String userDirPath = AraFileUtils.getUserDirectoryAbsolutePath() + "ara" + File.separator;
        File configFile = new File(userDirPath + configFileName);
        FileUtils.deleteQuietly(configFile);
    }

    @Test
    public void close() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/dataSource/closeRuntimeReload"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String s1 = result.getResponse().getContentAsString();
        log.info(s1);
        int code = JSONObject.parseObject(s1).getIntValue("code");
        int respect = -2;
        if (code == 0 || code == -1) {
            respect = -1;
        }
        mvc.perform(MockMvcRequestBuilders.post("/dataSource/closeRuntimeReload"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(respect)))
                .andReturn();
        result = mvc.perform(MockMvcRequestBuilders.get("/dataSource/config"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(0)))
                .andReturn();
        String config = result.getResponse().getContentAsString();
        log.info(config);
    }

    @Test
    public void config() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/dataSource/config"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String config = result.getResponse().getContentAsString();
        log.info(config);
        JSONObject object = JSONObject.parseObject(config);
        mvc.perform(MockMvcRequestBuilders.post("/dataSource/changeConfig")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.getJSONObject("dataSource").toJSONString()))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult mvcResult) throws Exception {
                        log.info(mvcResult.getResponse().getContentAsString());
                    }
                })
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void isReady() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/dataSource/isReady"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }
}