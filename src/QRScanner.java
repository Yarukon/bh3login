import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.Encrypt;
import utils.Utils;

public class QRScanner {

    public String uid;
    public String accessKey;

    public String ticket;
    public String appID;
    public String bizKey;

    public String openID;
    public String comboID;
    public String comboToken;

    public QRScanner(String uid, String accessKey) {
        this.uid = uid;
        this.accessKey = accessKey;
    }

    public void doQRLogin() throws Exception {
        JsonObject obj = (JsonObject) JsonParser.parseString(verify());
        if (obj == null) {
            System.out.println("错误: null");
        } else if (obj.get("retcode").getAsInt() != 0) {
            System.out.println("错误: " + obj.get("message").toString());
        } else {
            comboID = obj.get("data").getAsJsonObject().get("combo_id").getAsString();
            openID = obj.get("data").getAsJsonObject().get("open_id").getAsString();
            comboToken = obj.get("data").getAsJsonObject().get("combo_token").getAsString();

            RoleData role = new RoleData(openID, "", comboID, comboToken, "14", "2", "bilibili", 0);

            JsonObject data = new JsonObject();
            data.addProperty("app_id", appID);
            data.addProperty("device", Utils.androidID);
            data.addProperty("ticket", ticket);
            data.addProperty("ts", System.currentTimeMillis());

            JsonObject signData = data.deepCopy();
            data.addProperty("sign", Encrypt.bh3LoginSetSign(signData));
            String url = "https://api-sdk.mihoyo.com/" + bizKey + "/combo/panda/qrcode/scan";
            String result = Utils.httpPost(url, data.toString(), false);
            System.out.println(result);
            JsonObject resultJson = (JsonObject) JsonParser.parseString(result);

            if (resultJson.get("retcode").getAsInt() != 0) {
                System.out.println("扫码错误: " + (resultJson.has("message") ? resultJson.get("message").getAsString() : "二维码过期"));
            } else {

                JsonObject confirmJson = genRequest(role, data);
                url = "https://api-sdk.mihoyo.com/" + bizKey + "/combo/panda/qrcode/confirm";
                result = Utils.httpPost(url, confirmJson.toString().replace("\\/","/"), false);
                System.out.println(result);

                data = (JsonObject) JsonParser.parseString(result);

                if (data.get("retcode").getAsInt() != 0) {
                    System.out.println("确认登录错误: " + (data.has("message") ? data.get("message").getAsString() : "二维码过期"));
                } else {
                    System.out.println("确认登录成功!");
                }
            }
        }
    }

    public String verify() throws Exception {
        JsonObject accountData = new JsonObject();
        accountData.addProperty("uid", Integer.valueOf(uid));
        accountData.addProperty("access_key", accessKey);

        JsonObject requestData = new JsonObject();
        requestData.addProperty("app_id", 1);
        requestData.addProperty("channel_id", 14);
        requestData.addProperty("data", accountData.toString());
        requestData.addProperty("device", Utils.androidID);
        requestData.addProperty("sign", Encrypt.bh3LoginSetSign(requestData));

        return Utils.httpPost("https://api-sdk.mihoyo.com/bh3_cn/combo/granter/login/v2/login", requestData.toString(), false);
    }

    private JsonObject genRequest(RoleData roleData, JsonObject qrCheck) {
        JsonObject rawJson = new JsonObject();
        rawJson.addProperty("heartbeat", false);
        rawJson.addProperty("open_id", roleData.openID);
        rawJson.addProperty("device_id", Utils.androidID);
        rawJson.addProperty("app_id", appID);
        rawJson.addProperty("channel_id", roleData.channelID);
        rawJson.addProperty("combo_token", roleData.comboToken);
        rawJson.addProperty("asterisk_name", "Yarukon69420");
        rawJson.addProperty("combo_id", roleData.comboID);
        rawJson.addProperty("account_type", roleData.accountType);

        if (roleData.openID != null && !roleData.openID.isEmpty()) {
            rawJson.addProperty("open_token", roleData.openToken);
            rawJson.addProperty("guest", false);
        }

        JsonObject dispatchJson = new JsonObject();
        dispatchJson.addProperty("account_url", roleData.OAServer.get("account_url").getAsString());
        dispatchJson.addProperty("account_url_backup", roleData.OAServer.get("account_url_backup").getAsString());
        dispatchJson.add("asset_bundle_url_list", roleData.OAServer.get("asset_bundle_url_list").getAsJsonArray());
        dispatchJson.add("ex_resource_url_list", roleData.OAServer.get("ex_resource_url_list").getAsJsonArray());
        dispatchJson.add("ex_audio_and_video_url_list", roleData.OAServer.get("ex_audio_and_video_url_list").getAsJsonArray());

        roleData.OAServer.get("ext").getAsJsonObject().addProperty("is_checksum_off", 1);
        dispatchJson.add("ext", roleData.OAServer.get("ext"));

        dispatchJson.add("gameserver", roleData.OAServer.get("gameserver"));
        dispatchJson.add("gateway", roleData.OAServer.get("gateway"));
        dispatchJson.addProperty("oaserver_url", roleData.OAServer.get("oaserver_url").getAsString());
        dispatchJson.addProperty("region_name", roleData.OAServer.get("region_name").getAsString());
        dispatchJson.addProperty("retcode", 0);
        dispatchJson.addProperty("is_data_ready", true);
        dispatchJson.add("server_ext", roleData.OAServer.get("server_ext"));

        JsonObject data = new JsonObject();
        data.addProperty("accountType", roleData.accountType);
        data.addProperty("accountID", roleData.openID);
        data.addProperty("accountToken", roleData.comboToken);
        data.add("dispatch", dispatchJson);

        JsonObject ext = new JsonObject();
        ext.add("data", data);

        JsonObject payload = new JsonObject();
        payload.addProperty("raw", rawJson.toString());
        payload.addProperty("proto", "Combo");
        payload.addProperty("ext", ext.toString().replace("\\", ""));

        JsonObject confirm = new JsonObject();
        confirm.addProperty("device", Utils.androidID);
        confirm.addProperty("app_id", Integer.parseInt(appID));
        confirm.addProperty("ts", System.currentTimeMillis());
        confirm.addProperty("ticket", ticket);
        confirm.add("payload", payload);

        qrCheck.add("payload", payload);
        confirm.addProperty("sign", Encrypt.bh3LoginSetSign(qrCheck));

        return confirm;
    }

}
