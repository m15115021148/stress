package com.meigsmart.slb767_stress.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.CustomAdapter;
import com.meigsmart.slb767_stress.config.Const;

import butterknife.BindArray;
import butterknife.BindView;

public class HardwareActivity extends BaseActivity implements CustomAdapter.onCustomItemListener{
    private HardwareActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    @BindArray(R.array.HardwareList)
    public String[] mHardwareList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hardware;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setData(getData(mHardwareList, Const.hardwareList));
    }

    @Override
    public void onCustomItemClick(int position) {
        startActivity(mAdapter.getData().get(position));
    }
}
