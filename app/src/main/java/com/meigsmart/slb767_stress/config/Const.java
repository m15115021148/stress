package com.meigsmart.slb767_stress.config;

import com.meigsmart.slb767_stress.ui.HardwareActivity;
import com.meigsmart.slb767_stress.ui.LogActivity;
import com.meigsmart.slb767_stress.ui.SettingsActivity;
import com.meigsmart.slb767_stress.ui.TestLoopActivity;

/**
 * Created by chenMeng on 2018/5/18.
 */
public class Const {
    public static boolean isCanBackKey = true;
    public static final String LOOP_DELAY_BEFORE_TIME = "Delay before Hardware Test";
    public static final String LOOP_DELAY_AFTER_TIME = "Delay after Hardware Test";
    public static final String LOOP_SUSPEND_TIME = "Suspend time";

    public static Class[] menuList = {
            SettingsActivity.class,
            Class.class,
            Class.class,
            Class.class,
            Class.class
    };

    public static Class[] settingsList = {
            TestLoopActivity.class,
            HardwareActivity.class,
            LogActivity.class
    };

}
