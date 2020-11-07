package com.penglecode.xmodule.master4j.java.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/29 11:50
 */
public class HttpURLConnectionExample {

    public static void doGet() throws Exception {
        URL url = new URL("http://127.0.0.1:8181/now");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置请求方法，默认也为GET
        urlConnection.setRequestMethod("GET");
        /**
         * 设置是否向httpUrlConnection输出数据，因为这个是GET请求，
         * 所以请求体(正文)中不需要设置输出数据，所以设置为false，默认也为false
         */
        urlConnection.setDoOutput(false);
        //设置是否从httpUrlConnection读入数据，即是需要响应数据，默认为true
        urlConnection.setDoInput(true);
        //设置请求头
        urlConnection.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Pragma", "no-cache");
        //设置连接超时时间
        urlConnection.setConnectTimeout(6000);
        //设置不使用缓存
        urlConnection.setUseCaches(false);
        //urlConnection.getOutputStream()会隐含的进行connect()，所以此处可以不connect()
        urlConnection.connect();
        //获取响应状态码
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == 200) {
            //读取响应内容
            try(BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                String responseBody = br.lines().collect(Collectors.joining("\n"));
                System.out.println("responseBody = " + responseBody); //{"nowTime":"2020-09-29 12:21:05"}
            }
        }
        //关闭连接(其实就是关闭底层的那个Socket链接)
        urlConnection.disconnect();
    }

    public static void doPost() throws Exception {
        URL url = new URL("http://127.0.0.1:8181/login");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置请求方法，默认为GET
        urlConnection.setRequestMethod("POST");
        /**
         * 设置是否向httpUrlConnection输出数据，因为这个是POST请求，
         * 所以请求体(正文)中需要设置输出数据，所以设置为true，默认为false
         */
        urlConnection.setDoOutput(true);
        //设置是否从httpUrlConnection读入数据，即是需要响应数据，默认为true
        urlConnection.setDoInput(true);
        //设置请求头
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Pragma", "no-cache");
        //设置连接超时时间
        urlConnection.setConnectTimeout(6000);
        //设置不使用缓存
        urlConnection.setUseCaches(false);
        //urlConnection.getOutputStream()会隐含的进行connect()，所以此处可以不connect()
        urlConnection.connect();
        //向请求体中写入输出数据(即POST正文)
        //标准application/x-www-form-urlencoded类型的正文格式：参数键与值之间用=连接，每个键值对之间用&连接
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8))) {
            String requestBody = "username=admin&password=123456";
            bw.write(requestBody);
        }
        //获取响应状态码
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == 200) {
            //读取响应内容
            try(BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                String responseBody = br.lines().collect(Collectors.joining("\n"));
                System.out.println("responseBody = " + responseBody);
            }
        }
        //关闭连接(其实就是关闭底层的那个Socket链接)
        urlConnection.disconnect();
    }

    public static void main(String[] args) throws Exception {
        //doGet();
        doPost();
    }

}
