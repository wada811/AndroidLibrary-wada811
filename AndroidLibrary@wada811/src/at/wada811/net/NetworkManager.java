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

public class NetworkManager {

    private WifiManager         mWifiManager;
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
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Wifiに接続しているかを返す
     * 
     * @return Wifiに接続している場合は true
     */
    public boolean isWifiConnected(){
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
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
     */
    public void setWifiEnabled(boolean enabled){
        mWifiManager.setWifiEnabled(enabled);
    }

    /**
     * Wifiの有効/無効を取得する
     * 
     * @return boolean
     */
    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }

    /**
     * Wifiネットワークに接続する
     * 
     * @return int 接続した networkId
     */
    public int connect(Wifi.Security security, String ssid, String password){
        int networkId = 0;
        WifiConfiguration config = getWifiConfig(security, ssid, password);
        networkId = mWifiManager.addNetwork(config);
        mWifiManager.saveConfiguration();
        mWifiManager.updateNetwork(config);
        if(networkId == -1){
            return networkId;
        }
        mWifiManager.startScan();
        for(ScanResult result : mWifiManager.getScanResults()){
            // Android4.2以降のダブルクォーテーションを除去
            String resultSSID = result.SSID.replace("\"", "");
            if(resultSSID.equals(ssid)){
                mWifiManager.enableNetwork(networkId, true);
                break;
            }
        }
        return networkId;
    }

    /**
     * Wifiネットワークから切断する
     * 
     * @return boolean
     */
    public boolean disconnect(){
        return mWifiManager.disconnect();
    }

    /**
     * Wifi接続設定情報を取得する
     * 
     * @return WifiConfiguration wifi接続情報
     */
    private WifiConfiguration getWifiConfig(Wifi.Security security, String ssid, String password){
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
    public String getSSID(){
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        return connectionInfo.getSSID();
    }

}
