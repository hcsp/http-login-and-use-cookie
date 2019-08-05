package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {

    private static final String AUTH_URL = "http://47.91.156.35:8000/auth";
    private static final String LOGIN_URL = AUTH_URL + "/login";
    private static final String HTTP_CONTENT_TYPE = "application/json";
    private static final String HTTP_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";

    public static String loginAndGetResponse(String username, String password) {

        String cookie = firstRequest(username, password);

        String result = "";

        if (isNotEmpty(cookie)) {

            result = secondRequest(cookie);

        }

        return result;
    }

    /**
     * 获取第一次POST请求返回的cookie
     *
     * @date        2019/8/5 10:44
     * @param       username        用户名
     * @param       password        密码
     * @return      java.lang.String
     */
    private static String firstRequest(String username, String password) {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpClientContext context = HttpClientContext.create();

        HttpPost post = getHttpPostWithHeaders();

        StringEntity userLoginEntity = getUserLoginEntity(username, password);

        post.setEntity(userLoginEntity);

        HttpResponse response = getHttpResponse(client, post, context);

        String cookie = "";

        if (null != response) {
            cookie = getResponseCookie(context);
        }

        return cookie;
    }

    /**
     * 获取第二次GET请求返回的Body
     *
     * @date        2019/8/5 10:46
     * @param       cookie      第一次POST返回的cookie
     * @return      java.lang.String
     */
    private static String secondRequest(String cookie) {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet get = new HttpGet(AUTH_URL);

        get.setHeader("Cookie", "JSESSIONID=" + cookie);

        String result = "";

        try {
            HttpResponse response = client.execute(get);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取响应报文中的Cookie串
     *
     * @date        2019/8/5 10:06
     * @param       context         HttpClientContext对象
     * @return      java.lang.String
     */
    private static String getResponseCookie(HttpClientContext context) {

        List<Cookie> cookies = context.getCookieStore().getCookies();

        return cookies.get(0).getValue();
    }

    /**
     * 获取HttpPost对象
     *
     * @date        2019/8/5 9:48
     * @return      org.apache.http.client.methods.HttpPost
     */
    private static HttpPost getHttpPostWithHeaders() {

        HttpPost post = new HttpPost(LOGIN_URL);

        post.addHeader(HttpHeaders.CONTENT_TYPE, HTTP_CONTENT_TYPE);
        post.addHeader(HttpHeaders.USER_AGENT, HTTP_USER_AGENT);

        return post;
    }

    /**
     * 获取HttpResponse对象
     *
     * @date        2019/8/5 9:54
     * @param       client      HttpClient对象
     * @param       post        HttpPost对象
     * @param       context     HttpClientContext对象
     * @return      org.apache.http.HttpResponse
     */
    private static HttpResponse getHttpResponse(HttpClient client, HttpPost post, HttpClientContext context) {

        HttpResponse response = null;

        try {
            response = client.execute(post, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 获取请求实体
     *
     * @date        2019/8/5 9:48
     * @param       username        用户名
     * @param       password        密码
     * @return      org.apache.http.entity.StringEntity
     */
    private static StringEntity getUserLoginEntity(String username, String password) {

        String userLoginJson = getUserLoginJson(username, password);
        StringEntity userLoginEntity = null;

        try {
            userLoginEntity = new StringEntity(userLoginJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return userLoginEntity;
    }

    /**
     * 获取用户登录信息JSON字符串
     *
     * @date        2019/8/5 9:49
     * @param       username        用户名
     * @param       password        密码
     * @return      java.lang.String
     */
    private static String getUserLoginJson(String username, String password) {
        Map<String, String> map = new HashMap<>(16);

        map.put("username", username);
        map.put("password", password);

        return JSONObject.toJSONString(map);
    }

    private static boolean isNotEmpty(String str) {
        return str != null && str.length() != 0;
    }
}
