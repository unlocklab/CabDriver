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
import com.example.cabbooking.driver.dto.SetViewDto;
import com.example.cabbooking.driver.intr.AdpListner;



import org.json.JSONArray;

import java.util.List;
/*
 * adapter for home categories grid
 * with image loading
 * */
public class SettAdp extends RecyclerView.Adapter<SettAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private List<SetViewDto> l1;
    private Activity activity;
    private AdpListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1,txt2;
        public RelativeLayout rl1,card1,card2;

        public MyViewHolder(View view) {
            super(view);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            rl1 = view.findViewById(R.id.rl1);
            card1 = view.findViewById(R.id.card1);
            card2 = view.findViewById(R.id.card2);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public SettAdp(Activity activity, List<SetViewDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (AdpListner) activity;
    }

    public SettAdp(Activity activity, Fragment fragment, List<SetViewDto> l1) {
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
                .inflate(R.layout.set_item, parent, false);

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

            SetViewDto menuData = l1.get(holder.getAdapterPosition());

            if(menuData.getRtype()==1){
                holder.card2.setVisibility(View.VISIBLE);
                holder.card1.setVisibility(View.GONE);
                holder.txt2.setText(menuData.getValue());
            }
            else{
                holder.card2.setVisibility(View.GONE);
                holder.card1.setVisibility(View.VISIBLE);
                holder.txt1.setText(menuData.getValue());
            }


            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.click1(holder.getAdapterPosition());
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