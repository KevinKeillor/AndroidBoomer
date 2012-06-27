package com.groggypirate.boomer;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 21/06/12
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class XBMCNotificationReceiver extends IgnitedAsyncTask<MoviePlayerActivity, String, Void, Boolean>   {
    //protected Boolean doInBackground(String... urls) {
    private static final String TAG = "Boomer_XBMCNotificationReceiver";
    private SocketChannel socketChannel;

    @Override
    public Boolean run(String... urls) {

        try {
            // Open the socket to listen on
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(urls[0], 9090));

            // Create a StringBuffer so that we can convert the bytes to a String

            while(true){
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                int bytesRead;
                StringBuilder response = new StringBuilder();
                while ((bytesRead = socketChannel.read(buffer)) > 0){
                    if (bytesRead > 0){
                        // Now that we’re read information into the buffer, we need to "flip" it so that we
                        // can now get data out of the buffer.
                        buffer.flip();

                        // Create a CharSet that knows how to encode and decode standard text (UTF-8)
                        Charset charset = Charset.forName("UTF-8");

                        // Decode the buffer to a String using the CharSet and append it to our buffer
                        response.append( charset.decode( buffer ) );
                    }
                }
                response.append("\n");
                ActOnResponse(response);
            }

        } catch (IOException e) {
            Log.e(TAG, "IO Error:run", e);
        }

        return true;
    }

    /**
     * ActOnResponse
     * @param response
     */
    private void ActOnResponse(StringBuilder response) {
        Log.i(TAG, "Notification:" + response);
        // Deconstruct the response

        // pass on the related information based on the nature of the notification

    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Boolean result) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            Log.e(TAG,"IO Error:onPostExecute",e);
        }
    }

}
