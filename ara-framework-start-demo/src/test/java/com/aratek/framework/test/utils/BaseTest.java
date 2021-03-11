package com.aratek.framework.test.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author 姜寄羽
 * 测试基础类
 * @Date 2018/5/31 9:34
 */
public abstract class BaseTest {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected String token;
    protected MockMvc mvc;

    public void setUp(WebApplicationContext context) throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        final MockMvc finalMvc = mvc;
        mvc.perform(MockMvcRequestBuilders.options("/login"))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult r1) throws Exception {
                        String s1 = r1.getResponse().getContentAsString();
                        log.info("login options response {}",s1);
                        finalMvc.perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"fName\":\"zhangsan\",\"fPassWord\":\"123456\"}")
                        ).andDo(new ResultHandler() {
                            @Override
                            public void handle(MvcResult mvcResult) throws Exception {
                                String s1 = mvcResult.getResponse().getContentAsString();
                                log.info("login response {}",s1);
                                JSONObject o = JSONObject.parseObject(s1);
                                token = o.getString("token");
                            }
                        });
                    }
                });
    }
}
