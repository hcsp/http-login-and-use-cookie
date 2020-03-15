package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String param = JSON.toJSONString(map);
        HttpEntity httpEntity = new StringEntity(param);
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        Header setCookieHeader = response.getFirstHeader("Set-Cookie");
        String setdCookie = setCookieHeader.getElements()[0].getValue();

        HttpGet get = new HttpGet("http://47.91.156.35:8000/auth");
        get.addHeader("cookie", setdCookie);
        CloseableHttpResponse responseByGet = client.execute(get);
        HttpEntity responseByGetEntity = responseByGet.getEntity();
        return EntityUtils.toString(responseByGetEntity);


    }


}
