package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Encrypt {
    public static final String SALT = "dbf8f1b4496f430b8a3c0f436a35b931";
    public static final String BH_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDvekdPMHN3AYhm/vktJT+YJr7cI5DcsNKqdsx5DZX0gDuWFuIjzdwButrIYPNmRJ1G8ybDIF7oDW2eEpm5sMbL9zs\n9ExXCdvqrn51qELbqj0XxtMTIpaCHFSI50PfPpTFV9Xt/hmyVwokoOXFlAEgCn+Q\nCgGs52bFoYMtyi+xEQIDAQAB";
    public static final String BH_APP_KEY = "0ebc517adb1b62c6b408df153331f9aa";

    public static String loginSetSign(JsonObject data) {
        StringBuilder query = new StringBuilder();

        TreeMap<String, JsonElement> ordered = new TreeMap<>();

        data.addProperty("timestamp", System.currentTimeMillis() / 1000);
        data.addProperty("client_timestamp", System.currentTimeMillis() / 1000);

        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
            ordered.put(entry.getKey(), entry.getValue());

            if (entry.getKey().equals("pwd")) {
                try {
                    query.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().getAsString(), "UTF-8")).append("&");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            query.append(entry.getKey()).append("=").append(entry.getValue().getAsString()).append("&");
        }

        StringBuilder sign = new StringBuilder();

        ordered.forEach((key, value) -> {
            sign.append(value.getAsString());
        });

        sign.append(SALT);

        query.append("sign=").append(getMD5(sign.toString()).toLowerCase());
        System.out.println(sign);
        System.out.println(query);
        return query.toString();
    }

    public static String bh3LoginSetSign(JsonObject data) {
        TreeMap<String, JsonElement> datas = new TreeMap<>();
        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
            datas.put(entry.getKey(), entry.getValue());
        }

        StringBuilder sign = new StringBuilder();
        datas.forEach((key, value) -> {
            if (value.isJsonObject()) {
                sign.append(key).append("=").append(value).append("&");
            } else {
                sign.append(key).append("=").append(value.getAsString()).append("&");
            }
        });

        return HMACSHA256Encrypt(sign.substring(0, sign.length() - 1), BH_APP_KEY);
    }

    public static String getMD5(String str) {
        byte[] digest = null;

        try {
            digest = MessageDigest.getInstance("md5").digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert digest != null;
        return new BigInteger(1, digest).toString(16);
    }

    public static String rsaEncrypt(String str, String publicKey) throws Exception {
        //生成公钥
        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        byte[] decoded = Base64.getDecoder().decode(parse.matcher(publicKey).replaceFirst("$1").replace("\n", ""));
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    private static String HMACSHA256Encrypt(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hash;
    }
}
