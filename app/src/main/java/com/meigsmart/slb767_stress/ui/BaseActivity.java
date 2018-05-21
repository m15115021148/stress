package com.meigsmart.slb767_stress.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.application.MyApplication;
import com.meigsmart.slb767_stress.db.FunctionBean;
import com.meigsmart.slb767_stress.model.TypeModel;
import com.meigsmart.slb767_stress.util.PreferencesUtil;
import com.meigsmart.slb767_stress.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected static final String TAG_ESC_ACTIVITY = "com.broader.esc";
    private MyBroaderEsc receiver;//广播
    private Unbinder butterKnife;//取消绑定
    protected boolean startBlockKeys = false;
    protected boolean isStartTest = false;//start test
    private PowerManager.WakeLock wakeLock = null;
    protected String mName = "";
    protected List<FunctionBean> mList = new ArrayList<>();
    protected int SUCCESS = 2;
    protected int FAILURE = 1;
    protected int NOTEST = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        acquireWakeLock();
        // 注册广播
        receiver = new MyBroaderEsc();
        registerReceiver(receiver, new IntentFilter(TAG_ESC_ACTIVITY));
        // 反射注解机制初始化
        butterKnife = ButterKnife.bind(this);
        initData();
    }

    protected abstract int getLayoutId();//获取布局layout

    protected abstract void initData();//初始化数据

    class MyBroaderEsc extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                butterKnife.unbind();
                finish();
            }
        }
    }

    protected List<TypeModel> getData(String[] array,Class[] cls){
        List<TypeModel> list = new ArrayList<>();
        for (int i=0;i<array.length;i++){
            TypeModel model = new TypeModel();
            model.setId(i);
            model.setName(array[i]);
            model.setType(0);
            model.setItemType(0);
            if (cls!=null)model.setCls(cls[i]);
            list.add(model);
        }
        return list;
    }

    protected List<TypeModel> getData(String[] array, int[] ids, Class[] cls){
        List<TypeModel> list = new ArrayList<>();
        for (int i=0;i<array.length;i++){
            if (ids[i] == 1){
                TypeModel model = new TypeModel();
                model.setId(i);
                model.setName(array[i]);
                if (cls!=null)model.setCls(cls[i]);
                model.setType(0);
                model.setItemType(0);
                list.add(model);
            }
        }
        return list;
    }

    protected List<TypeModel> getData(String[] array, int[] ids, Class[] cls,List<FunctionBean> f){
        List<TypeModel> list = new ArrayList<>();
        for (int i=0;i<array.length;i++){
            if (ids[i] == 1){
                TypeModel model = new TypeModel();
                model.setId(i);
                model.setName(array[i]);
                model.setCls(cls[i]);
                if ( f!=null && f.size()>0){
                    for (FunctionBean bean : f){
                        if (array[i].equals(bean.getSubclassName())){
                            model.setType(bean.getResults());
                            break;
                        }
                    }
                }else{
                    model.setType(0);
                }
                list.add(model);
            }
        }
        return list;
    }

    protected void startActivity(TypeModel model){
        if (model.getCls()!=null && model.getCls().equals(Class.class)){
            ToastUtil.showBottomShort(getResources().getString(R.string.to_be_developed));
            return;
        }
        if (model.getCls() != null){
            Intent intent = new Intent(this,model.getCls());
            intent.putExtra("name",model.getName());
            startActivityForResult(intent,1111);
        }
    }

    protected void sendErrorMsg(Handler handler,String error){
        Message msg = handler.obtainMessage();
        msg.what = 9999;
        msg.obj = error;
        handler.sendMessage(msg);
    }

    protected void sendErrorMsgDelayed(Handler handler,String error){
        Message msg = handler.obtainMessage();
        msg.what = 9999;
        msg.obj = error;
        handler.sendMessageDelayed(msg,2000);
    }

    protected void addData(String fatherName,String subName){
        FunctionBean sb = getSubData(fatherName, subName);
        if (sb!=null && !TextUtils.isEmpty(sb.getSubclassName()) && !TextUtils.isEmpty(sb.getFatherName()))return;

        FunctionBean bean = new FunctionBean();
        bean.setFatherName(fatherName);
        bean.setSubclassName(subName);
        bean.setResults(0);
        bean.setReason("NOTEST");

        MyApplication.getInstance().mDb.addData(bean);
    }

    protected void updateData(String fatherName, String subName, int result){
        MyApplication.getInstance().mDb.update(fatherName,subName,result,"");
    }

    protected void updateData(String fatherName, String subName, int result,String reason){
        MyApplication.getInstance().mDb.update(fatherName,subName,result,reason);
    }

    protected FunctionBean getSubData(String fatherName, String subName){
        return MyApplication.getInstance().mDb.getSubData(fatherName, subName);
    }

    protected List<FunctionBean> getFatherData(String fatherName){
        return MyApplication.getInstance().mDb.getFatherData(fatherName);
    }

    protected List<FunctionBean> getAllData(){
        return MyApplication.getInstance().mDb.getAllData();
    }

    protected boolean isPreferencesData(String key){
        return PreferencesUtil.getIntegerData(this,key)!=0;
    }

    protected void setPreferenceData(String key,int values){
        PreferencesUtil.setIntegerData(this,key,values);
    }

    protected int getPreferencesData(String key){
        return PreferencesUtil.getIntegerData(this,key);
    }

    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass() .getCanonicalName());
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (null != wakeLock && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseWakeLock();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_BACK:
                if (startBlockKeys) return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
