package com.meigsmart.slb767_stress.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.log.LogUtil;
import com.meigsmart.slb767_stress.ui.MainActivity;
import com.meigsmart.slb767_stress.util.PreferencesUtil;

/**
 * Created by chenMeng on 2018/1/22.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d("action:"+action);

        if (ACTION.equals(action)) {
            if (PreferencesUtil.getIntegerData(context, Const.LOOP_ACTION) == 1){
                Intent main = new Intent(context, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(main);
            }
        }
    }
}
