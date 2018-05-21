package com.meigsmart.slb767_stress.ui;

import android.widget.TextView;

import com.meigsmart.slb767_stress.R;

import butterknife.BindView;

public class HardwareActivity extends BaseActivity {
    private HardwareActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hardware;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);
    }
}
