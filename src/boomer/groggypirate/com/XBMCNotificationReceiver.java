package boomer.groggypirate.com;

import android.os.AsyncTask;
import android.view.View;

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
public class XBMCNotificationReceiver extends //AsyncTask<String, Void, Boolean> {
    IgnitedAsyncTask<MoviePlayerActivity, String, Void, Boolean>   {
    //protected Boolean doInBackground(String... urls) {

    private SocketChannel socketChannel;

    @Override
    public Boolean run(String... urls) {

try {
            // Open the socket to listen on
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(urls[0], 9090));

            while (true){
                ByteBuffer buffer = ByteBuffer.allocate(2048);

                // Create a StringBuffer so that we can convert the bytes to a String
                StringBuffer response = new StringBuffer();

                int bytesRead;
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
                // Output the response
                System.out.println( "Response: " + response );
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
            return true;

    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Boolean result) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
