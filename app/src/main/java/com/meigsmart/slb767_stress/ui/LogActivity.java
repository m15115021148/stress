package com.meigsmart.slb767_stress.ui;

import android.widget.TextView;

import com.meigsmart.slb767_stress.R;

import butterknife.BindView;

public class LogActivity extends BaseActivity {
    private LogActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_log;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);
    }
}
