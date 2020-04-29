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
import com.example.cabbooking.driver.dto.TripDto;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.services.BgProcess;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentRides extends AppCompatActivity implements AdpListner {

    private RecyclerView lv1;
    private List<TripDto> l1 = new ArrayList<>();
    private BottomSheetDialog dialog1 = null;
    private String tripId = "";
    private UserDto userDto = null,ldata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        initUI();
        ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
        try{
            tripId = getIntent().getExtras().getString(Const.tripId);
        }
        catch (Exception e){
            e.printStackTrace();
            tripId = "";
        }
        loadData();
    }
    private void loadData() {
        try{
            DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
            mDatabaseUser.orderByChild(Const.driver_id).equalTo(ldata.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        l1 = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            TripDto tripDto = dataSnapshot1.getValue(TripDto.class);
                            l1.add(tripDto);
                            if(tripDto.getTrip_id().equals(tripId)){
                                OtpPop(tripDto);
                            }
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
        RidesAdp1 mainAdp = new RidesAdp1(this, l1);
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
        OtpPop(l1.get(position));
    }

    private void OtpPop(final TripDto tripDto){
        if(dialog1!=null && dialog1.isShowing()){
            dialog1.dismiss();
        }


        dialog1 = new BottomSheetDialog(RecentRides.this);
        dialog1.setCancelable(true);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.ride_est_pop);

        ImageView right_icon = dialog1.findViewById(R.id.right_icon);

        final TextView txt1 = dialog1.findViewById(R.id.txt1);
        TextView txt2 = dialog1.findViewById(R.id.txt2);
        TextView txt21 = dialog1.findViewById(R.id.txt21);

        final TextView fare_txt = dialog1.findViewById(R.id.fare_txt);
        final TextView distance_txt = dialog1.findViewById(R.id.distance_txt);
        final TextView duration_txt = dialog1.findViewById(R.id.duration_txt);
        final TextView pay_type_txt = dialog1.findViewById(R.id.pay_type_txt);
        RatingBar rt1 = dialog1.findViewById(R.id.rt1);

        txt2.setText(tripDto.getFrom_str());

        txt21.setText(tripDto.getTo_str());

        fare_txt.setText(tripDto.getFare());
        distance_txt.setText(tripDto.getDistance());
        duration_txt.setText(tripDto.getDuration());
        pay_type_txt.setText(tripDto.getPay_type());

        DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
        mDatabaseUser.child(tripDto.getRider_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    userDto = dataSnapshot.getValue(UserDto.class);
                    txt1.setText(userDto.getFname()+" "+userDto.getLname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripDto.getStatus().equals(Const.accepted)
                        || tripDto.getStatus().equals(Const.received)) {
                    Intent intent = new Intent(getApplicationContext(), TrackingScreen.class);
                    intent.putExtra(Const.tripId, tripDto.getTrip_id());
                    intent.putExtra(Const.rtype,1);
                    startActivity(intent);
                }
                else  if(tripDto.getStatus().equals(Const.pending)) {
                    new AlertDialog.Builder(RecentRides.this)
                            .setTitle("Accept Ride")
                            .setMessage("Are you sure?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                                    mDatabaseUser.child(tripId).child(Const.status).setValue(Const.accepted);
                                    if(userDto!=null) {
                                        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                                , UserDto.class);


                                        BgProcess.sendNotification(getApplicationContext(),
                                                userDto.getUserId()
                                                , tripId
                                                , ldata.getFname() + " " + ldata.getLname()
                                                , "Has Accepted Ride");
                                    }

                                    Intent intent = new Intent(getApplicationContext(), TrackingScreen.class);
                                    intent.putExtra(Const.tripId, tripDto.getTrip_id());
                                    intent.putExtra(Const.rtype,1);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })

                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                                    mDatabaseUser.child(tripId).child(Const.status).setValue(Const.canceled);

                                    if(userDto!=null) {
                                        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                                , UserDto.class);


                                        BgProcess.sendNotification(getApplicationContext(),
                                                userDto.getUserId()
                                                , tripId
                                                , ldata.getFname() + " " + ldata.getLname()
                                                , "Has Canceled Ride");
                                    }

                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        dialog1.show();
    }

    @Override
    public void click2(int position) {
        updateSt(l1.get(position).getTrip_id(),Const.accepted);
    }

    private void updateSt(final String trip_id, final String accepted) {
        String str = "Cancel";
        if(accepted.equals(Const.accepted)){
            str = "Accept";
        }
        new AlertDialog.Builder(this)
                .setTitle(str)
                .setMessage("Are you sure?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                        mDatabaseUser.child(trip_id).child(Const.status).setValue(accepted);

                        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                , UserDto.class);

                        if(userDto!=null) {
                            BgProcess.sendNotification(getApplicationContext(),
                                    userDto.getUserId()
                                    , tripId
                                    , ldata.getFname() + " " + ldata.getLname()
                                    , "Has Accepted Ride");
                        }

                        if(accepted.equals(Const.accepted)){
                            Intent intent = new Intent(getApplicationContext(),TrackingScreen.class);
                            intent.putExtra(Const.tripId,trip_id);
                            intent.putExtra(Const.rtype,1);
                            startActivity(intent);
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void click3(int position) {
        updateSt(l1.get(position).getTrip_id(),Const.canceled);
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
