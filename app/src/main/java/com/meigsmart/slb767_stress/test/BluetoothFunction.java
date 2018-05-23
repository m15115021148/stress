package com.meigsmart.slb767_stress.test;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by chenMeng on 2018/5/23.
 */
public class BluetoothFunction {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int BLUETOOTH_OPEN = 0x001;
    private static final int BLUETOOTH_CLOSE = 0x002;
    private static final int DELAY = 3000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BLUETOOTH_OPEN:
                    openBlue();
                    break;
                case BLUETOOTH_CLOSE:
                    closeBlue();
                    break;
            }
        }
    };

    public BluetoothFunction(Context context){
        mBluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
            return ;
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return ;
        }
    }

    public void start(){
        openBlue();
    }

    public void reset(){
        mHandler.removeMessages(BLUETOOTH_CLOSE);
        mHandler.removeMessages(BLUETOOTH_OPEN);
    }

    private void openBlue(){
        if (mBluetoothAdapter!=null){
            if (!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
            mHandler.sendEmptyMessageDelayed(BLUETOOTH_CLOSE,DELAY);
        }
    }

    private void closeBlue(){
        if (mBluetoothAdapter!=null){
            if (mBluetoothAdapter.enable())mBluetoothAdapter.disable();
            mHandler.sendEmptyMessageDelayed(BLUETOOTH_OPEN,DELAY);
        }
    }

}
