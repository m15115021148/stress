package com.meigsmart.slb767_stress.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.MainAdapter;
import com.meigsmart.slb767_stress.adapter.MenuAdapter;
import com.meigsmart.slb767_stress.application.MyApplication;
import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.log.LogUtil;
import com.meigsmart.slb767_stress.model.TypeModel;
import com.meigsmart.slb767_stress.util.DialogUtil;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        MenuAdapter.OnMenuCallBack ,MainAdapter.OnMainItemCallBack {
    private MainActivity mContext;
    @BindView(R.id.menu)
    public LinearLayout mMenu;
    @BindArray(R.array.MenuList)
    public String[] mMenuList;
    @BindArray(R.array.MenuConfigList)
    public int[] mMenuConfigList;
    private PopupWindow mPop;
    private MenuAdapter mMenuAdapter;
    @BindView(R.id.spinner)
    public Spinner mSpinner;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    @BindView(R.id.close)
    public TextView mClose;
    @BindView(R.id.start)
    public TextView mStart;
    @BindArray(R.array.MainList)
    public String[] mMainList;
    @BindArray(R.array.MainConfigList)
    public int[] mMainConfigList;
    private boolean isStarting;
    private MainAdapter mMainAdapter;
    @BindView(R.id.flag)
    public TextView mFlag;
    @BindView(R.id.title)
    public TextView mTitle;
    private int delayBeforeTime = 3;
    private int delayAfterTime = 3;
    private int suspendTime = 5;
    private static final int START = 0x001;
    private static final int STOP = 0x002;
    private static final int CANCEL = 0x003;
    private static final int TIMER_FIRST = 0x004;
    private static final int TIMER_SECOND = 0x005;
    private static final int TIMER_FINISH = 0x006;
    private int mTestCurrCount = 1;
    private CustomTestTimer mTestTimer ;
    private CustomActionTimer mActionTimer;
    private View dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.startBlockKeys = true;
        mMenu.setOnClickListener(this);
        mMenu.setVisibility(View.VISIBLE);
        mClose.setOnClickListener(this);
        mStart.setOnClickListener(this);

        initSpinner();
        initMainRecyclerView();
        initPopupWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();
        delayBeforeTime = getResources().getInteger(R.integer.settings_loop_delay_before_hardware_time);
        delayAfterTime = getResources().getInteger(R.integer.settings_loop_delay_after_hardware_time);
        suspendTime = getResources().getInteger(R.integer.settings_loop_action_suspend_time);

        if (isPreferencesData(Const.LOOP_DELAY_BEFORE_TIME))delayBeforeTime = getPreferencesData(Const.LOOP_DELAY_BEFORE_TIME);
        if (isPreferencesData(Const.LOOP_DELAY_AFTER_TIME))delayAfterTime = getPreferencesData(Const.LOOP_DELAY_AFTER_TIME);
        if (isPreferencesData(Const.LOOP_SUSPEND_TIME))suspendTime = getPreferencesData(Const.LOOP_SUSPEND_TIME);

        LogUtil.w("delayBeforeTime:"+delayBeforeTime+"\ndelayAfterTime:"+delayAfterTime+"\nsuspendTime:"+suspendTime);

        mTestTimer = new CustomTestTimer(1000*(delayBeforeTime+1), 1000);
        mActionTimer = new CustomActionTimer(1000*(delayAfterTime+1), 1000);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START:
                    mSpinner.setEnabled(false);
                    startTimer(mTestTimer);
                    mTitle.setText(String.format(getResources().getString(R.string.app_test_number),mTestCurrCount));
                    break;
                case STOP:
                    mSpinner.setEnabled(true);
                    cancelTimer(mTestTimer);
                    cancelTimer(mActionTimer);
                    mTitle.setText(R.string.app_name);
                    mTestCurrCount = 1;
                    mHandler.removeMessages(TIMER_FINISH);
                    break;
                case CANCEL:
                    break;
                case TIMER_FIRST://start test function
                    startTestFunction();
                    break;
                case TIMER_SECOND:
                    mTestCurrCount++;
                    startTimer(mTestTimer);
                    mTitle.setText(String.format(getResources().getString(R.string.app_test_number),mTestCurrCount));
                    break;
                case TIMER_FINISH:
                    startTimer(mActionTimer);
                    break;
            }
        }
    };

    private void startTestFunction(){
        mHandler.sendEmptyMessageDelayed(TIMER_FINISH,1000*suspendTime);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.TAG_ESC_ACTIVITY);
        sendBroadcast(intent);
        System.exit(0);
        finish();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mMenu) {
            if (mPop!=null && !mPop.isShowing()){
                mPop.showAsDropDown(mMenu, -10, 10);
            }
        }
        if (v == mClose){
            Intent intent = new Intent();
            intent.setAction(BaseActivity.TAG_ESC_ACTIVITY);
            sendBroadcast(intent);
            System.exit(0);
            finish();
        }
        if (v == mStart){
            if (isStarting){//stop test
                isStarting = false;
                mStart.setText(R.string.start);
                mClose.setSelected(false);
                mClose.setEnabled(true);
                setStat(0);
                mMainAdapter.setClick(true);

                mHandler.sendEmptyMessage(STOP);

            }else {//start test
                isStarting = true;
                mStart.setText(R.string.stop);
                mClose.setSelected(true);
                mClose.setEnabled(false);
                setStat(1);
                mMainAdapter.setClick(false);

                mHandler.sendEmptyMessage(START);

            }
        }
    }

    private void initMainRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainAdapter = new MainAdapter(this,this);
        mRecyclerView.setAdapter(mMainAdapter);
        List<TypeModel> data = getData(mMainList, mMainConfigList, null);
        createDBData(data);
        mMainAdapter.setData(getData(mMainList, mMainConfigList, null,getAllData()));
        if (!isSelectAll()){
            mStart.setSelected(true);
            mStart.setEnabled(false);
        }
    }

    private void createDBData(List<TypeModel> list){
        for (TypeModel m: list){
            addData(m.getName());
        }
    }

    private boolean isSelectAll(){
        for (TypeModel model:mMainAdapter.getData()){
            if (model.getType() == 1){
                return true;
            }
        }
        return false;
    }

    private void initSpinner(){
        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.LoopActionList, R.layout.spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initPopupWindow() {
        mPop = new PopupWindow();
        View view = getLayoutInflater().inflate(R.layout.menu_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pop_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMenuAdapter = new MenuAdapter(this);
        recyclerView.setAdapter(mMenuAdapter);
        mMenuAdapter.setData(getData(mMenuList,mMenuConfigList, Const.menuList));

        mPop.setContentView(view);
        mPop.setWidth(MyApplication.getInstance().screenWidth/2);
        mPop.setHeight(MyApplication.getInstance().screenHeight/2);
        mPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPop.setOutsideTouchable(true);
        mPop.setTouchable(true);
        mPop.setAnimationStyle(R.style.MenuTranslateStyle);
        backgroundAlpha(1f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onItemClick(int position) {//popupWindow item click
        mPop.dismiss();
        if (position == 0){
            startActivity(mMenuAdapter.getData().get(position));
        }else if (position == 1){
            dialog = DialogUtil.customPromptDialog(this,
                    mMenuAdapter.getData().get(position).getName(),
                    getResources().getString(R.string.sure),
                    getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetData();
                            mMainAdapter.setData(getData(mMainList, mMainConfigList, null,getAllData()));
                            mMainAdapter.notifyDataSetChanged();
                        }
                    },null);
            TextView tv= (TextView) dialog.findViewById(R.id.dialog_tv_txt);
            tv.setText(R.string.reset_flag);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPop!=null && mPop.isShowing()){
                mPop.dismiss();
                return true;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setStat(int state){
        List<TypeModel> data = mMainAdapter.getData();
        for (TypeModel model: data){
            if (model.getType() == 1){
                model.setItemType(state);
            }
        }
        mMainAdapter.notifyDataSetChanged();
    }

    private void startTimer(CountDownTimer timer){
        mFlag.setVisibility(View.VISIBLE);
        timer.start();
    }

    private void cancelTimer(CountDownTimer timer){
        mFlag.setVisibility(View.GONE);
        timer.cancel();
    }

    @Override
    public void onMainItemListener(int position) {
        if (mMainAdapter.getData().get(position).getType() == 0){
            mMainAdapter.getData().get(position).setType(1);
            update(mMainAdapter.getData().get(position).getName(),SELECT);
        }else {
            mMainAdapter.getData().get(position).setType(0);
            update(mMainAdapter.getData().get(position).getName(),UNSELECT);
        }
        mStart.setSelected(false);
        mStart.setEnabled(true);
        if (!isSelectAll()){
            mStart.setSelected(true);
            mStart.setEnabled(false);
        }
        mMainAdapter.notifyDataSetChanged();
    }

    private class CustomTestTimer extends CountDownTimer{

        public CustomTestTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mFlag.setText(String.format(getResources().getString(R.string.test_will_tag), l / 1000));
        }

        @Override
        public void onFinish() {
            cancelTimer(mTestTimer);
            mHandler.sendEmptyMessage(TIMER_FIRST);
        }
    }

    private class CustomActionTimer extends CustomTestTimer{

        public CustomActionTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mFlag.setText(String.format(getResources().getString(R.string.action_will_tag), l / 1000));
        }

        @Override
        public void onFinish() {
            cancelTimer(mActionTimer);
            mHandler.sendEmptyMessage(TIMER_SECOND);
        }

    }

}
