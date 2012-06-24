package boomer.groggypirate.com;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22/06/12
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class XBMCSettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        addPreferencesFromResource(R.xml.preferences2);
    }
}
