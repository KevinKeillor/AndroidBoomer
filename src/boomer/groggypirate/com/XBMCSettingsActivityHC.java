package boomer.groggypirate.com;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 24/06/12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class XBMCSettingsActivityHC extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart () {
        super.onStart();
    }

    @Override
    public void onBuildHeaders(List<Header> target) {

        loadHeadersFromResource(R.xml.preference_headers, target);
    }
}

