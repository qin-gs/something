package com.qin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static String sendGet(String url, String param, Map<String, String> headers) {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            String urlNameString = url + "?" + param;
            System.out.println(urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();

            for (String key : map.keySet()) {
                System.out.println(key + " " + map.get(key));
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String sendPost(String url, String param, Map<String, String> header) {
        PrintWriter out = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            System.out.println(realUrl);
            URLConnection connection = realUrl.openConnection();
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            // 通过输出流对象将参数写出去
            out.print(param);
            out.flush();

            // 通过连接对象获取一个输入流，向远程读取
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) {

//        String getUrl = "https://www.baidu.com/s";
//        String getUrl = "https://blog.csdn.net/HezhezhiyuLe/article/details/92395041";
//        Map<String, String> getHeader = new HashMap<>();
//        getHeader.put("accept", "*/*");
//        getHeader.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
//
//        String result = sendGet(getUrl, "", getHeader);
//        System.out.println(result);

        Map<String, String> postHeader = new HashMap<>();
        postHeader.put("accept", "*/*");
        postHeader.put("Cookie", "JSESSIONID=2ffc2a61-8239-4b45-ae8e-a717c4609a05; UserName=%E6%B5%8B%E8%AF%95%E5%91%980720; GOOGOSOFT_USERTYPE=ADMIN; Admin-Token=2ffc2a61-8239-4b45-ae8e-a717c4609a05");
        postHeader.put("GGS-API", "2019");
        postHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        postHeader.put("x-token", "2ffc2a61-8239-4b45-ae8e-a717c4609a05");
        postHeader.put("Content-Type", "application/x-www-form-urlencoded");
        String postUrl = "http://192.168.10.81:9528/api/syscenter/policy/addPolicy";
        String postResult = sendPost(postUrl, "CLMC=postTest&YCRZSBCS=1&YCSDSJJG=1", postHeader);
        System.out.println(postResult);
    }
}
