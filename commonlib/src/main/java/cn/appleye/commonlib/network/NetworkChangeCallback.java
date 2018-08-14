package cn.appleye.commonlib.network;

/**
 * @author newhope1106
 * date 2018/8/14
 * 网络变化回调方法
 */
public interface NetworkChangeCallback {
    void onChange(NetworkState networkState);
}
