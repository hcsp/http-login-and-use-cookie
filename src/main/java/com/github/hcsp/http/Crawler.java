package com.github.hcsp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("user-agent", "Mozilla/5.0");
        httpPost.addHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("username", username);
        ObjectMapper mapper = new ObjectMapper();
        httpPost.setEntity(new StringEntity(mapper.writeValueAsString(map)));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator("Set-Cookie"));
        String cookieData = null;
        while (it.hasNext()) {
            HeaderElement elem = it.nextElement();
            cookieData = elem.getValue();
        }
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("user-agent", "Mozilla/5.0");
        httpGet.addHeader("Cookie", "JSESSIONID=" + cookieData);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            return IOUtils.toString(response1.getEntity().getContent());
        } finally {
            response.close();
        }
    }
}
