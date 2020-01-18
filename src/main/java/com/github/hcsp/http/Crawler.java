package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String USER_AGENT = "User-Agent";
    public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    public static String CONTENT_TYPE = "Content-Type";
    public static String APPLICATION = "application/json;charset=UTF-8";
    public static Connection.Response response;
    private static String loginUrl = "http://47.91.156.35:8000/auth/login";
    private static String getUrl = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String json = userToJsonString(username, password);
        postResponse(loginUrl, json);
        return getResponse(getUrl);
    }

    //用户名和密码序列化成一个JSON字符串
    public static String userToJsonString(String username, String password) {
        Map<String, String> map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }

    //指定url获得post请求的响应体
    public static void postResponse(String url, String json) throws IOException {
        Connection connect = getConnection(url);
        response = connect.ignoreContentType(true).method(Connection.Method.POST).requestBody(json).execute();
    }

    private static Connection getConnection(String url) {
        Connection connect = Jsoup.connect(url);
        connect
                .header(CONTENT_TYPE, APPLICATION)
                .header(USER_AGENT, USER_AGENT_VALUE);
        return connect;
    }

    //返回get请求的响应体
    public static String getResponse(String url) throws IOException {
        Connection connect = getConnection(url)
                .cookies(response.cookies());
        Connection.Response responseByGet = connect.ignoreContentType(true).method(Connection.Method.GET).execute();
        return responseByGet.body();
    }
}

