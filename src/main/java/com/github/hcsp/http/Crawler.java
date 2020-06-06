package com.github.hcsp.http;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Crawler {
    private static final String LOGIN = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(LOGIN);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0");
        UserLogInInfo userLogInInfo = new UserLogInInfo(username, password);
        Gson gson = new Gson();
        String json = gson.toJson(userLogInInfo, UserLogInInfo.class);
        httpPost.setEntity(new StringEntity(json));
        HttpResponse loginResponse = closeableHttpClient.execute(httpPost);
        Header setCookie = loginResponse.getFirstHeader("Set-Cookie");
        String JSessionId = getJSessionId(setCookie);

        HttpGet httpGet = new HttpGet(AUTH);
        httpGet.setHeader("JSESSIONID", JSessionId);
        HttpResponse authResponse = closeableHttpClient.execute(httpGet);
        String result = IOUtils.toString(authResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        closeableHttpClient.close();
        return result;
    }

    private static String getJSessionId(Header header) {
        String[] cookieInfo = header.getValue().split(";");
        String JSessionId = "";
        for (String info :
                cookieInfo) {
            if (info.startsWith("JSESSIONID")) {
                JSessionId = info.split("=")[1];
            }
        }
        return JSessionId;
    }
}

class UserLogInInfo {
    private String username;
    private String password;

    public UserLogInInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
