package com.example.cabbooking.driver.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.ReviewDto;
import com.example.cabbooking.driver.dto.TripDto;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;


public class RwAdp extends RecyclerView.Adapter<RwAdp.MyViewHolder> {

    private List<ReviewDto> l1;
    private Activity activity;
    private AdpListner adpListner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RatingBar rt1;
        public RelativeLayout rl1;
        public ImageView right_icon;
        public TextView txt1,txt2;
        public MyViewHolder(View view) {
            super(view);

            right_icon = view.findViewById(R.id.right_icon);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            rt1 = view.findViewById(R.id.rt1);
            rl1 = view.findViewById(R.id.rl1);
        }
    }


    public RwAdp(Activity activity, List<ReviewDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        adpListner = (AdpListner) activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rw_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {

            holder.txt2.setText(l1.get(holder.getAdapterPosition()).getFeedback());

            holder.rt1.setNumStars(5);
            holder.rt1.setRating(Float.parseFloat(l1.get(holder.getAdapterPosition()).getRating()));

            holder.right_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.rl1.performClick();
                }
            });

            holder.rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpListner.click1(holder.getAdapterPosition());
                }
            });



            DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
            mDatabaseUser.child(l1.get(holder.getAdapterPosition()).getRider_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        UserDto userDto = dataSnapshot.getValue(UserDto.class);
                        holder.txt1.setText(userDto.getFname()+" "+userDto.getLname());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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