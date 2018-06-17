package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.R;

import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class ProvinceChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Province> provinceList;
    private Context context;
    private ProvinceChooseAdapter.OnItemClickListener itemClickListener;
    public ProvinceChooseAdapter(List<Province> provinceList, Context context){
        this.provinceList = provinceList;
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
        ProvinceChooseAdapter.ViewHolder viewHolder = new ProvinceChooseAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ProvinceChooseAdapter.ViewHolder)holder).provinceName.setText(provinceList.get(position).getProvinceName());
        ((ProvinceChooseAdapter.ViewHolder)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView provinceName;
        public ViewHolder(View itemView) {
            super(itemView);
            provinceName = itemView.findViewById(R.id.rv_item_tv);
        }
    }

    public void setOnItemClickListener(ProvinceChooseAdapter.OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }
    //声明外部接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
