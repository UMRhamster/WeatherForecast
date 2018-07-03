package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.DailyWeather;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.Model.WeatherUtils;
import com.whut.umrhamster.weatherforecast.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 12421 on 2018/6/24.
 */

public class WeatherManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private boolean isEdit = false;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ADD = 1;
    private Context context;
    private List<Weather> weatherList;


    private OnItemClickListener itemClickListener;

    public WeatherManagementAdapter(Context context, List<Weather> weatherList){
        this.context = context;
        this.weatherList = weatherList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_grid_item,parent,false);
        WeatherManagementAdapter.ViewHolder holder = new WeatherManagementAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder)holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener !=null ){
                    if (!isEdit){
                        itemClickListener.onItemClick(view,position,0); //0跳转
                    }
                }
            }
        });
        ((ViewHolder)holder).imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position+1 !=getItemCount()){
                    if (itemClickListener !=null ){
                        itemClickListener.onItemClick(view,position,1);  //1删除
                    }
                }
            }
        });
        if (!isEdit){
            ((ViewHolder)holder).imageViewDelete.setVisibility(View.INVISIBLE);
        }else {
            if (position +1 != getItemCount())
            ((ViewHolder)holder).imageViewDelete.setVisibility(View.VISIBLE);
        }
        if (position+1 == getItemCount()){
            ((ViewHolder)holder).textViewCity.setVisibility(View.INVISIBLE);
            ((ViewHolder)holder).textViewHigh.setVisibility(View.INVISIBLE);
            ((ViewHolder)holder).textViewLow.setVisibility(View.INVISIBLE);
            ((ViewHolder)holder).textViewType.setVisibility(View.INVISIBLE);
            ((ViewHolder)holder).imageViewType.setVisibility(View.INVISIBLE);
            ((ViewHolder)holder).viewV.setVisibility(View.VISIBLE);
            ((ViewHolder)holder).viewH.setVisibility(View.VISIBLE);
//            ((ViewHolder)holder).imageViewDelete.setVisibility(View.GONE);
        }else {
            ((ViewHolder)holder).textViewCity.setText(weatherList.get(position).getCity());
            ((ViewHolder)holder).textViewHigh.setText(WeatherUtils.getTemperatureFormated(weatherList.get(position).getForecast().get(1).getHigh()));
            ((ViewHolder)holder).textViewLow.setText(WeatherUtils.getTemperatureFormated(weatherList.get(position).getForecast().get(1).getLow()));
            ((ViewHolder)holder).textViewType.setText(weatherList.get(position).getForecast().get(1).getType());
            ((ViewHolder)holder).imageViewType.setImageResource(WeatherUtils.getImgbyType(weatherList.get(position).getForecast().get(1).getType()));
            ((ViewHolder)holder).imageViewType.setColorFilter(Color.GRAY);  //设置图片为灰色
        }
        ((ViewHolder)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return weatherList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1==getItemCount()){
            return TYPE_ADD;
        }else {
            return TYPE_ITEM;
        }
    }

    public void editStart(){  //进行编辑模式，显示x按钮
        isEdit = true;
        notifyDataSetChanged();
    }

    public void editEnd(){
        isEdit = false;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView imageViewDelete;
        ImageView imageViewType;
        TextView textViewType;
        TextView textViewHigh;
        TextView textViewLow;
        TextView textViewCity;

        View viewH;
        View viewV;
        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rv_grid_item_card_rl);
            imageViewDelete = itemView.findViewById(R.id.rv_grid_item_delete_iv);
            imageViewType = itemView.findViewById(R.id.rv_grid_item_type_iv);
            textViewType = itemView.findViewById(R.id.rv_grid_item_type_tv);
            textViewHigh = itemView.findViewById(R.id.rv_grid_item_high_tv);
            textViewLow = itemView.findViewById(R.id.rv_grid_item_low_tv);
            textViewCity = itemView.findViewById(R.id.rv_grid_item_city_tv);
            viewH = itemView.findViewById(R.id.rv_grid_item_card_h);
            viewV = itemView.findViewById(R.id.rv_grid_item_card_v);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }
    //声明外部接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position, int type); //0-跳转，1-删除
    }
}
