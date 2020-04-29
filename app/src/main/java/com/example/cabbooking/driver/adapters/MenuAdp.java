package com.example.cabbooking.driver.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.MenuData;
import com.example.cabbooking.driver.intr.AdpListner;


import java.util.List;

/*
 * adapter for home categories grid
 * with image loading
 * */
public class MenuAdp extends RecyclerView.Adapter<MenuAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private List<MenuData> l1;
    private Activity activity;
    private AdpListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;
        public RelativeLayout rl1;
        private ImageView imv;

        public MyViewHolder(View view) {
            super(view);
            txt1 = view.findViewById(R.id.txt1);
            rl1 = view.findViewById(R.id.rl1);
            imv = view.findViewById(R.id.imv);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public MenuAdp(Activity activity, List<MenuData> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (AdpListner) activity;
    }

    public MenuAdp(Activity activity, Fragment fragment, List<MenuData> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (AdpListner) fragment;
    }
    /*
     * connecting layout xml file with java file
     * connecting item layout with adapter
     * */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);

        return new MyViewHolder(itemView);
    }
    /*
     *
     * adding click listner to view
     * and setting values from list to view
     * */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {

            MenuData menuData = l1.get(holder.getAdapterPosition());

            holder.txt1.setText(menuData.getTitle());
            holder.imv.setImageResource(menuData.getIcon());

            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.click2(holder.getAdapterPosition());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return l1.size();
    }

}