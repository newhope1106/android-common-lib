package cn.appleye.demo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.appleye.commonlib.network.NetworkChangeCallback;
import cn.appleye.commonlib.network.NetworkState;
import cn.appleye.commonlib.network.NetworkUtil;
import cn.appleye.demo.R;

/**
 * @author newhope1106
 * date 2018/8/14
 * 网络范例
 */
public class NetworkFragment extends Fragment {

    private static final int MSG_NETWORK_CHANGED = 1000;

    private TextView mContentView;

    private Handler mMainHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MSG_NETWORK_CHANGED:{
                    NetworkState networkState = (NetworkState)msg.obj;

                    if(networkState != null){
                        mContentView.setText(networkState.toString());
                    }
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_network, null);

        mContentView = rootView.findViewById(R.id.network_info_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        NetworkUtil.getInstance().registerReceiver(getContext(), new NetworkChangeCallback() {
            @Override
            public void onChange(NetworkState networkState) {
                Message msg = Message.obtain();
                msg.what = MSG_NETWORK_CHANGED;
                msg.obj = networkState;
                mMainHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        NetworkUtil.getInstance().unregisterReceiver();
    }
}
