package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36";

    private static final String HTTP_ADDRESS = "http://47.91.156.35:8000";

    private static final String HTTP_LOGIN_URL = HTTP_ADDRESS + "/auth/login";

    private static final String HTTP_DATA_URL = HTTP_ADDRESS + "/auth";

    private static final String CONTENT_TYPE = "application/json";

    private static final String COOKIE = "Cookie";

    private static final String SET_COOKIE = "Set-Cookie";

    private static final String USER_NAME = "username";

    private static final String PASS_WORD = "password";

    public static String loginAndGetResponse(String username, String password) {
        // 获取JSESSIONID
        String JSESSIONID = getJSESSIONID(
                HttpRequest.post(HTTP_LOGIN_URL).userAgent(USER_AGENT).contentType(CONTENT_TYPE)
                        .send(buildBody(username, password)).headers());
        // 获取body
        HttpRequest bodyResponse = HttpRequest.get(HTTP_DATA_URL).userAgent(USER_AGENT).header(COOKIE, JSESSIONID);
        return bodyResponse.body();
    }

    public static String buildBody(String username, String password) {
        Map<String, String> credentialMap = new HashMap<>();
        credentialMap.put(USER_NAME, username);
        credentialMap.put(PASS_WORD, password);
        return JSON.toJSONString(credentialMap);
    }

    public static String getJSESSIONID(Map<String, List<String>> responseHeaders) {
        return responseHeaders.get(SET_COOKIE).get(0).split(";")[0];
    }

}

