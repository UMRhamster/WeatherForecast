package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.R;

import java.util.List;

/**
 * Created by 12421 on 2018/6/18.
 */

public class CitySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Object> objectList;
    private Context context;

    private OnItemClickListener itemClickListener;

    public CitySearchAdapter(List<Object> objectList, Context context){
        this.objectList = objectList;
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
        CitySearchAdapter.ViewHolder viewHolder = new CitySearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (objectList.get(position) instanceof Province){
            ((ViewHolder)holder).cityName.setText(((Province) objectList.get(position)).getProvinceName());
        }else if (objectList.get(position) instanceof City){
            ((ViewHolder)holder).cityName.setText(((City) objectList.get(position)).getCityName());
        }else if(objectList.get(position) instanceof District){
            ((ViewHolder)holder).cityName.setText(((District) objectList.get(position)).getDistrictName());
            ((ViewHolder)holder).nextLevel.setVisibility(View.GONE);
        }
        if (position == getItemCount()){
            ((CitySearchAdapter.ViewHolder)holder).divider.setVisibility(View.GONE);
        }
        ((CitySearchAdapter.ViewHolder)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        ImageView nextLevel;
        View divider;
        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.rv_item_tv);
            divider = itemView.findViewById(R.id.rv_item_v);
            nextLevel = itemView.findViewById(R.id.rv_item_iv);
        }
    }

    public void setOnItemClickListener(CitySearchAdapter.OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }
    //声明外部接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
