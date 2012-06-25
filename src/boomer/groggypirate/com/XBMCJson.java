package boomer.groggypirate.com;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;


/**
 * XBMCJson
 */
public class XBMCJson {

    private static final String TAG = "Boomer_XBMCJson";
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int CONNECTION_READ_TIMEOUT = 5000;

    public void writeCommand(final String method, final JSONObject jsonCommand, final Context context) throws IOException, JSONException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    _writeCommand(method, jsonCommand, context);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }).start();
    }

    public JSONArray _writeCommand(String method, JSONObject jsonCommand,Context context) throws IOException, JSONException {
        URL url;

        JSONArray response = new JSONArray();
        XBMCSettings xbmcSettings = XBMCSettings.getInstance(context);
        String path = "http://"+xbmcSettings.getIpAddress()+":"+xbmcSettings.getPort()+"/jsonrpc";
        url = new URL(path);

        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        uc.setConnectTimeout(CONNECTION_TIMEOUT);
        uc.setReadTimeout(CONNECTION_READ_TIMEOUT);
        uc.setRequestProperty("Connection", "close");

        String authEncoded = EncodeAuth(xbmcSettings.getName()+":"+xbmcSettings.getPassword());

        if (authEncoded != null) {
            uc.setRequestProperty("Authorization", "Basic " + authEncoded);
        }

        OutputStreamWriter out = new OutputStreamWriter(
                uc.getOutputStream());
        //out.write("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"Player.PlayPause\",\"params\":{\"playerid\":1}}");
        out.write(CreateCommand(method, jsonCommand).toString());
        out.close();

        try {
            response = GetResponse(uc);
        } catch (JSONException e) {
            final String debugUrl;
            debugUrl = URLDecoder.decode(url.toString(), "UTF-8");
            Log.e(TAG,debugUrl);
            Log.e(TAG,e.getMessage());
        }
        return response;
    }

    private JSONObject CreateCommand(String method, JSONObject params) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("jsonrpc", "2.0");
        json.put("id", "0");   // we don't really use this so value is always zero
        json.put("method", method);
        json.put("params", params);

        return json;
    }

    private JSONArray GetResponse(URLConnection uc) throws IOException, JSONException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()), 8192);
        final StringBuilder response = new StringBuilder();
        String line;

        while((line = in.readLine()) != null) {
            response.append(line);
        }

        JSONArray JsonResponse = new JSONArray(response.toString());

        in.close();
        return JsonResponse;
    }

    private String EncodeAuth(String auth) {
        String authEncoded;
        authEncoded = Base64.encodeBytes(auth.getBytes());
        return authEncoded;
    }
}
