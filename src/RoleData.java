import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.Utils;

public class RoleData {
    public String openID;
    public String openToken;
    public String comboID;
    public String comboToken;
    public String channelID;
    public JsonObject OAServer;
    public String accountType;
    public String oaReqKey;
    public int specialTag;

    public RoleData(String openID, String openToken, String comboID, String comboToken, String channelID, String accountType, String oaReqKey, int specialTag) throws Exception {
        String ver = "5.5.0";
        this.openID = openID;
        this.openToken = openToken;
        this.comboID = comboID;
        this.comboToken = comboToken;
        this.channelID = channelID;
        this.accountType = accountType;
        this.oaReqKey = ver + "_gf_android_" + oaReqKey;
        this.specialTag = specialTag;
        System.out.println("拿取OA伺服器...");
        this.OAServer = getOAServer();
        System.out.println("拿取OA伺服器成功");
    }

    public JsonObject getOAServer() throws Exception {
        String url = "https://global2.bh3.com/query_dispatch?version=" + oaReqKey + "&t=" + System.currentTimeMillis();
        String result = Utils.httpPost(url, "", false);
        System.out.println(result);
        JsonObject data = (JsonObject) JsonParser.parseString(result);

        JsonObject dispatch = (JsonObject) JsonParser.parseString(data.get("region_list").getAsJsonArray().get(0).toString());
        url = dispatch.get("dispatch_url").getAsString() + "?version=" + oaReqKey + "&t=" + System.currentTimeMillis();
        result = Utils.httpPost(url, "", false);
        System.out.println(result);

        return (JsonObject) JsonParser.parseString(result);
    }
}
