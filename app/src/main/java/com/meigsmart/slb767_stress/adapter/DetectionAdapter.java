package com.meigsmart.slb767_stress.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.model.DetectionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenMeng on 2018/4/26.
 */
public class DetectionAdapter extends RecyclerView.Adapter<DetectionAdapter.Holder> {
    private List<DetectionModel> mList = new ArrayList<>();
    private OnDetectionCallBack mCallBack;

    public DetectionAdapter(OnDetectionCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnDetectionCallBack {
        void onCustomItemClick(int position);
    }

    public void setData(List<DetectionModel> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public List<DetectionModel> getData() {
        return this.mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detection_list_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.initData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout)
        public RelativeLayout mLayout;
        @BindView(R.id.name)
        public TextView mName;
        @BindView(R.id.time)
        public TextView mTime;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initData(final int position) {
            DetectionModel model = mList.get(position);
            mName.setText(model.getName());
            mTime.setText(model.getTime());

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null)mCallBack.onCustomItemClick(position);
                }
            });
        }
    }
}
