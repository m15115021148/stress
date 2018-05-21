package com.meigsmart.slb767_stress.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.CustomAdapter;
import com.meigsmart.slb767_stress.config.Const;

import butterknife.BindArray;
import butterknife.BindView;

public class FunctionActivity extends BaseActivity implements CustomAdapter.onCustomItemListener{
    private FunctionActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    @BindArray(R.array.MainList)
    public String[] mMainList;
    @BindArray(R.array.MainConfigList)
    public int[] mMainConfigList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_function;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setData(getData(mMainList,mMainConfigList,null));
    }

    @Override
    public void onCustomItemClick(int position) {

    }
}
