package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Created by 12421 on 2018/6/15.
 */

public class CityChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<City> cityList;
    private Context context;
    private OnItemClickListener itemClickListener;
    public CityChooseAdapter(List<City> cityList, Context context){
        this.cityList = cityList;
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
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).cityName.setText(cityList.get(position).getCityName());
        if (position == getItemCount()){
            ((ViewHolder)holder).divider.setVisibility(View.GONE);
        }
        ((ViewHolder)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        View divider;
        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.rv_item_tv);
            divider = itemView.findViewById(R.id.rv_item_v);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }
    //声明外部接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
