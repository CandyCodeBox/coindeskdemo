package com.coindesk.demo;

//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallDesk {

    Timestamp isoTime;
    public static void main(String[] args) {

    }
    public JsonNode data(String type) throws Exception{
        String ndata= this.getData();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(ndata);
            String isoTime = rootNode.path("time").path("updatedISO").asText();
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoTime);
            this.isoTime = Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
            JsonNode bpiNode = rootNode.path("bpi");
            JsonNode curNode = bpiNode.path(type.toUpperCase());
            return curNode;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    public String getData() throws Exception {
        URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        }
    }

}
