package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.R;

import java.util.List;

/**
 * Created by 12421 on 2018/6/15.
 */

public class CityChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<String> cityList;
    private Context context;
    public CityChooseAdapter(List<String> cityList, Context context){
        this.cityList = cityList;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).cityName.setText(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.rv_item_tv);
        }
    }
}
