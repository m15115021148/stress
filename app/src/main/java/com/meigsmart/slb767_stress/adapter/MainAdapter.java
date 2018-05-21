package com.meigsmart.slb767_stress.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Holder> {
    private Context mContext;
    private List<TypeModel> mList = new ArrayList<>();
    private OnMainItemCallBack mCallBack;
    private boolean isClick = true;

    public MainAdapter(Context context,OnMainItemCallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }

    public interface OnMainItemCallBack {
        void onMainItemListener(int position);
    }

    public void setClick(boolean click) {
        isClick = click;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, null);
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
        public LinearLayout mLayout;
        @BindView(R.id.name)
        public TextView mName;
        @BindView(R.id.img)
        public ImageView mImg;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initData(final int position) {
            TypeModel model = mList.get(position);
            mName.setText(model.getName());

            if (model.getType() == 0){
                mImg.setBackground(mContext.getResources().getDrawable(R.drawable.group_normal));
            }else if (model.getType() == 1){
                if (model.getItemType() == 0){
                    mImg.setBackground(mContext.getResources().getDrawable(R.drawable.group_selected));
                }else {
                    mImg.setBackground(mContext.getResources().getDrawable(R.drawable.group_grey));
                }
            }

            if (isClick){
                mLayout.setEnabled(true);
            }else {
                mLayout.setEnabled(false);
            }

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null)mCallBack.onMainItemListener(position);
                }
            });
        }
    }
}
