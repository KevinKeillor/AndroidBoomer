package boomer.groggypirate.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 23/06/12
 * Time: 01:30
 * To change this template use File | Settings | File Templates.
 */
public class XBMCSettings {
    private static XBMCSettings ourInstance = new XBMCSettings();

    SharedPreferences m_preferences;

    public String getPort() {
        return m_preferences.getString("XBMCPort", "8080");
    }

    public String getIpAddress() {
        return m_preferences.getString("XBMCHost", "192.168.1.14");
    }

    public String getName() {
        return m_preferences.getString("XBMCUserName", "user");
    }

    public String getPassword() {
        return m_preferences.getString("XBMCPassword", "pass");
    }

    public static XBMCSettings getInstance(Context context) {
        ourInstance.m_preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return ourInstance;
    }

    private XBMCSettings() {
    }
}
