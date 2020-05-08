package com.github.hcsp.http;


import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;;

import java.io.IOException;;
import java.util.HashMap;
import java.util.Map;


public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //构建一个Client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        //伪装user-agent  header
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");



        // 构建body，然后使用你喜欢的JSON序列化库把这个map序列化成一个JSON字符串
        // 序列化在下面顺便完成了
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

//        我没能够获得形如Set-Cookie: XXXXXXXXX; Path=/; HttpOnly的Header，但我个人判断http应该是给了我respon

//       我的理解是：在这个地方想办法获取到cookie就能够成功了
//        CloseableHttpClient httpClient2 = HttpClients.createDefault();
//        HttpPost httpPost2 = new HttpPost("http://47.91.156.35:8000/auth");
//        //Cookie: JSESSIONID=XXXXXXXXX
//        httpPost2.addHeader("Cookie", cookie);


        StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());


    }
}
