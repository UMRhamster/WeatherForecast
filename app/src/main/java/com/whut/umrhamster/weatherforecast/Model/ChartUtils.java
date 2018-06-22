package com.whut.umrhamster.weatherforecast.Model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.NumberPicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.whut.umrhamster.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/18.
 */

public class ChartUtils {
    public static LineChart initChart(LineChart chart, Weather weather) {
        /*图表设置*/
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色->是否显示网格线
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        //不可以拖拽
        chart.setDragEnabled(false);
        //不可以触摸操作
        chart.setTouchEnabled(false);
        // 不显示y轴右边的值
//        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
//        chart.setExtraLeftOffset(-15);


        /*XY轴设置*/
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);  // 不显示x轴
        xAxis.setDrawGridLines(true);   //绘制x轴上的竖线
        xAxis.setGridColor(Color.GRAY); //设置竖线颜色
        xAxis.setDrawLabels(false);   //是否显示坐标值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5);  //设置标签数，貌似有点问题，可能不算“0”标签
        xAxis.setAxisMaximum(6);
        xAxis.setAxisMinimum(0);


        YAxis yAxisLeft = chart.getAxisLeft();
        // 不显示y轴
        yAxisLeft.setDrawAxisLine(false);
        // 设置y轴数据的位置
//        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxisLeft.setDrawGridLines(false);
//        yAxis.setTextColor(Color.WHITE);
//        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
//        yAxis.setXOffset(30);
//        yAxis.setYOffset(-3);
//        yAxis.setAxisMinimum(0);
//        yAxisLeft.setAxisMaximum(getMaxTemperature(weather)); //y坐标最大值
//        yAxisLeft.setAxisMinimum(getMinTemperature(weather)); //y坐标最小值
        yAxisLeft.setStartAtZero(false);
        yAxisLeft.setDrawZeroLine(false);
        yAxisLeft.setDrawLabels(false);



        YAxis yAxisRight = chart.getAxisRight();
        //不显示y轴
        yAxisRight.setDrawAxisLine(false);
        //不从y轴发出横向直线
        yAxisRight.setDrawGridLines(false);
//        yAxisRight.setAxisMaximum(getMaxTemperature(weather));
//        yAxisRight.setAxisMinimum(getMinTemperature(weather));
        yAxisRight.setDrawZeroLine(false);
        yAxisRight.setStartAtZero(false);
        yAxisRight.setDrawLabels(false);

        chart.invalidate();
        return chart;
    }

//    public static void setData(LineChart lineChart, Weather weather){
//        ArrayList<Entry> yHigh = new ArrayList<>(6);
//        LineDataSet high = new LineDataSet(yHigh,"高温"); //
//        setHighTemperature(high,yHigh,weather);
//
//        ArrayList<Entry> yLow = new ArrayList<>(6);
//        LineDataSet low = new LineDataSet(yLow,"低温"); //
//        setLowTemperature(low,yLow,weather);
//
//        ArrayList<LineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(high);
//        dataSets.add(low);
//
////        LineData data = new LineData(dataSets);
//
////        lineChart.setData(data);
//
//    }
//
//    public static void setHighTemperature(LineDataSet high, ArrayList<Entry> yVals, Weather weather){
//        yVals.add(new Entry(WeatherUtils.getTemperatureFloat(weather.getYesterday().getHigh()),0));
//        for (int i=1;i<6;i++){
//            yVals.add(new Entry(WeatherUtils.getTemperatureFloat(weather.getForecast().get(i-1).getHigh()),i));
//        }
//
//        //以左边y轴为标准
//        high.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//        high.setLineWidth(2f);
//        high.setColor(R.color.redPopular);
//        high.setCircleRadius(4f);
//        high.setDrawCircleHole(false);
//        high.setCircleColor(R.color.redPopular);
////        high.setCircleColorHole(R.color.redPopular);
////        high.setDrawCircleHole(true);
//    }
//
//    public static void setLowTemperature(LineDataSet low, ArrayList<Entry> yVals, Weather weather){
//        yVals.add(new Entry(WeatherUtils.getTemperatureFloat(weather.getYesterday().getLow()),0));
//        for (int i=1;i<6;i++){
//            yVals.add(new Entry(WeatherUtils.getTemperatureFloat(weather.getForecast().get(i-1).getLow()),i));
//        }
//
//        //以右边y轴为标准
//        low.setAxisDependency(YAxis.AxisDependency.RIGHT);
//
//        low.setLineWidth(2f);
//        low.setColor(R.color.bluePopular);
//        low.setCircleRadius(4f);
//        low.setDrawCircleHole(false);
//        low.setCircleColor(R.color.bluePopular);
//    }

    public static int getMaxTemperature(Weather weather){
        int max = WeatherUtils.getTemperatureInt(weather.getYesterday().getHigh());
        for (int i =0;i<5;i++){
            int temp = WeatherUtils.getTemperatureInt(weather.getForecast().get(i).getHigh());
            if (max < temp){
                max = temp;
            }
        }
        return max;
    }

    public static int getMinTemperature(Weather weather){
        int min = WeatherUtils.getTemperatureInt(weather.getYesterday().getLow());
        for (int i=0;i<5;i++){
            int temp =WeatherUtils.getTemperatureInt(weather.getForecast().get(i).getLow());
            if (min > temp){
                min = temp;
            }
        }
        return min;
    }

    //一下为自己的折线图处理方法
    public static void setChartData(Context context, LineChart lineChart, Weather weather){
        ArrayList<Entry> highData = new ArrayList<>();
        ArrayList<Entry> lowData = new ArrayList<>();

        highData.add(new Entry(0.5f,WeatherUtils.getTemperatureFloat(weather.getYesterday().getHigh())));
        lowData.add(new Entry(0.5f,WeatherUtils.getTemperatureFloat(weather.getYesterday().getLow())));
        for (int i=1;i<6;i++){
            highData.add(new Entry(i+0.5f,WeatherUtils.getTemperatureFloat(weather.getForecast().get(i-1).getHigh())));
            lowData.add(new Entry(i+0.5f,WeatherUtils.getTemperatureFloat(weather.getForecast().get(i-1).getLow())));
        }

        //每一个LineDataSet对应一条连接线
        LineDataSet highSet;
        LineDataSet lowSet;

        //先判断图中原来是否有数据
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() >0){
            Log.d("ChartUtils","hasData");
            highSet = (LineDataSet) lineChart.getLineData().getDataSetByIndex(0);
            highSet.setValues(highData);
            lowSet = (LineDataSet) lineChart.getLineData().getDataSetByIndex(1);
            lowSet.setValues(lowData);
            //刷新数据
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        }else {
            //设置高温数据
            highSet = new LineDataSet(highData,"");
            highSet.setLineWidth(1f);
            highSet.setColor(context.getResources().getColor(R.color.redPopular,null));
            highSet.setCircleRadius(4f);
            highSet.setCircleColor(context.getResources().getColor(R.color.redPopular,null));
            highSet.setDrawCircleHole(false);
            highSet.setHighlightEnabled(false);
            highSet.setValueTextSize(14f);  //设置文字大小
            highSet.setDrawFilled(false);
            List<Integer> colorsHigh = new ArrayList<>();
            colorsHigh.add(Color.GRAY);
            for (int i=0;i<5;i++){
                colorsHigh.add(Color.WHITE);
            }
            highSet.setValueTextColors(colorsHigh);
            highSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (dataSetIndex == 0){}
                    return (int)value+"°";
                }
            });
            //设置低温数据
            lowSet = new LineDataSet(lowData,"");
            lowSet.setLineWidth(1f);
            lowSet.setColor(context.getResources().getColor(R.color.bluePopular,null));
            lowSet.setCircleRadius(4f);
            lowSet.setCircleColor(context.getResources().getColor(R.color.bluePopular,null));
            lowSet.setDrawCircleHole(false);
            lowSet.setValueTextSize(14f);  //设置文字大小
            lowSet.setDrawFilled(false);
            List<Integer> colorsLow = new ArrayList<>();
            colorsLow.add(Color.GRAY);
            for (int i=0;i<5;i++){
                colorsLow.add(Color.WHITE);
            }
            lowSet.setValueTextColors(colorsLow);
            lowSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return (int)value+"°";
                }
            });

            //保存LineDataSet集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(highSet); // add the datasets
            dataSets.add(lowSet);
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            lineChart.setData(data);
            //绘制图表
            lineChart.invalidate();
        }
    }
}
