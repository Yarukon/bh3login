import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;
import utils.BSGameSDK;
import utils.Utils;

import java.awt.Dimension;
import java.awt.*;
import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            //new BSGameSDK("", "").loginNoCaptcha();
            doScan();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void doScan() {
        try {
            // 登录结果, 解析自个写就行
            JsonObject result = (JsonObject) JsonParser.parseString(FileUtils.readFileToString(new File("E:/BH3BiliLogin/result.json"), "UTF-8"));

            QRScanner scanner = new QRScanner(result.get("uid").getAsString(), result.get("access_key").getAsString());

            // 全屏截图找二维码 所以确保没别的二维码在桌面上
            Robot robot = new Robot();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            System.out.println("屏幕大小: " + d);

            MultiFormatReader multiFormatReader = new MultiFormatReader();
            LuminanceSource source = new BufferedImageLuminanceSource(robot.createScreenCapture(new Rectangle(d)));
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map hints = new HashMap();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            Result scanResult = multiFormatReader.decode(binaryBitmap, hints);

            // 解析URL并登入
            HashMap<String, String> shit = Utils.stripUrl(URLDecoder.decode(scanResult.getText(), "UTF-8"));
            scanner.bizKey = shit.get("biz_key");
            scanner.ticket = shit.get("ticket");
            scanner.appID = shit.get("app_id");

            scanner.doQRLogin();
        } catch (Exception ex) {
            // 错误抛出
            System.out.println("如果错误为com.google.zxing.NotFoundException代表未找到二维码");
            ex.printStackTrace();
        }
    }

}
