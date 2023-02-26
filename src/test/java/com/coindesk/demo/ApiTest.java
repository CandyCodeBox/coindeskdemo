package com.coindesk.demo;

import com.coindesk.demo.model.Coin;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoindeskApplication.class)
@WebAppConfiguration
public class ApiTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    MockMvc mvc;

    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test//        測試 單一查詢
    public void queryData() throws Exception{
        String uri = "/coin/USD";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("錯誤",200,status);
    }


    @Test//        測試 新增
    public void addData() throws Exception {
        Coin coin = new Coin();
        coin.setCode("USD");
        coin.setName("測試用");
        coin.setExchangeRate(168.0);
        Date date = new Date();
        coin.setLastUpdated(new Timestamp(date.getTime()));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(coin);
        String uri = "/add/coin";
        mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
                )
                .andReturn();
    }


    @Test//        測試 單一更新
    public void updateData() throws Exception{
        Coin coin = new Coin();
        coin.setCode("USD");
        coin.setName("測試用");
        coin.setExchangeRate(168.0);
        Date date = new Date();
        coin.setLastUpdated(new Timestamp(date.getTime()));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(coin);
        String uri = "/update/coin";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("錯誤",200,status);
    }

    @Test//        測試 刪除
    public void deleteData() throws Exception{
        String uri = "/coin/delete?id=3";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("錯誤",200,status);
    }

    @Test //原本API
    public void oriApi() throws Exception{
        String uri = "/origins";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("錯誤",200,status);

    }
    @Test //新api
    public void allApi() throws Exception{
        String uri = "/coins";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("錯誤",200,status);
    }
}
