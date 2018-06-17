package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.R;

import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class DistrictChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<District> districtList;
    private Context context;
    private DistrictChooseAdapter.OnItemClickListener itemClickListener;
    public DistrictChooseAdapter(List<District> districtList, Context context){
        this.districtList = districtList;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_item,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener !=null ){
                    itemClickListener.onItemClick(view,(int)view.getTag());
                }
            }
        });
        DistrictChooseAdapter.ViewHolder viewHolder = new DistrictChooseAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DistrictChooseAdapter.ViewHolder)holder).districtName.setText(districtList.get(position).getDistrictName());
        ((DistrictChooseAdapter.ViewHolder)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return districtList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView districtName;
        public ViewHolder(View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.rv_item_tv);
        }
    }

    public void setOnItemClickListener(DistrictChooseAdapter.OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }
    //声明外部接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
