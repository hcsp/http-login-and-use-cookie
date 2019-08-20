package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        final String url = "http://47.91.156.35:8000/auth/login";
        final String content_type_value = "application/json";
        final String useragent_value = "Mozilla/5.0 (Windows NT 10.0; Win64;  x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpClientContext context = HttpClientContext.create();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", content_type_value);
        httpPost.addHeader("User-Agent", useragent_value);

        //利用LinkedHashMap 装body的内容
        Map<String, String> map = new LinkedHashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);

        httpPost.setEntity(new StringEntity(json));
        //设置body需要转换成实体类
        HttpResponse response = httpClient.execute(httpPost, context);
        //获取返回的response中的Ser-Cookie值
        String cookie = response.getFirstHeader("Set-Cookie").getValue();

        return CookiesGetResponse(cookie);
    }

    //再次向服务器发送get命令
    public static String CookiesGetResponse(String cookieStore) throws IOException {
        CloseableHttpClient gethttpClient = HttpClients.createDefault();
        String newUrl = "http://47.91.156.35:8000/auth";
        HttpGet httpGet = new HttpGet(newUrl);
        //设置Header
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64;  x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        httpGet.addHeader("Cookie", cookieStore);

        HttpResponse httpResponse = gethttpClient.execute(httpGet);
        //将返回的response变成实体类型
        HttpEntity httpEntity = httpResponse.getEntity();
        //通过流来读取实体中的内容
        InputStream is = httpEntity.getContent();
        //通过IO口读取的内容转换成String，并用UTF-8格式
        String result = IOUtils.toString(is, "UTF-8");
        return result;
    }
}
