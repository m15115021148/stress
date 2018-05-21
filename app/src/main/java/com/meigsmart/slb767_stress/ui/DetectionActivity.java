package com.meigsmart.slb767_stress.ui;


import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;

import butterknife.BindView;

public class DetectionActivity extends BaseActivity {
    private DetectionActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detection;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);
    }
}
