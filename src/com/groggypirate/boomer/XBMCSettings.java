package com.groggypirate.boomer;

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

    /**
     *
     * @return the port of the XBMC connection
     */
    public String getPort() {
        return m_preferences.getString("XBMCPort", "8080");
    }

    /**
     *
     * @return the ip address of the XBMC connection
     */
    public String getIpAddress() {
        return m_preferences.getString("XBMCHost", "192.168.1.14");
    }

    /**
     *
     * @return the username for the XBMC connection
     */
    public String getName() {
        return m_preferences.getString("XBMCUserName", "user");
    }

    /**
     *
     * @return  the password for the XBMC connection
     */
    public String getPassword() {
        return m_preferences.getString("XBMCPassword", "pass");
    }

    /**
     *
     * @param context Activity to get the preferences
     * @return the XBMCSettings containing
     */
    public static XBMCSettings getInstance(Context context) {
        ourInstance.m_preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return ourInstance;
    }

    /**
     * private constructor
     */
    private XBMCSettings() {
    }
}
