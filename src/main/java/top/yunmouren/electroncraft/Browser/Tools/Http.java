package top.yunmouren.electroncraft.Browser.Tools;

import top.yunmouren.electroncraft.ElectronCraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Http {
    public static String get(String webUrl) {
        String charset = "UTF-8";
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn;

        try {
            URL url = new URL(webUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000); // 30 秒连接超时
            conn.setReadTimeout(10000); // 10 秒读取超时
            conn.setRequestProperty("Content-Type", "application/json;charset=" + charset);
            conn.setRequestProperty("Accept", "application/json");

            // 检查响应状态码
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 成功获取响应
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                // 非 200 响应状态码
                ElectronCraft.logger.error("Error during request. Response code: " + responseCode);
                ElectronCraft.logger.error("Response message: " + conn.getResponseMessage());
                ElectronCraft.logger.error("URL: " + webUrl);
            }
        } catch (Exception e) {
            ElectronCraft.logger.warn(e.getMessage());
        } finally {
            // 关闭 BufferedReader
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ElectronCraft.logger.warn(ioe.getMessage());
            }
        }

        // 返回结果字符串
        return result.toString();
    }

    public static String post(String webUrl, String json) {
        String charset = "UTF-8";
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn;
        try {
            URL url = new URL(webUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json;charset=" + charset);
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            out.write(json);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                ElectronCraft.logger.warn(String.valueOf(conn.getResponseCode()));
            }
        } catch (Exception e) {
            ElectronCraft.logger.warn(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ElectronCraft.logger.warn(ioe.getMessage());
            }
        }
        return result.toString();
    }
}
