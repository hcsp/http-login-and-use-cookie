package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStreamReader;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost request = new HttpPost("http://47.91.156.35:8000/auth/login");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        JSONObject params = new JSONObject();
        params.put("username", username);
        params.put("password", password);
        request.setEntity(new StringEntity(params.toJSONString()));
        return httpClient.execute(request, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                InputStreamReader reader = new InputStreamReader(httpResponse.getEntity().getContent());
                char[] data = new char[1024];
                int len = 0;
                len = reader.read(data);
                Header[] headers = httpResponse.getAllHeaders();
                String header;
                String result = "";
                for (Header h : headers) {
                    if ("Set-Cookie".equals(h.getName()) && h.getValue().contains("JSESSIONID=")) {
                        header = getSessionId(h.getValue());
                        result = newRequest(httpClient, header);
                        break;
                    }
                }
                return result;
            }
        });
    }

    private static String newRequest(CloseableHttpClient client, String sessionId) throws IOException {
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", sessionId);
        return client.execute(httpGet, httpResponse -> {
            InputStreamReader reader = new InputStreamReader(httpResponse.getEntity().getContent());
            char[] data = new char[1024];
            int len = 0;
            len = reader.read(data);
            return new String(data, 0, len);
        });
    }

    private static String getSessionId(String value){
        int i = value.indexOf("JSESSIONID=");
        return value.substring(i, value.indexOf(";", i));
    }
}
