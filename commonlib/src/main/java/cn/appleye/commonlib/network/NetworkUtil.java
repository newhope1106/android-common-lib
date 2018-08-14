package cn.appleye.commonlib.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author newhope1106
 * date 2018/8/14
 * 网络工具类
 */
public class NetworkUtil {
    private Context mContext;

    /**
     * 网络连接状态变化广播接收器
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                onNetworkChanged();
            }
        }
    };

    private Set<NetworkChangeCallback> mCallbacks = new HashSet<>();


    private NetworkUtil(){}

    private interface LazyHolder{
        NetworkUtil sInstance = new NetworkUtil();
    }

    /**
     * 获取实例
     * */
    public static NetworkUtil getInstance(){
        return LazyHolder.sInstance;
    }

    /**
     * 注册网络变化广播
     * @param context 上下文，全局使用，建议用Application
     * @param callback 网络回调，注意调用{@link #removeCallback(NetworkChangeCallback)}注销，避免内存泄露
     * */
    public void registerReceiver(@NonNull Context context, NetworkChangeCallback callback) {
        //使用Application Context，避免内存泄露
        mContext = context.getApplicationContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mContext.registerReceiver(mReceiver, filter);

        mCallbacks.add(callback);
    }

    /**
     * 移除回调接口
     * @param callback 注销回调
     * */
    public void removeCallback(NetworkChangeCallback callback){
        mCallbacks.remove(callback);
    }

    /**
     * 注销网络变化广播，此时所有的网络回调都会清除
     * */
    public void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
        mCallbacks.clear();
    }

    /**
     * 获取当前网络状态
     * */
    public NetworkState getCurrentNetworkState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkState networkState;
        if(networkInfo != null) {
            int networkType = networkInfo.getType();
            if(ConnectivityManager.TYPE_WIFI == networkType) {
                networkState = new NetworkState(NetworkState.NetworkType.WIFI);
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                networkState.SSID = wifiInfo.getSSID();
                networkState.mac = wifiInfo.getMacAddress();
                networkState.rssi = wifiInfo.getRssi();

            } else if(ConnectivityManager.TYPE_MOBILE == networkType){
                networkState = new NetworkState(NetworkState.NetworkType.MOBILE);

                int subNetworkType = networkInfo.getSubtype();
                setSubNetworkType(subNetworkType, networkInfo.getSubtypeName(), networkState);
            } else {
                networkState = new NetworkState(NetworkState.NetworkType.NONE);
            }
        } else {
            networkState = new NetworkState(NetworkState.NetworkType.NONE);
        }

        return networkState;
    }

    private void onNetworkChanged() {
        dispatch(getCurrentNetworkState());
    }

    /**
     * 设置数据网络情况下，网络类型
     * @param subNetworkType 网络类型
     * @param subTypeName 网络名称
     * @param networkState 网络状态信息
     * */
    private void setSubNetworkType(int subNetworkType, String subTypeName, NetworkState networkState) {
        switch (subNetworkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                networkState.subNetworkType = NetworkState.SubNetworkType.G2;
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
            case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
            case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                networkState.subNetworkType = NetworkState.SubNetworkType.G3;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                networkState.subNetworkType = NetworkState.SubNetworkType.G4;
                break;
            default: {
                // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA")
                        || subTypeName.equalsIgnoreCase("CDMA2000")) {
                    networkState.subNetworkType = NetworkState.SubNetworkType.G3;
                } else {
                    networkState.subNetworkType = NetworkState.SubNetworkType.UNKNOWN;
                }

                break;
            }
        }
    }

    /**
     * 下发网络状态
     * */
    private void dispatch(NetworkState networkState){
        Iterator<NetworkChangeCallback> iterator = mCallbacks.iterator();
        while(iterator.hasNext()){
            iterator.next().onChange(networkState);
        }
    }
}
