package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Crawler {
    static String loginAndGetResponse(String username, String password) throws Exception {
        String url = "http://47.91.156.35:8000/auth/login";
        String contentType = "application/json";
        String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", contentType);
        httpPost.addHeader("User-Agent", userAgent);

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("username", username);
        infoMap.put("password", password);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(infoMap)));
        CloseableHttpClient httpClientForPost = HttpClients.createDefault();
        try (CloseableHttpResponse responseForPost = httpClientForPost.execute(httpPost)){
            Header[] headers = responseForPost.getHeaders("Set-Cookie"); //可能有多个Set-Cookie
            List<Header> headerList = Arrays.asList(headers);
            String sessionId = getSessionId(headerList);

            CloseableHttpClient httpClientFotGet = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.addHeader("Cookie", "JSESSIONID="+sessionId);

            try (CloseableHttpResponse responseForGet = httpClientFotGet.execute(httpGet)){
                try (InputStream is = responseForGet.getEntity().getContent()){
                    return IOUtils.toString(is, StandardCharsets.UTF_8);
                }
            }

        }
    }

    private static String getSessionId(List<Header> headers){
        if (headers==null){
            return null;
        }
        for (Header header: headers){
            String value = header.getValue();
            List<String> keyValues = Arrays.asList(value.split(";"));
            for (String keyValue : keyValues){
                List<String> kv = Arrays.asList(keyValue.split("="));
                String key = kv.get(0);
                if ("JSESSIONID".equals(key) && kv.size()>1){
                    return kv.get(1);
                }
            }
        }
        return null;
    }
}
