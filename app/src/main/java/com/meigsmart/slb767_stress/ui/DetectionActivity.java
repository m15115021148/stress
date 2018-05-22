package com.meigsmart.slb767_stress.ui;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.adapter.DetectionAdapter;
import com.meigsmart.slb767_stress.config.Const;
import com.meigsmart.slb767_stress.log.LogUtil;
import com.meigsmart.slb767_stress.model.DetectionModel;
import com.meigsmart.slb767_stress.util.DialogUtil;
import com.meigsmart.slb767_stress.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class DetectionActivity extends BaseActivity implements DetectionAdapter.OnDetectionCallBack {
    private DetectionActivity mContext;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private DetectionAdapter mAdapter;
    @BindArray(R.array.DetectionList)
    public String[] mDetectionList;
    @BindArray(R.array.DetectionActionList)
    public String[] mDetectionActionList;
    private int mRetryTimes;
    private int mRetryDelay;
    private int mActionCurrPos ;
    private View dialog;
    private EditText mDialogEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detection;
    }

    @Override
    protected void initData() {
        mContext = this;
        super.mName = getIntent().getStringExtra("name");
        mTitle.setText(super.mName);

        mRetryTimes = getResources().getInteger(R.integer.settings_detection_default_config_retry_times);
        mRetryDelay = getResources().getInteger(R.integer.settings_detection_default_config_retry_delay);
        mActionCurrPos = mDetectionActionList.length-1;

        if (isPreferencesData(Const.DETECTION_RETRY_TIMES))mRetryTimes = getPreferencesData(Const.DETECTION_RETRY_TIMES);
        if (isPreferencesData(Const.DETECTION_RETRY_DELAY))mRetryDelay = getPreferencesData(Const.DETECTION_RETRY_DELAY);
        mActionCurrPos = getPreferencesData(Const.DETECTION_ACTION);//Do not make non-zero judgments
        LogUtil.w("mRetryTimes:"+mRetryTimes+"\nmRetryDelay:"+mRetryDelay+"\nmActionCurrPos:"+mActionCurrPos);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DetectionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(getData(mDetectionList,mDetectionActionList,mRetryTimes,mRetryDelay,mActionCurrPos));
    }

    private List<DetectionModel> getData(String[] detectionList,String[] actionList,int retryTimes,int retryDelay,int actionPos){
        List<DetectionModel> list = new ArrayList<>();
        for (int i=0;i<detectionList.length;i++){
            DetectionModel model = new DetectionModel();
            model.setName(detectionList[i]);
            list.add(model);
        }
        list.get(0).setTime(String.valueOf(retryTimes));
        list.get(1).setTime(String.valueOf(retryDelay)+"s");
        list.get(2).setTime(actionPos<actionList.length?actionList[actionPos]:actionList[actionList.length-1]);
        return list;
    }

    @Override
    public void onCustomItemClick(final int position) {
        if (mAdapter.getData().get(position).getName().equals(getResources().getString(R.string.detection_action))){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(mAdapter.getData().get(position).getName());
            builder.setSingleChoiceItems(mDetectionActionList, mActionCurrPos,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mActionCurrPos = which;
                            mAdapter.getData().get(position).setTime(mDetectionActionList[which]);

                            setPreferenceData(
                                    Const.DETECTION_ACTION,
                                    mActionCurrPos
                            );

                            mAdapter.notifyDataSetChanged();
                        }
                    });
            builder.setNegativeButton(getResources().getString(R.string.cancel),null);
            builder.create().show();
        }else {
            dialog = DialogUtil.customInputDialog(
                    this,
                    mAdapter.getData().get(position).getName(),
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
                            mAdapter.getData().get(position).setTime(String.valueOf(t)+"s");

                            setPreferenceData(
                                    mAdapter.getData().get(position).getName(),
                                    t
                            );

                            mAdapter.notifyDataSetChanged();
                        }
                    }, null
            );
            mDialogEt = (EditText) dialog.findViewById(R.id.dialog_et_txt);
            mDialogEt.setText(String.valueOf(
                    mAdapter.getData().get(position).getTime().contains("s")?mAdapter.getData().get(position).getTime().substring(0,mAdapter.getData().get(position).getTime().length()-1):
                            mAdapter.getData().get(position).getTime())
            );
            mDialogEt.requestFocus();
        }
    }
}
