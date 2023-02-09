import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class TradeDemo {
    String url = "https://open-api.bingx.com";
    String apiKey = "Set your api key here!!";
    String secretKey = "Set your secret key here!!";

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }


    String generateHmac256(String message) {
        try {
            byte[] bytes = hmac("HmacSHA256", secretKey.getBytes(), message.getBytes());
            return bytesToHex(bytes);
        } catch (Exception e) {
            System.out.println("generateHmac256 expection:" + e);
        }
        return "";
    }


    String getMessageToDigest(String method, String path, TreeMap<String, String> parameters) {
        Boolean first = true;
        String valueToDigest = "";
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (!first) {
                valueToDigest += "&";
            }
            first = false;
            valueToDigest += e.getKey() + "=" + e.getValue();
        }
        return valueToDigest;
    }

    String getRequestUrl(String path, String parameters) {
        String urlStr = url + path + "?" + parameters;
        return urlStr;
    }

    void send_request(String requestUrl, String method) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)conn;
            http.setRequestMethod(method); // PUT is another valid option
            http.addRequestProperty("X-BX-APIKEY", apiKey);
            http.setDoOutput(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String result = "";
            String line = "";
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = in.readLine()) != null) {
                result += line;
            }

            System.out.println("\t"+result);

        } catch (Exception e) {
            System.out.println("expection:" + e);
        }
    }


    void placeMarketOrder(String symbol, String side, String positionSide, String quantity) {
        String method = "POST";
        String path = "/openApi/swap/v2/trade/order";
        String timestamp = ""+new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("side", side);
        parameters.put("type", "MARKET");
        parameters.put("positionSide", positionSide);
        parameters.put("quantity", quantity);

        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        send_request(requestUrl, method);
    }

    public static void main(String[] args) {
        TradeDemo h = new TradeDemo();

        System.out.println("placeMarketOrder:");
        h.placeMarketOrder("LINK-USDT", "BUY", "LONG", "5");
    }
}
