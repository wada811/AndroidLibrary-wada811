/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import at.wada811.net.NetworkManager.Wifi.Security;
import at.wada811.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager {

    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;

    /**
     * コンストラクタ
     */
    public NetworkManager(Context context) {
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * ネットワークの接続状態を返す
     * 
     * @return ネットワークに接続している場合は true
     */
    public boolean isNetworkConnected(){
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * ネットワークの接続状態を返す
     * 
     * @param type ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE など
     * @return ネットワークに接続している場合は true
     */
    public boolean isNetworkConnected(int type){
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == type;
    }

    /**
     * Wifiに接続しているかを返す
     * 
     * @return Wifiに接続している場合は true
     */
    public boolean isWifiConnected(){
        return isNetworkConnected(ConnectivityManager.TYPE_WIFI);
    }

    public static class Wifi {

        public static enum Security {

            /** セキュリティなし */
            NONE("NONE"),
            /** WEP */
            WEP("WEP"),
            /** WPA/WPA2-PSK */
            WPA("WPA");

            private String type;

            Security(String type) {
                this.type = type;
            }

            public String getSecurityType(){
                return type;
            }
        }
    }

    /**
     * Wifiの有効/無効を設定する
     * 
     * @param enabled
     * @return
     */
    public boolean setWifiEnabled(boolean enabled){
        return mWifiManager.setWifiEnabled(enabled);
    }

    /**
     * Wifiの有効/無効を取得する
     * 
     * @return boolean
     */
    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }

    public boolean reconnect(){
        return mWifiManager.reconnect();
    }

    /**
     * Wifiネットワークに接続する
     * 
     * @return boolean
     */
    public boolean connect(String ssid){
        int networkId = 0;
        WifiConfiguration config = getWifiConfig(ssid);
        networkId = mWifiManager.addNetwork(config);
        if(networkId == -1){
            return false;
        }
        mWifiManager.saveConfiguration();
        mWifiManager.updateNetwork(config);
        config.networkId = networkId;
        return connect(config);
    }

    /**
     * Wifiネットワークに接続する
     * 
     * @return boolean
     */
    public boolean connect(String ssid, String password){
        int networkId = 0;
        Security security = getWifiSecurity(ssid);
        WifiConfiguration config = createWifiConfig(security, ssid, password);
        networkId = mWifiManager.addNetwork(config);
        if(networkId == -1){
            return false;
        }
        mWifiManager.saveConfiguration();
        mWifiManager.updateNetwork(config);
        config.networkId = networkId;
        return connect(config);
    }

    private boolean connect(WifiConfiguration config){
        mWifiManager.startScan();
        for(ScanResult result : mWifiManager.getScanResults()){
            // Android4.2以降のダブルクォーテーションを除去
            LogUtils.i("config.SSID: " + getSsidName(config.SSID));
            LogUtils.i("result.SSID: " + getSsidName(result.SSID));
            if(getSsidName(config.SSID).equals(getSsidName(result.SSID))){
                return mWifiManager.enableNetwork(config.networkId, true);
            }
        }
        return false;
    }

    /**
     * Wifiネットワークから切断する
     * 
     * @return boolean
     */
    public boolean disconnect(){
        return mWifiManager.disconnect();
    }

    public Security getWifiSecurity(String ssid){
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        for(ScanResult scanResult : scanResults){
            if(scanResult.SSID.equals(ssid)){
                if(scanResult.capabilities.contains(Security.WPA.type)){
                    return Security.WPA;
                }else if(scanResult.capabilities.contains(Security.WEP.type)){
                    return Security.WEP;
                }else{
                    return Security.NONE;
                }
            }
        }
        return Security.NONE;
    }

    /**
     * Wifi接続設定情報を取得する
     * 
     * @return WifiConfiguration wifi接続情報
     */
    public WifiConfiguration getWifiConfig(String ssid){
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        for(WifiConfiguration wifiConfiguration : configuredNetworks){
            if(wifiConfiguration.SSID.equals(ssid)){
                return wifiConfiguration;
            }
        }
        return null;
    }

    /**
     * Wifi接続設定情報を取得する
     * 
     * @return WifiConfiguration wifi接続情報
     */
    private WifiConfiguration createWifiConfig(Wifi.Security security, String ssid, String password){
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        switch(security){
            case NONE:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedAuthAlgorithms.clear();
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                break;
            case WEP:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.wepKeys[0] = "\"" + password + "\"";
                config.wepTxKeyIndex = 0;
                break;
            case WPA:
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.preSharedKey = "\"" + password + "\"";
                break;
        }
        return config;
    }

    public boolean scanWifi(){
        return mWifiManager.startScan();
    }

    public List<ScanResult> getScanResults(){
        ArrayList<ScanResult> results = new ArrayList<ScanResult>();
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        if(scanResults == null){
            scanResults = new ArrayList<ScanResult>();
        }
        for(ScanResult scanResult : scanResults){
            String ssid = getSsidName(scanResult.SSID);
            if(!TextUtils.isEmpty(ssid)){
                results.add(scanResult);
            }
        }
        return results;
    }

    public List<WifiConfiguration> getConfiguredNetworks(){
        List<WifiConfiguration> configs = new ArrayList<WifiConfiguration>();
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if(configuredNetworks == null){
            configuredNetworks = new ArrayList<WifiConfiguration>();
        }
        for(WifiConfiguration config : configuredNetworks){
            String ssid = getSsidName(config.SSID);
            if(!TextUtils.isEmpty(ssid)){
                configs.add(config);
            }
        }
        return configs;
    }

    /**
     * BSSIDを返す
     * 
     * @return BSSID
     */
    public String getBSSID(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getBSSID();
    }

    /**
     * リンクスピードを返す
     * 
     * @return リンクスピード(Mbps)
     */
    public int getLinkSpeed(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getLinkSpeed();
    }

    /**
     * IPアドレスを返す
     * 
     * @return ipAddress
     */
    public String getIpAddress(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        return ((ipAddress >> 0) & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "." + ((ipAddress >> 24) & 0xFF);
    }

    /**
     * MACアドレスを返す
     * 
     * @return MACアドレス
     */
    public String getMacAddress(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getMacAddress();
    }

    /**
     * ネットワークIDを返す
     * 
     * @return Network ID
     */
    public int getNetworkId(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getNetworkId();
    }

    /**
     * Wifi強度を取得する
     * 
     * @param numLevels 強度の段階数
     * @return int 強度
     */
    public int getRssiLevel(int numLevels){
        WifiInfo info = mWifiManager.getConnectionInfo();
        return WifiManager.calculateSignalLevel(info.getRssi(), numLevels);
    }

    /**
     * SSIDを返す
     * 
     * @return SSID
     */
    public String getSsid(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getSSID();
    }

    /**
     * Android4.2以降のダブルクォーテーションを除去したSSIDを返す
     * 
     * @return SSID
     */
    public String getSsidName(){
        return getSsidName(getSsid());
    }

    /**
     * Android4.2以降のダブルクォーテーションを除去
     * 
     * @return SSID
     */
    public String getSsidName(String ssid){
        if(ssid == null || ssid.replace("\"", "").equals("null")){
            return null;
        }
        return ssid.replace("\"", "");
    }

}
