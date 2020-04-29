package com.example.cabbooking.driver.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.CatDto;
import com.example.cabbooking.driver.intr.AdpListner;


import java.util.List;


public class CatAdp extends RecyclerView.Adapter<CatAdp.MyViewHolder> {

    private List<CatDto> l1;
    private Activity activity;
    private AdpListner adpListner;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;


        public MyViewHolder(View view) {
            super(view);
            txt1 = view.findViewById(R.id.txt1);
        }
    }


    public CatAdp(Activity activity, List<CatDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        adpListner = (AdpListner) activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            holder.txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpListner.click1(holder.getAdapterPosition());
                }
            });

            holder.txt1.setText(l1.get(holder.getAdapterPosition()).getCat_name());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return l1.size();
    }

}