package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.*;

public class Crawler {
  public static String loginAndGetResponse(String username, String password) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();

    HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");

    Map<String, String> map = new HashMap<>();
    map.put("username", username);
    map.put("password", password);

    httpPost.setEntity(new StringEntity(JSON.toJSONString(map)));

    CloseableHttpResponse response2 = httpclient.execute(httpPost);
    String cookies = response2.getFirstHeader("set-cookie").getValue();
    String sessionId = cookies.split(";")[0];

    HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
    httpGet.setHeader("cookie", sessionId);

    CloseableHttpResponse response3 = httpclient.execute(httpGet);
    String res = IOUtils.toString(response3.getEntity().getContent(), "UTF-8");

    return res;
  }
}
