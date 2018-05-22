package com.meigsmart.slb767_stress.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.ExpandRecyclerViewAdapter;
import com.meigsmart.slb767_stress.adapter.LoopAdapter;
import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.model.LoopModel;
import com.meigsmart.slb767_stress.util.DialogUtil;
import com.meigsmart.slb767_stress.util.PreferencesUtil;
import com.meigsmart.slb767_stress.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class TestLoopActivity extends BaseActivity implements LoopAdapter.OnLoopCallBack {
    private TestLoopActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private List<ExpandRecyclerViewAdapter.DataBean<LoopModel, LoopModel.LoopSubModel>> mList = new ArrayList<>();
    private LoopAdapter mAdapter;
    @BindArray(R.array.LoopGroupList)
    public String[] mLoopGroupList;
    @BindArray(R.array.LoopDelayItemList)
    public String[] mLoopDelayItemList;
    @BindArray(R.array.LoopActionItemList)
    public String[] mLoopActionItemList;
    private View dialog;
    private EditText mDialogEt;
    private int delayBeforeTime ;
    private int delayAfterTime;
    private int suspendTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_loop;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);

        delayBeforeTime = getResources().getInteger(R.integer.settings_loop_delay_before_hardware_time);
        delayAfterTime = getResources().getInteger(R.integer.settings_loop_delay_after_hardware_time);
        suspendTime = getResources().getInteger(R.integer.settings_loop_action_suspend_time);

        if (isPreferencesData(Const.LOOP_DELAY_BEFORE_TIME))delayBeforeTime = getPreferencesData(Const.LOOP_DELAY_BEFORE_TIME);
        if (isPreferencesData(Const.LOOP_DELAY_AFTER_TIME))delayAfterTime = getPreferencesData(Const.LOOP_DELAY_AFTER_TIME);
        if (isPreferencesData(Const.LOOP_SUSPEND_TIME))suspendTime = getPreferencesData(Const.LOOP_SUSPEND_TIME);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LoopAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getData(mLoopGroupList,mLoopDelayItemList,mLoopActionItemList,
                delayBeforeTime,delayAfterTime,suspendTime
        );
        mAdapter.setData(mList);
    }

    private void getData(String[] group, String[] delayItem, String[] actionItem,int delayBefore,int delayAfter,int suspendTime){
        List<LoopModel> list = new ArrayList<>();

        for (int i=0;i<group.length;i++){
            LoopModel loopModel = new LoopModel();
            loopModel.setName(group[i]);
            String[] str = null;
            List<LoopModel.LoopSubModel> subList = new ArrayList<>();
            if ( i == 0){
                str = delayItem;
            }else if (i == 1){
                str = actionItem;
            }
            for (int j=0;j<str.length;j++){
                LoopModel.LoopSubModel m = new LoopModel.LoopSubModel();
                m.setName(str[j]);
                if ( i == 0 ){
                    m.setTime(j==0?delayBefore:delayAfter);
                }else if (i == 1){
                    m.setTime(suspendTime);
                }
                subList.add(m);
            }
            loopModel.setList(subList);
            list.add(loopModel);
        }
        for (LoopModel model:list){
            mList.add(new ExpandRecyclerViewAdapter.DataBean(model,model.getList()));
        }
    }

    @Override
    public void onGroupItem(int groupPos) {
    }

    @Override
    public void onSubItemClick(final int groupPos, final int subPosition) {
        dialog = DialogUtil.customInputDialog(
                this,
                mList.get(groupPos).getGroupItem().getList().get(subPosition).getName(),
                getResources().getString(R.string.sure),
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(mDialogEt.getText().toString())){
                            ToastUtil.showBottomShort(getResources().getString(R.string.loop_input_flag));
                            return;
                        }
                        if (Integer.parseInt(mDialogEt.getText().toString())==0){
                            ToastUtil.showBottomShort(getResources().getString(R.string.loop_input_flag1));
                            return;
                        }
                        int t = Integer.parseInt(mDialogEt.getText().toString());
                        mList.get(groupPos).getGroupItem().getList().get(subPosition).setTime(t);

                        setPreferenceData(
                                mList.get(groupPos).getGroupItem().getList().get(subPosition).getName(),
                                t
                        );

                        mAdapter.notifyDataSetChanged();
                    }
                }, null
        );
        mDialogEt = (EditText) dialog.findViewById(R.id.dialog_et_txt);
        mDialogEt.setText(String.valueOf(mList.get(groupPos).getGroupItem().getList().get(subPosition).getTime()));
//        mDialogEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                assert imm != null;
//                imm.showSoftInput(mDialogEt, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
        mDialogEt.requestFocus();
    }
}
