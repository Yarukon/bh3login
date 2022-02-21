package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.net.URI;

public class BSGameSDK {
    public String loginURL = "https://line1-sdk-center-login-sh.biligame.net/";

    private String account;
    private String password;

    public BSGameSDK(String account, String password) {
        this.account = account;
        this.password = password;
    }

    /**
     * 无验证码登录, 登录失败会返回包含验证码所需的Json, 调用失败会返回错误信息
     * @return String[] 元素0 {0成功, -1登入失败, -2调用失败} 元素1 {错误信息或Json}
     * @throws Exception 抛出处理
     */
    public String[] login() throws Exception {
        String[] data = loginNoCaptcha();

        if (data[0].equals("0")) {
            JsonObject obj = (JsonObject) JsonParser.parseString(data[1]);
            if (obj.get("code").getAsInt() == 500002) {
                return new String[] {"-2", "密码错误! 请检查输入的值是否正确!"};
            } else if (!obj.has("access_key")) {
                return new String[] {"-1", obj.toString()};
            } else {
                return new String[] {"0", obj.toString()};
            }
        }

        return new String[] {"-2", data[1]};
    }

    /**
     * 打开极验的认证界面
     * @param gt 极验参数
     * @param challenge 极验参数
     * @param userID 极验参数
     */
    public void openCaptcha(String gt, String challenge, String userID) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(String.format("https://help.tencentbot.top/geetest/?captcha_type=1&challenge=%s&gt=%s&userid=%s&gs=1", challenge, gt, userID));
                desktop.browse(uri);
            } else {
                System.out.println("您的系统不支持调用浏览器访问网页!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 使用验证码登录
     * @param challenge 极验参数
     * @param userID 极验参数
     * @param captchaResult 极验验证结果
     * @return String[] 元素0 {0成功, -1失败} 元素1 {错误信息}
     * @throws Exception 抛出处理
     */
    public String[] loginWithCaptcha(String challenge, String userID, String captchaResult) throws Exception {
        JsonObject data = Utils.getRSARequestJson();
        String query = Encrypt.loginSetSign(data);
        JsonObject rsa = (JsonObject) JsonParser.parseString(Utils.httpPost(loginURL + "api/client/rsa", query, true));
        if (rsa.get("code").getAsInt() != 0) {
            System.out.println("登入出错! 信息: " + rsa.get("message").getAsString());
        } else {
            data = Utils.getLoginRequestJson();
            String public_key = rsa.has("rsa_key") ? rsa.get("rsa_key").getAsString() : "";
            String hash = rsa.has("hash") ? rsa.get("hash").getAsString() : "";
            data.addProperty("gt_user_id", userID);
            data.addProperty("validate", "");
            data.addProperty("challenge", challenge);
            data.addProperty("user_id", account);
            data.addProperty("validate", captchaResult);
            data.addProperty("seccode", captchaResult + "|jordan");
            data.addProperty("pwd", Encrypt.rsaEncrypt(hash + password, public_key));
            query = Encrypt.loginSetSign(data);
            Utils.httpPost(loginURL + "api/client/login", query, true);

            return new String[] {"0", Utils.httpPost(loginURL + "api/client/login", query, true)};
        }

        return new String[] {"-1", rsa.has("message") ? rsa.get("message").getAsString() : "未知错误"};
    }

    /**
     * 不使用验证码登录
     * @return String[] 元素0 {0成功, -1失败} 元素1 {错误信息}
     * @throws Exception 抛出处理
     */
    public String[] loginNoCaptcha() throws Exception {
        JsonObject data = Utils.getRSARequestJson();
        String query = Encrypt.loginSetSign(data);
        JsonObject rsa = (JsonObject) JsonParser.parseString(Utils.httpPost(loginURL + "api/client/rsa", query, true));

        if (rsa.get("code").getAsInt() == 0) {
            data = Utils.getLoginRequestJson();
            String public_key = rsa.has("rsa_key") ? rsa.get("rsa_key").getAsString() : "";
            String hash = rsa.has("hash") ? rsa.get("hash").getAsString() : "";
            data.addProperty("gt_user_id", "");
            data.addProperty("validate", "");
            data.addProperty("challenge", "");
            data.addProperty("user_id", account);
            data.addProperty("pwd", Encrypt.rsaEncrypt(hash + password, public_key));
            query = Encrypt.loginSetSign(data);

            return new String[] {"0", Utils.httpPost(loginURL + "api/client/login", query, true)};
        }

        return new String[] {"-1", rsa.has("message") ? rsa.get("message").getAsString() : "未知错误"};
    }
}
