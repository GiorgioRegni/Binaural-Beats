package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by JANHAVI on 3/15/2017.
 */

public class SharedPref {

    SharedPreferences sd;
    SharedPreferences.Editor ed;
    Context mContext;

    private static SharedPref ourInstance = new SharedPref();

    public static SharedPref getInstance()
    {
        return  ourInstance;
    }

    public void initialize(Context context)
    {
        mContext = context;
        sd = mContext.getSharedPreferences("BinauralBeatsSharedPref", Context.MODE_PRIVATE);
        ed = sd.edit();
    }

    public void putData(String key, String value)
    {
       // if(value instanceof String){}
        ed.putString(key,value);
        ed.commit();
    }
    public String getString(String key)
    {
        String sharedData = sd.getString(key,"");
        return sharedData;
    }


    public void putList(String key,ArrayList<String> list){

        Set<String> set = new LinkedHashSet<>();

        for(int i=0;i<list.size();i++){
            set.add(list.get(i));
        }
        ed.putStringSet(key,set);
        ed.commit();

    }
    public ArrayList<String> getList(String key){

        ArrayList<String> list = new ArrayList<>();
        Set<String> setList = new LinkedHashSet<>();

        setList = sd.getStringSet(key, null);
        if(setList!=null) {
            list.addAll(setList);
        }
        return list;
    }

    public void putBoolean(String key,boolean tag)
    {
        ed.putBoolean(key,tag);
        ed.commit();
    }

    public boolean getBoolean(String key)
    {
        boolean bValue = sd.getBoolean(key,false);
        return bValue;
    }

    public void putInteger(String key,int num)
    {
        ed.putInt(key,num);
        ed.commit();
    }

    public int getInteger(String key)
    {
        int numValue = sd.getInt(key,0);
        return numValue;
    }

    public void putLongValue(String key,long value)
    {
        ed.putLong(key,value);
        ed.commit();
    }

    public long getLongValue(String key)
    {
        long numValue = sd.getLong(key,0);
        return numValue;
    }
}
