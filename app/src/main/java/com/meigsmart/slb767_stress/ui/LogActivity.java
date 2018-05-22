package com.meigsmart.slb767_stress.ui;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.util.PreferencesUtil;

import butterknife.BindView;

public class LogActivity extends BaseActivity implements View.OnClickListener{
    private LogActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.batteryInfo)
    public RelativeLayout mBatterInfo;
    @BindView(R.id.switch1)
    public Switch mSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_log;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);
        mBatterInfo.setOnClickListener(this);
        mSwitch.setChecked(PreferencesUtil.getFristLogin(this, Const.LOG_BATTERY_INFO));
    }

    @Override
    public void onClick(View v) {
        if (v == mBatterInfo){
            if (mSwitch.isChecked()){
                mSwitch.setChecked(false);
                PreferencesUtil.isFristLogin(mContext,Const.LOG_BATTERY_INFO,false);
            }else {
                mSwitch.setChecked(true);
                PreferencesUtil.isFristLogin(mContext,Const.LOG_BATTERY_INFO,true);
            }
        }
    }
}
