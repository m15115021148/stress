package com.meigsmart.slb767_stress.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.model.TypeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenMeng on 2018/4/26.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    private List<TypeModel> mList = new ArrayList<>();
    private onCustomItemListener mCallBack;

    public CustomAdapter(onCustomItemListener callBack) {
        this.mCallBack = callBack;
    }

    public interface onCustomItemListener {
        void onCustomItemClick(int position);
    }

    public void setData(List<TypeModel> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public List<TypeModel> getData() {
        return this.mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, null);
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

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initData(final int position) {
            mName.setText(mList.get(position).getName());

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null)mCallBack.onCustomItemClick(position);
                }
            });
        }
    }
}
