package com.groggypirate.boomer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.security.PrivateKey;


/**
 *  XBMCJson
 */
public class XBMCJson {

    private static final String TAG = "Boomer_XBMCJson";
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int CONNECTION_READ_TIMEOUT = 5000;
    private static Integer m_id = 0;

    /**
     * writeCommand
     * @param method the method to call
     * @param params the parameters to add to the called method
     * @param context
     */
    public void writeCommand(final String method, final JSONObject params, final Context context){
        new Thread(new Runnable() {
            public void run() {
                try {
                    _writeCommand(method, params, context);
                } catch (IOException e) {
                    Log.e(TAG,"IO Error on XBMCJson:writeCommand",e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException on XBMCJson:writeCommand", e);
                }
            }
        }).start();
    }
    public void writeCommand(final String method, final JSONObject params, final Context context, final Handler handler){
        new Thread(new Runnable() {
            public void run() {
                try {
                    String response = _writeCommand(method, params, context);
                    // our handler
                    Message Msg = new Message();
                    Bundle bndl = new Bundle();
                    bndl.putString("Response", response);
                    Msg.setData(bndl);
                    Msg.what = 1;
                    handler.sendMessage(Msg);

                } catch (IOException e) {
                    Log.e(TAG,"IO Error on XBMCJson:writeCommand",e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException on XBMCJson:writeCommand", e);
                }
            }
        }).start();
    }

    /**
     * _writeCommand private method to call
     * @param method the method to call
     * @param params the parameters to add to the called method
     * @param context the activity from which we can get the preferences
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private String _writeCommand(String method, JSONObject params,Context context) throws IOException, JSONException {
        URL url;

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
        out.write(CreateCommand(method, params).toString());
        out.close();

        return  GetResponse(uc);
    }

    /**
     * Returns a complete XBMC command from the given method and parameters
     * @param method the method to call
     * @param params the parameters to add to the called method
     * @return  the created command
     * @throws JSONException
     */
    private JSONObject CreateCommand(String method, JSONObject params) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("jsonrpc", "2.0");
        json.put("id", m_id.toString());   // we don't really use this so value is always zero
        json.put("method", method);
        json.put("params", params);
        m_id++;
        return json;
    }

    /**
     *
     * @param uc the URLConnection to receive a response from
     * @return the response from the previous command
     * @throws IOException
     * @throws JSONException
     */
    private String GetResponse(URLConnection uc) throws IOException, JSONException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()), 8192);
        final StringBuilder response = new StringBuilder();
        String line;

        while((line = in.readLine()) != null) {
            response.append(line);
        }

        //JSONObject JsonResponse = new JSONObject(response.toString());

        in.close();
        return response.toString();
    }

    /**
     * Returns a base64 encoded version of the supplied authorisation code
     *
     * @param  auth the authorisation code to be encoded
     * @return      the base64 encoded authorisation code
     * @see         String
     */
    private String EncodeAuth(String auth) {
        String authEncoded;
        authEncoded = Base64.encodeBytes(auth.getBytes());
        return authEncoded;
    }
}
