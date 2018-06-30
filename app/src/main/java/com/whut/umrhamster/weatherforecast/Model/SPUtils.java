package com.whut.umrhamster.weatherforecast.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by 12421 on 2018/6/25.
 */

public class SPUtils {
    public static void saveWeather(Context context,String cityName, String strObject){
        SharedPreferences sp = context.getSharedPreferences("weathers", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(cityName, strObject);
        edit.commit();
    }
    public static String getWeather(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(name, "");
    }
//    public static Map<String,Weather> getAllWeather(Context context, String name){
//        SharedPreferences sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
//        sp.getAll()
//    }
    //序列化
    public static String serialize(Weather weather){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            objectOutputStream.writeObject(weather);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String serStr = null;
        try {
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serStr;
    }
    //反序列化
    public static Object deSerialization(String str)  {
        String redStr = null;
        try {
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object person = null;
        try {
            person = objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }
}
