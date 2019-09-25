package com.github.hcsp.http;

import net.dongliu.requests.Cookie;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;

import java.util.HashMap;
import java.util.List;

public class Crawler {
    public static final String CHROME_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    public static String visitWithCookie(List cookies, HashMap headers) {
        String authUrl = "http://47.91.156.35:8000/auth";

        HashMap<String, Object> requestCookies = new HashMap<>();

        Cookie cookie = (Cookie) cookies.get(0);

        requestCookies.put("JSESSIONID", cookie.getValue());

        String ret = Requests.get(authUrl).headers(headers).cookies(requestCookies).send().readToText();

        return ret;
    }

    public static String loginAndGetResponse(String username, String password) {
        String loginUrl = "http://47.91.156.35:8000/auth/login";

        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);


        HashMap<String, Object> headers = new HashMap<>();

        headers.put("User-Agent", CHROME_USER_AGENT);
        headers.put("Content-Type", "application/json");

        RawResponse response = Requests.post(loginUrl).headers(headers).jsonBody(params).send();

        return visitWithCookie(response.cookies(), headers);

    }

    public static void main(String[] args) {
        String cookie = loginAndGetResponse("xdml", "xdml");
        System.out.println(cookie);
    }
}
