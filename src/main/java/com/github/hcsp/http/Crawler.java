package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
  private static CloseableHttpClient httpclient = HttpClients.createDefault();

  public static String loginAndGetResponse(String username, String password) throws IOException {
    return login(getCookie(username, password));
  }

  private static String getCookie(String username, String password) throws IOException {
    HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

    Map<String, String> map = new HashMap<>();
    map.put("username", username);
    map.put("password", password);
    String content = JSON.toJSONString(map);
    StringEntity se = new StringEntity(content);

    httpPost.setEntity(se);

    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");

    CloseableHttpResponse response1 = httpclient.execute(httpPost);

    String jsessionId = response1.getHeaders("set-cookie")[0].getElements()[0].getValue();
    try {
      HttpEntity entity1 = response1.getEntity();
      EntityUtils.consume(entity1);
    } finally {
      response1.close();
    }
    return jsessionId;
  }

  private static String login(String jsessionId) throws IOException {
    HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
    httpGet.setHeader("cookie", "JSESSIONID" + jsessionId);

    CloseableHttpResponse response2 = httpclient.execute(httpGet);
    String result;
    try {
      HttpEntity entity2 = response2.getEntity();
      result = IOUtils.toString(entity2.getContent(), "UTF-8");
      EntityUtils.consume(entity2);
    } finally {
      response2.close();
    }
    return result;
  }



  public static void main(String[] args) throws IOException {
    loginAndGetResponse("xdml", "xdml");
  }
}
