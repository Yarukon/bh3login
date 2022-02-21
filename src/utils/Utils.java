package utils;

import com.google.gson.JsonObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Utils {

    // 请求参数, 一般情况下不需要更改
    public static final String appID = "180";
    public static final String domain = "line1-sdk-center-login-sh.biligame.net";
    public static final String serverID = "378";
    public static final String oldBUVID = "XZ4596E46B8FB6B2152AD5BE95099CF082204";
    public static final String platformType = "3";
    public static final String apkSign = "d1b01b32b10526be2659108204a751d8";
    public static final String udid = "KREhESMUI0F4T3hPM08zSTEAZ1NhAnIYfA";

    // 安卓手机配置
    public static String androidID = "2e7793608ac77ee7"; // 验证ID
    public static String phoneBrand = "Xiaomi"; // 手机品牌
    public static String phoneModel = "Redmi Note 9 Pro 5G"; // 手机型号
    public static String screenDPI = "2400*1080"; // 屏幕分辨率
    public static String macAddress = "5B:AA:E5:B3:5F:6C"; // 设备MAC地址
    public static String imei = "863971059995835"; // IMEI号
    public static String supportAbis = "x86,armeabi-v7a,armeabi"; // 支持的架构

    public static JsonObject getCaptchaLoginRequestJson() {
        JsonObject obj = new JsonObject();

        obj.addProperty("operators", "1");
        obj.addProperty("merchant_id", "590");
        obj.addProperty("isRoot", "0");
        obj.addProperty("domain_switch_count", "0");
        obj.addProperty("sdk_type", "1");
        obj.addProperty("sdk_log_type", "1");
        obj.addProperty("timestamp", "");
        obj.addProperty("support_abis", supportAbis);
        obj.addProperty("access_key", "");
        obj.addProperty("sdk_ver", "3.4.2");
        obj.addProperty("oaid", "");
        obj.addProperty("dp", screenDPI);
        obj.addProperty("original_domain", "");
        obj.addProperty("imei", imei);
        obj.addProperty("version", "1");
        obj.addProperty("udid", udid);
        obj.addProperty("apk_sign", apkSign);
        obj.addProperty("platform_type", "3");
        obj.addProperty("old_buvid", oldBUVID);
        obj.addProperty("android_id", androidID);
        obj.addProperty("fingerprint", "");
        obj.addProperty("mac", macAddress);
        obj.addProperty("server_id", "378");
        obj.addProperty("domain", domain);
        obj.addProperty("app_id", "180");
        obj.addProperty("version_code", "19");
        obj.addProperty("net", "4");
        obj.addProperty("pf_ver", "6.0.1");
        obj.addProperty("cur_buvid", "XZ4596E46B8FB6B2152AD5BE95099CF082204");
        obj.addProperty("c", "1");
        obj.addProperty("brand", phoneBrand);
        obj.addProperty("client_timestamp", "");
        obj.addProperty("channel_id", "1");
        obj.addProperty("uid", "");
        obj.addProperty("game_id", "180");
        obj.addProperty("ver", "1.4.2-dev");
        obj.addProperty("model", phoneModel);

        return obj;
    }

    public static JsonObject getLoginRequestJson() {
        JsonObject obj = new JsonObject();

        obj.addProperty("operators", "1");
        obj.addProperty("merchant_id", "590");
        obj.addProperty("isRoot", "0");
        obj.addProperty("domain_switch_count", "0");
        obj.addProperty("sdk_type", "1");
        obj.addProperty("sdk_log_type", "1");
        obj.addProperty("timestamp", "");
        obj.addProperty("support_abis", supportAbis);
        obj.addProperty("access_key", "");
        obj.addProperty("sdk_ver", "3.4.2");
        obj.addProperty("oaid", "");
        obj.addProperty("dp", screenDPI);
        obj.addProperty("original_domain", "");
        obj.addProperty("imei", imei);
        obj.addProperty("version", "1");
        obj.addProperty("udid", udid);
        obj.addProperty("apk_sign", apkSign);
        obj.addProperty("platform_type", platformType);
        obj.addProperty("old_buvid", oldBUVID);
        obj.addProperty("android_id", androidID);
        obj.addProperty("fingerprint", "");
        obj.addProperty("mac", macAddress);
        obj.addProperty("server_id", "378");
        obj.addProperty("domain", domain);
        obj.addProperty("app_id", appID);
        obj.addProperty("pwd", "");
        obj.addProperty("version_code", "19");
        obj.addProperty("net", "4");
        obj.addProperty("pf_ver", "6.0.1");
        obj.addProperty("cur_buvid", "XZ4596E46B8FB6B2152AD5BE95099CF082204");
        obj.addProperty("c", "1");
        obj.addProperty("brand", phoneBrand);
        obj.addProperty("client_timestamp", "");
        obj.addProperty("channel_id", "1");
        obj.addProperty("uid", "0");
        obj.addProperty("game_id", "180");
        obj.addProperty("user_id", "");
        obj.addProperty("ver", "1.4.2-dev");
        obj.addProperty("model", phoneModel);

        return obj;
    }

    public static JsonObject getRSARequestJson() {
        JsonObject obj = new JsonObject();

        obj.addProperty("operators", "1");
        obj.addProperty("merchant_id", "590");
        obj.addProperty("isRoot", "0");
        obj.addProperty("domain_switch_count", "0");
        obj.addProperty("sdk_type", "1");
        obj.addProperty("sdk_log_type", "1");
        obj.addProperty("timestamp", "");
        obj.addProperty("support_abis", "x86,armeabi-v7a,armeabi");
        obj.addProperty("access_key", "");
        obj.addProperty("sdk_ver", "3.4.2");
        obj.addProperty("oaid", "");
        obj.addProperty("dp", screenDPI);
        obj.addProperty("original_domain", "");
        obj.addProperty("imei", imei);
        obj.addProperty("version", "1");
        obj.addProperty("udid", udid);
        obj.addProperty("apk_sign", apkSign);
        obj.addProperty("platform_type", platformType);
        obj.addProperty("old_buvid", oldBUVID);
        obj.addProperty("android_id", androidID);
        obj.addProperty("fingerprint", "");
        obj.addProperty("mac", macAddress);
        obj.addProperty("server_id", serverID);
        obj.addProperty("domain", domain);
        obj.addProperty("app_id", appID);
        obj.addProperty("version_code", "19");
        obj.addProperty("net", "4");
        obj.addProperty("pf_ver", "6.0.1");
        obj.addProperty("cur_buvid", "XZ4596E46B8FB6B2152AD5BE95099CF082204");
        obj.addProperty("c", "1");
        obj.addProperty("brand", phoneBrand);
        obj.addProperty("client_timestamp", "");
        obj.addProperty("channel_id", "1");
        obj.addProperty("uid", "");
        obj.addProperty("game_id", "180");
        obj.addProperty("ver", "1.4.2-dev");
        obj.addProperty("model", phoneModel);

        return obj;
    }

    public static String httpPost(String url, String data, boolean isLogin) throws Exception {
        // 创建HttpClient上下文
        HttpClientContext context = HttpClientContext.create();

        // 创建HttpClient
        HttpClient httpClient = HttpClients.createDefault();

        String resp;
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(data));

        if (isLogin) {
            httpPost.setHeader("User-Agent", "Mozilla/5.0 BSGameSDK");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Host", "line1-sdk-center-login-sh.biligame.net");
        } else {
            httpPost.setHeader("accept", "*/*");
        }

        resp = httpClient.execute(httpPost, httpResponse -> getStringFromInputStream(httpResponse.getEntity().getContent()), context);

        return resp;
    }

    public static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static HashMap<String, String> stripUrl(String urlIn) {
        HashMap<String, String> shit = new HashMap<>();
        String[] param = urlIn.substring(urlIn.indexOf("?") + 1).split("&");
        for(String s : param) {
            String[] split = s.split("=");
            shit.put(split[0], split[1]);
        }

        return shit;
    }
}
