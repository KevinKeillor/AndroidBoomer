package com.groggypirate.boomer;

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
import boomer.groggypirate.com.R;

/**
 * XBMCSettingsActivityHC
 */
public class XBMCSettingsActivityHC extends PreferenceActivity {

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     */
    @Override
    public void onStart () {
        super.onStart();
    }

    /**
     * onBuildHeaders
     * @param target a list of headers
     */
    @Override
    public void onBuildHeaders(List<Header> target) {

        loadHeadersFromResource(R.xml.preference_headers, target);
    }
}

