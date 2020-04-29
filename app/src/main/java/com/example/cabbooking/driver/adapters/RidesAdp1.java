package com.example.cabbooking.driver.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.TripDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;


import java.util.List;


public class RidesAdp1 extends RecyclerView.Adapter<RidesAdp1.MyViewHolder> {

    private List<TripDto> l1;
    private Activity activity;
    private AdpListner adpListner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl1,accept_bt,reject_bt;
        public TextView txt1,txt2,txt3;
        public MyViewHolder(View view) {
            super(view);
            rl1 = view.findViewById(R.id.rl1);
            accept_bt = view.findViewById(R.id.accept_bt);
            reject_bt = view.findViewById(R.id.reject_bt);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            txt3 = view.findViewById(R.id.txt3);
        }
    }


    public RidesAdp1(Activity activity, List<TripDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        adpListner = (AdpListner) activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_item1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            holder.txt1.setText(l1.get(holder.getAdapterPosition()).getFrom_str().substring(0,20));
            holder.txt2.setText(l1.get(holder.getAdapterPosition()).getTo_str().substring(0,20));
            holder.txt3.setText(Const.getLocalTime(l1.get(holder.getAdapterPosition()).getDatetime()));

            if(l1.get(holder.getAdapterPosition()).getStatus().equals(Const.pending)==false){
                holder.accept_bt.setVisibility(View.GONE);

                holder.reject_bt.setVisibility(View.GONE);
            }
            else{
                holder.accept_bt.setVisibility(View.VISIBLE);
                holder.reject_bt.setVisibility(View.VISIBLE);
            }

            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpListner.click1(holder.getAdapterPosition());
                }
            });
            holder.accept_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpListner.click2(holder.getAdapterPosition());
                }
            });
            holder.reject_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpListner.click3(holder.getAdapterPosition());
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