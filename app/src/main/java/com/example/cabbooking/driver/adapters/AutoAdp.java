package com.example.cabbooking.driver.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.cabbooking.driver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AutoAdp extends BaseAdapter {

    private Activity activity;
    private JSONArray l1;
    private LayoutInflater inflater;

    public AutoAdp(Activity activity, JSONArray l1) {
        this.activity = activity;
        this.l1 = l1;
        inflater = LayoutInflater.from(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return l1.length();
    }

    @Override
    public JSONObject getItem(int i) {
        try {
            return l1.getJSONObject(i);
        } catch (JSONException e) {

            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spin_item, viewGroup, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView code = (TextView) view.findViewById(R.id.code);
        try {
            String str = l1.getJSONObject(i).getString("name").trim();
            if (str.length() > 22) {
                name.setText(str.substring(0, 22));
            } else {
                name.setText(str);
            }
            code.setText(l1.getJSONObject(i).getString("dial_code"));
        } catch (JSONException e) {

        }


        return view;
    }
}