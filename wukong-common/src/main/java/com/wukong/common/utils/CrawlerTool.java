package com.wukong.common.utils;

import com.wukong.common.model.NewsBO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTool {
    /**
     * 1、先介绍环境、前置条件
     * 2、jsoup 采集虎扑新闻列表、详情页
     * 3、httpclient 正则采集虎扑列表页
     * @param args
     */
    public static void main(String[] args) {
        String url = "https://news.ifeng.com/";
        CrawlerTool crawlerTool = new CrawlerTool();
        crawlerTool.jsoupList(url);
//        crawlerBase.httpClientList(url);
    }

    /**
     * jsoup方式 获取虎扑新闻列表页
     * @param url 虎扑新闻列表页url
     */
    public static List<NewsBO> jsoupList(String url){
        List<NewsBO> newsBOS = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("ul.news-stream-basic-news-list > li > a");
            for (Element element:elements){
                // 获取详情页链接
                String d_url = "http:" + element.attr("href");
                String title1 = element.attr("title");
                NewsBO newsBO = new NewsBO();
                newsBO.setTitle(title1);
                newsBO.setUrl(d_url);
                newsBOS.add(newsBO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsBOS;
    }

    /**
     * httpclient + 正则表达式 获取虎扑新闻列表页
     * @param url 虎扑新闻列表页url
     */
    public void httpClientList(String url){
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String body = EntityUtils.toString(entity,"utf-8");
                /*
                 * 替换掉换行符、制表符、回车符，这样就只有一行了
                 * 只有空格符号和其他正常字体
                 */
                if (body!=null) {
                    Pattern p = Pattern.compile("\t|\r|\n");
                    Matcher m = p.matcher(body);
                    body = m.replaceAll("");
                    /*
                     * 提取列表页的正则表达式
                     * 去除换行符之后的 li
                     * <div class="list-hd">                                    <h4>                                        <a href="https://voice.hupu.com/nba/2485167.html"  target="_blank">与球迷亲切互动！凯尔特人官方晒球队开放训练日照片</a>                                    </h4>                                </div>
                     */
                    Pattern pattern = Pattern
                            .compile("<div class=\"list-hd\">\\s* <h4>\\s* <a href=\"(.*?)\"\\s* target=\"_blank\">(.*?)</a>\\s* </h4>\\s* </div>" );

                    Matcher matcher = pattern.matcher(body);
                    // 匹配出所有符合正则表达式的数据
                    while (matcher.find()){
//                        String info = matcher.group(0);
//                        System.out.println(info);
                        // 提取出链接和标题
                        System.out.println("详情页链接："+matcher.group(1)+" ,详情页标题："+matcher.group(2));

                    }
                }else {
                    System.out.println("处理失败！！！获取正文内容为空");
                }

            } else {
                System.out.println("处理失败！！！返回状态码：" + response.getStatusLine().getStatusCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}