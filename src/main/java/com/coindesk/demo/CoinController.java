package com.coindesk.demo;

import com.coindesk.demo.model.Coin;
import com.coindesk.demo.service.CoinService;
import com.coindesk.demo.CallDesk;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class CoinController {

    @Autowired
    private CoinService coinService;

    public static void main() {    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String defaultSetting() throws Exception {
        List<Coin> defaultCoins =  coinService.findAll();
        CallDesk lookup = new CallDesk();

        defaultCoins.get(0).setName("美元");
        JsonNode dollarNode =  lookup.data("USD");
        Double newRate = dollarNode.path("rate_float").asDouble();
        defaultCoins.get(0).setExchangeRate(newRate);
        defaultCoins.get(0).setLastUpdated(lookup.isoTime);
        coinService.save(defaultCoins.get(0));

        defaultCoins.get(1).setName("英鎊");
        dollarNode =  lookup.data("GBP");
        newRate = dollarNode.path("rate_float").asDouble();
        defaultCoins.get(1).setExchangeRate(newRate);
        defaultCoins.get(1).setLastUpdated(lookup.isoTime);
        coinService.save(defaultCoins.get(1));

        defaultCoins.get(2).setName("歐元");
        dollarNode =  lookup.data("EUR");
        newRate = dollarNode.path("rate_float").asDouble();
        defaultCoins.get(2).setExchangeRate(newRate);
        defaultCoins.get(2).setLastUpdated(lookup.isoTime);
        coinService.save(defaultCoins.get(2));
        return "初始化完成";
    }

    @RequestMapping(value = "/coin/{type}", method = RequestMethod.GET)
    List<Coin> getCoin(@PathVariable String type){
        type = type.toUpperCase();
        return  coinService.findByCode(type);
    }

    @RequestMapping(value = "/add/coin", method = RequestMethod.POST)
    String addCoin(@RequestBody Coin currency){
        Coin savedCoin = coinService.save(currency);
        return "Created";
    }

    @RequestMapping(value = "/update/coin", method = RequestMethod.POST)
    String updateCoin(@RequestBody String jdata) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> fdata = objectMapper.readValue(jdata, new TypeReference<Map<String, Object>>(){});
        String type = fdata.get("code").toString();
        Double newRate = Double.valueOf(fdata.get("exchangeRate").toString());
        Coin x = coinService.findByCode(type).get(0);
        String desc = "此次更新幣別為:" + x.getName() + ";更新後匯率為:" + newRate;

        // 更新並顯示
        x.setExchangeRate(newRate);
        Date date = new Date();
        x.setLastUpdated(new Timestamp(date.getTime()));
        coinService.save(x);
        return desc;
    }

    @RequestMapping(value = "/coin/delete", method = RequestMethod.POST)
    Map<String, String> deleteCoin(@RequestParam Integer id){
        Map<String, String> status = new HashMap<>();
        Optional<Coin> currency = coinService.findById(id);
        if(currency.isPresent()) {
            coinService.delete(currency.get());
            status.put("Status", "Coin deleted successfully");
        }
        else {
            status.put("Status", "Coin not exist");
        }
        return status;
    }

    // Select, Insert, Delete for List of Coins

    @RequestMapping(value = "/coins", method = RequestMethod.GET)
    List<Coin> getAllCoin(){
        return coinService.findAll();
    }
    @RequestMapping(value = "/origins", method = RequestMethod.GET)
    String oriData() throws Exception {
        CallDesk lookup = new CallDesk();
        String ori = lookup.getData();
        return  ori;
    }


}
