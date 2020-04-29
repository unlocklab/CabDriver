package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.RidesAdp;
import com.example.cabbooking.driver.adapters.RidesAdp1;
import com.example.cabbooking.driver.adapters.RwAdp;
import com.example.cabbooking.driver.dto.ReviewDto;
import com.example.cabbooking.driver.dto.TripDto;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.services.BgProcess;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewsScreen extends AppCompatActivity implements AdpListner {

    private RecyclerView lv1;
    private List<ReviewDto> l1 = new ArrayList<>();

    private UserDto ldata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        initUI();
        ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);

        loadData();
    }
    private void loadData() {
        try{
            DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.review_tbl);
            mDatabaseUser.orderByChild(Const.driver_id).equalTo(ldata.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        l1 = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            ReviewDto tripDto = dataSnapshot1.getValue(ReviewDto.class);
                            l1.add(tripDto);

                        }

                        addAdp();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUI() {

        lv1  = findViewById(R.id.lv1);

    }

    private void addAdp() {
        Collections.reverse(l1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lv1.setLayoutManager(mLayoutManager);
        lv1.setItemAnimator(new DefaultItemAnimator());
        lv1.setNestedScrollingEnabled(false);
        RwAdp mainAdp = new RwAdp(this, l1);
        lv1.setAdapter(mainAdp);
    }


    public void LoginNow(View v) {
        finish();
    }


    public void NavNow(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void click1(int position) {
        try{
            Intent intent = new Intent(getApplicationContext(), TrackingScreen.class);
            intent.putExtra(Const.tripId, l1.get(position).getTrip_id());
            intent.putExtra(Const.rtype,Integer.parseInt(l1.get(position).getTrip_type()));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void click2(int position) {

    }



    @Override
    public void click3(int position) {

    }

    @Override
    public void click4(int position) {

    }

    @Override
    public void click5(int position) {

    }

    @Override
    public void click6(int position) {

    }

    @Override
    public void click7(int position) {

    }

    @Override
    public void click8(int position) {

    }

    @Override
    public void click9(int position) {

    }

    @Override
    public void click10(int position) {

    }
}
