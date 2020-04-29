package com.example.cabbooking.driver.other;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BHUPESH on 5/31/2016.
 */
public class MySharedPref {

    SharedPreferences sp;
    public void saveData(Context context, String key, String value)
    {
        sp = context.getSharedPreferences(Const.appSirSp,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public String getData(Context context, String key, String value)
    {
        sp = context.getSharedPreferences(Const.appSirSp,context.MODE_PRIVATE);
        return sp.getString(key,value);
    }
    public void DeleteData(Context context)
    {
        sp = context.getSharedPreferences(Const.appSirSp,context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public void NullData(Context context , String key)
    {
        sp = context.getSharedPreferences(Const.appSirSp,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,null);
        editor.commit();
    }


}
