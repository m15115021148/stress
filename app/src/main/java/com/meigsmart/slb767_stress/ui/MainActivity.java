package com.meigsmart.slb767_stress.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.MenuAdapter;
import com.meigsmart.slb767_stress.application.MyApplication;
import com.meigsmart.slb767_stress.config.Const;

import butterknife.BindArray;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener, MenuAdapter.OnMenuCallBack {
    private MainActivity mContext;
    @BindView(R.id.menu)
    public LinearLayout mMenu;
    @BindArray(R.array.MenuList)
    public String[] mMenuList;
    @BindArray(R.array.MenuConfigList)
    public int[] mMenuConfigList;
    private PopupWindow mPop;
    private MenuAdapter mMenuAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.startBlockKeys = true;
        mMenu.setOnClickListener(this);


        initPopupWindow();

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
        backgroundAlpha(1f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onItemClick(int position) {
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
}
