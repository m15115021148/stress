package com.meigsmart.slb767_stress.ui;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.MainAdapter;
import com.meigsmart.slb767_stress.adapter.MenuAdapter;
import com.meigsmart.slb767_stress.application.MyApplication;
import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.model.TypeModel;

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
    private int mTestTime = 3;
    private int mActionTime = 3;
    private static final int START = 0x001;
    private static final int STOP = 0x002;
    private static final int CANCEL = 0x003;
    private static final int TIMER_FIRST = 0x004;
    private static final int TIMER_SECOND = 0x005;
    private static final int TIMER_FINISH = 0x006;
    private int mTestCurrCount = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.startBlockKeys = true;
        mMenu.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mStart.setOnClickListener(this);

        initSpinner();
        initMainRecyclerView();
        initPopupWindow();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START:
                    mTitle.setText(String.format(getResources().getString(R.string.app_test_number),mTestCurrCount));
                    startTimer(mTestTimer);
                    break;
                case STOP:
                    cancelTimer(mTestTimer);
                    cancelTimer(mActionTimer);
                    mTitle.setText(R.string.app_name);
                    mTestCurrCount = 1;
                    break;
                case CANCEL:
                    break;
                case TIMER_FIRST://start test function
                    startTestFunction();
                    break;
                case TIMER_SECOND:
                    mTestCurrCount++;
                    mTitle.setText(String.format(getResources().getString(R.string.app_test_number),mTestCurrCount));
                    startTimer(mTestTimer);
                    break;
                case TIMER_FINISH:
                    startTimer(mActionTimer);
                    break;
            }
        }
    };

    private void startTestFunction(){
        mHandler.sendEmptyMessageDelayed(TIMER_FINISH,5000);
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
        mMainAdapter.setData(getData(mMainList,mMainConfigList,null));
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
        startActivity(mMenuAdapter.getData().get(position));
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
        }else {
            mMainAdapter.getData().get(position).setType(0);
        }
        mMainAdapter.notifyDataSetChanged();
    }

    private CountDownTimer mTestTimer = new CountDownTimer(1000*(mTestTime+1), 1000) {
        @Override
        public void onTick(long l) {
            mFlag.setText(String.format(getResources().getString(R.string.test_will_tag), l / 1000));
        }

        @Override
        public void onFinish() {
            cancelTimer(mTestTimer);
            mHandler.sendEmptyMessage(TIMER_FIRST);
        }
    };

    private CountDownTimer mActionTimer = new CountDownTimer(1000*(mActionTime+1), 1000) {
        @Override
        public void onTick(long l) {
            mFlag.setText(String.format(getResources().getString(R.string.action_will_tag), l / 1000));
        }

        @Override
        public void onFinish() {
            cancelTimer(mActionTimer);
            mHandler.sendEmptyMessage(TIMER_SECOND);
        }
    };

}
