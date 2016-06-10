package com.agiledge.keocometemployee.constants;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Pateel on 1/11/2016.
 */
public class GetMacAddress extends Activity{
    public static String MAC_ADDRESS;
    public String GetMac(){

        WifiManager wifi=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wnfo=wifi.getConnectionInfo();
       MAC_ADDRESS=wnfo.getMacAddress();
        return wnfo.getMacAddress();
    }

}
