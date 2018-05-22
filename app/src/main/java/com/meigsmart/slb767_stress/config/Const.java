package com.meigsmart.slb767_stress.config;

import com.meigsmart.slb767_stress.ui.DetectionActivity;
import com.meigsmart.slb767_stress.ui.FunctionActivity;
import com.meigsmart.slb767_stress.ui.HardwareActivity;
import com.meigsmart.slb767_stress.ui.LogActivity;
import com.meigsmart.slb767_stress.ui.SettingsActivity;
import com.meigsmart.slb767_stress.ui.TestLoopActivity;

/**
 * Created by chenMeng on 2018/5/18.
 */
public class Const {
    public static boolean isCanBackKey = true;
    //Corresponding xml configuration
    public static final String LOOP_DELAY_BEFORE_TIME = "Delay before Hardware Test";
    public static final String LOOP_DELAY_AFTER_TIME = "Delay after Hardware Test";
    public static final String LOOP_SUSPEND_TIME = "Suspend time";
    public static final String DETECTION_RETRY_TIMES = "Retry times";
    public static final String DETECTION_RETRY_DELAY = "Retry delay";
    public static final String DETECTION_ACTION = "detection action";

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

    public static Class[] hardwareList = {
            DetectionActivity.class,
            FunctionActivity.class
    };

}
