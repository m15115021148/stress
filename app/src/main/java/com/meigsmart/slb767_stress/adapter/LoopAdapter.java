package com.meigsmart.slb767_stress.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;
import com.meigsmart.slb767_stress.model.LoopModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenMeng on 2018/5/21.
 */
public class LoopAdapter extends ExpandRecyclerViewAdapter<LoopAdapter.GroupItemViewHolder,LoopAdapter.SubItemViewHolder> {
    private List<DataBean<LoopModel, LoopModel.LoopSubModel>> mList = new ArrayList<>();
    private OnLoopCallBack mCallBack;

    public LoopAdapter(OnLoopCallBack callBack){
        this.mCallBack = callBack;
    }

    public interface OnLoopCallBack{
        void onGroupItem(int groupPos);
        void onSubItemClick(int groupPos, int subPosition);
    }

    public void setData(List data) {
        mList = data;
        notifyNewData(mList);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loop_group_item,null);
        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loop_sub_item,null);
        return new SubItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder emptyViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {
        ((GroupItemViewHolder) holder).name.setText(mList.get(groupItemIndex).getGroupItem().getName());
    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        ((SubItemViewHolder) holder).time.setText(mList.get(groupItemIndex).getSubItems().get(subItemIndex).getTime()+"s");
        ((SubItemViewHolder) holder).name.setText(mList.get(groupItemIndex).getSubItems().get(subItemIndex).getName());
    }

    @Override
    public void onEmptyBindViewHolder(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
        if (mCallBack!=null)mCallBack.onGroupItem(groupItemIndex);
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        if (mCallBack!=null)mCallBack.onSubItemClick(groupItemIndex,subItemIndex);
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setEnabled(false);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.name)
        TextView name;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
