package com.wukong.common.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    //创建httpclient连接池
    private static PoolingHttpClientConnectionManager connectionManager;
    static{
        connectionManager=new PoolingHttpClientConnectionManager();
        //定义连接池最大连接数
        connectionManager.setMaxTotal(200);
        //对指定的网址最多只有20个连接
        connectionManager.setDefaultMaxPerRoute(20);
    }

    private static CloseableHttpClient getCloseableHttpClient(){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        return httpClient;
    }

    private static String execute(HttpRequestBase httpRequestBase) throws IOException {
        httpRequestBase.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");

        //设置超时时间
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(10 * 1000).build();

        httpRequestBase.setConfig(config);
        CloseableHttpClient httpClient = getCloseableHttpClient();
        CloseableHttpResponse response = httpClient.execute(httpRequestBase);

        String html = EntityUtils.toString(response.getEntity(), "utf-8");
        return html;
    }

    public static String doGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        String html = execute(httpGet);
        return html;
    }

    public static String doPost(String url, Map<String,String> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        List<BasicNameValuePair> list = new ArrayList<>();
        for (String key : params.keySet()) {
            list.add(new BasicNameValuePair(key,params.get(key)));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(entity);

        return execute(httpPost);
    }
}