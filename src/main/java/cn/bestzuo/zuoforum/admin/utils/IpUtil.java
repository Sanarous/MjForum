package cn.bestzuo.zuoforum.admin.utils;

import cn.bestzuo.zuoforum.admin.pojo.IpAddress;
import cn.bestzuo.zuoforum.admin.pojo.IpInfo;
import cn.bestzuo.zuoforum.util.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP工具
 */
public class IpUtil {

    /**
     * 获取登录时的公网IP地址
     *
     * @return 公网IP地址
     */
    public static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Pattern p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
        }
        return ip;
    }

    /**
     * 获取公网ipv4地址
     *
     * @return
     */
    public static String getMyV4IP() throws Exception {
        String ip = null;
        String url = "http://pv.sohu.com/cityjson";
        try {
            String request = "";
            String ipInfo = getData(url, request);
            IpInfo info = JsonUtils.jsonToPojo(ipInfo.substring(19), IpInfo.class);
            return info.getCip();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取ip地址信息
     * @param url
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String getData(String url, String request) throws UnsupportedEncodingException, IOException {
        String charset = "UTF-8";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        HttpURLConnection connect = (HttpURLConnection) (new URL(url).openConnection());
        connect.setRequestMethod("GET");
        connect.setDoOutput(true);
        connect.setConnectTimeout(1000 * 10);
        connect.setReadTimeout(1000 * 80);
        connect.setRequestProperty("ContentType", "application/x-www-form-urlencoded"); //采用通用的URL百分号编码的数据MIME类型来传参和设置请求头
        connect.setDoInput(true);
        // 连接
        connect.connect();
        // 发送数据
        connect.getOutputStream().write(request.getBytes(charset));
        // 接收数据
        int responseCode = connect.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream in = connect.getInputStream();
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = in.read(data, 0, data.length)) != -1) {
                outStream.write(data, 0, len);
            }
            in.close();
        }
        // 关闭连接
        connect.disconnect();
        String response = outStream.toString("UTF-8");
        return response;
    }

    /**
     * 通过IP获取地址(需要联网，调用淘宝的IP库)
     *
     * @param ip ip地址
     * @return ip地址信息
     */
    public static String getIpInfo(String ip) {
        if ("127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        String info = "";
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
            htpcon.setRequestMethod("GET");
            htpcon.setDoOutput(true);
            htpcon.setDoInput(true);
            htpcon.setUseCaches(false);

            InputStream in = htpcon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder temp = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            JSONObject obj = (JSONObject) JSON.parse(temp.toString());
            if (obj.getIntValue("code") == 0) {
                JSONObject data = obj.getJSONObject("data");
                info += data.getString("country") + "-";  //国家
                info += data.getString("region") + "省-";   //省
                info += data.getString("city") + "市";   //市
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }


    public static String getIpv4Info(String ip){
        String url = "http://ip-api.com/json/"+ ip + "?lang=zh-CN";
        try {
            String request = "";
            String ipInfo = getData(url, request);
            System.out.println(ipInfo);
            IpAddress address = JsonUtils.jsonToPojo(ipInfo, IpAddress.class);
            return address.getCountry() + address.getRegionName();
        } catch (Exception e) {
            e.printStackTrace();
            return "暂无具体地址信息";
        }
    }
}

