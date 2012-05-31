package wcf1.groggypirate.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Wcf1Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Go();
    }
        
    public void Go() {
	    RestClient client = new RestClient("http://192.168.1.14:8732/Service1/data/1");
	
	    try {
	        client.Execute(RestClient.RequestMethod.GET);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }    
	    String response = client.getResponse();    
	    Log.d(response, response);
    }
    
}

