package boomer.groggypirate.com;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22/06/12
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
public class XBMCSettingsActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xbmc_setting);
    }
}
