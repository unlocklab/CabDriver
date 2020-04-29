package com.example.cabbooking.driver.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.TranDto;
import com.example.cabbooking.driver.dto.TripDto;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.other.ApiCall;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MapConst;
import com.example.cabbooking.driver.other.MapTasks;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.services.BgProcess;
import com.example.cabbooking.driver.services.GPSTracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrackingScreen extends AppCompatActivity implements OnMapReadyCallback {
    private ValueEventListener listener;
    private UserDto rider = null;
    private TripDto tripDto = null;
    private ProgressDialog pd = null;
    private List<LatLng> markers = new ArrayList<>();
    private HashMap<String, String> markerLocation = new HashMap<>();
    private TextView fare_txt,distance_txt,duration_txt,pay_type_txt,txt2,txt1,txt3;
    private String tripId;
    private Location my_location;
    private GoogleMap mMap;
    private GPSTracker gps = null;
    private Polyline my_line = null;
    private int rtype = 0;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    String str[] = new String[]{
            android.Manifest.permission.INTERNET
            , android.Manifest.permission.ACCESS_NETWORK_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void initPD() {
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));
    }

    private boolean checkIfAlreadyhavePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            return false;
        }
    }

    private void requestReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(TrackingScreen.this, str,
                1);
    }
    private void initUI(){

         fare_txt = findViewById(R.id.fare_txt);
         distance_txt = findViewById(R.id.distance_txt);
         duration_txt = findViewById(R.id.duration_txt);
         pay_type_txt = findViewById(R.id.pay_type_txt);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
//        txt3 = findViewById(R.id.txt3);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initPD();
        initUI();

        tripId = getIntent().getExtras().getString(Const.tripId);
        rtype = getIntent().getExtras().getInt(Const.rtype);
        
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void RecivedNow(View v){
        if(tripDto!=null){
            if(tripDto.getStatus().equals(Const.accepted)){
                new AlertDialog.Builder(this)
                        .setTitle("Received Rider")
                        .setMessage("Are you sure?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl);
                                if(rtype==1){
                                    mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                                }
                                mDatabaseUser.child(tripId).child(Const.status).setValue(Const.received);

                                ((TextView)findViewById(R.id.right_icon)).setText(getString(R.string.complete));

                                UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                        , UserDto.class);
                                final DatabaseReference mDatabaseUser1 = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
                                mDatabaseUser1.child(ldata.getUserId()).child(UserConst.status).setValue("0");

                                BgProcess.sendNotification(getApplicationContext(),
                                        rider.getUserId()
                                        ,tripId
                                        ,ldata.getFname()+" "+ldata.getLname()
                                        ,"Has picked up you");


                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(tripDto.getStatus().equals(Const.received)){
                new AlertDialog.Builder(this)
                        .setTitle("Completed Ride")
                        .setMessage("Are you sure?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                stopTimer();
                                DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl);
                                if(rtype==1){
                                    mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                                }
                                mDatabaseUser.child(tripId).child(Const.status).setValue(Const.completed);

                                UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                        , UserDto.class);


                                DatabaseReference mDatabaseUser2 = FirebaseDatabase.getInstance().getReference(Const.tran_tbl);
                                TranDto tranDto = new TranDto();

                                tranDto.setFrom(rider.getFname()+" "+rider.getLname());
                                tranDto.setTo(ldata.getFname()+" "+ldata.getLname());
                                tranDto.setAmount(tripDto.getFare());
                                tranDto.setDatetime(Const.getLocalTime(Const.getUtcTime()));
                                tranDto.setTran_id(mDatabaseUser2.push().getKey());
                                tranDto.setUser_id(ldata.getUserId());

                                mDatabaseUser2.child(tranDto.getTran_id()).setValue(tranDto);

                                tranDto.setTran_id(mDatabaseUser2.push().getKey());
                                tranDto.setUser_id(rider.getUserId());

                                mDatabaseUser2.child(tranDto.getTran_id()).setValue(tranDto);

                                final DatabaseReference mDatabaseUser1 = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
                                mDatabaseUser1.child(ldata.getUserId()).child(UserConst.status).setValue("1");

                                BgProcess.sendNotification(getApplicationContext(),
                                        rider.getUserId()
                                        ,tripId
                                        ,ldata.getFname()+" "+ldata.getLname()
                                        ,"Has completed ride");
                                Toast.makeText(getApplicationContext(),getString(R.string.ride_com_msg),6).show();

                                finish();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                , UserDto.class);
        startTracking(ldata);
    }

    private void stopTimer(){
        try {
            if (mTimer1 != null) {
                mTimer1.cancel();
                mTimer1.purge();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startTimer(){

        stopTimer();
        try {
            mTimer1 = new Timer();
            mTt1 = new TimerTask() {
                public void run() {
                    mTimerHandler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        public void run() {
                            my_location = gps.getLocation();
                            UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata,"")
                                    , UserDto.class);
                            try{

                                DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
                                mDatabaseUser.child(ldata.getUserId()).child(UserConst.location).setValue(new Gson().toJson(my_location)+"");
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            if (tripDto.getStatus().equals(Const.accepted)) {

                                getDirationsCall(my_location.getLatitude() + "," + my_location.getLongitude()
                                        , tripDto.getFrom_lat_lng()
                                        ,ldata.getFname()+" "+ldata.getLname()
                                ,tripDto.getFrom_str());
                            }
                            else if (tripDto.getStatus().equals(Const.received)) {
                                getDirationsCall(my_location.getLatitude() + "," + my_location.getLongitude()
                                        , tripDto.getTo_lat_lng()
                                        ,ldata.getFname()+" "+ldata.getLname()
                                ,tripDto.getTo_str());
                            }
                        }
                    });
                }
            };

            mTimer1.schedule(mTt1, 1, 5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (tripDto != null && tripDto.getStatus().equals(Const.accepted)
                    || tripDto != null && tripDto.getStatus().equals(Const.received)) {
                startTimer();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startTracking(final UserDto ldata) {
        if(checkIfAlreadyhavePermission()) {
            if (gps == null) {
                try {
                    gps = new GPSTracker(TrackingScreen.this, ldata);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                         my_location = gps.getLocation();

                        DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl);
                        if(rtype==1){
                            mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.trip_tbl1);
                        }
                        mDatabaseUser.child(tripId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null){
                                    tripDto = dataSnapshot.getValue(TripDto.class);

                                    if(tripDto.getStatus().equals(Const.completed)){
                                        ((ImageView)findViewById(R.id.right_icon)).setVisibility(View.GONE);
                                    }

                                    txt2.setText(tripDto.getFrom_str().substring(0,20) + "" +
                                            " to " + tripDto.getTo_str().substring(0,20));

//                                    if(rtype==1) {
//                                        txt3.setVisibility(View.VISIBLE);
//                                        txt3.setText(tripDto.getDate_str());
//                                    }
//                                    else{
//                                        txt3.setVisibility(View.GONE);
//                                        txt3.setText("");
//                                    }

                                    fare_txt.setText(tripDto.getFare());
                                    distance_txt.setText(tripDto.getDistance());
                                    duration_txt.setText(tripDto.getDuration());
                                    pay_type_txt.setText(tripDto.getPay_type());
                                    System.out.println("testbk----------error-----");
                                    if (tripDto.getStatus().equals(Const.accepted)) {
                                        getDirationsCall(my_location.getLatitude() + "," + my_location.getLongitude()
                                                , tripDto.getFrom_lat_lng()
                                        ,ldata.getFname()+" "+ldata.getLname()
                                        ,tripDto.getFrom_str());
                                        startTimer();
                                    }
                                    else if (tripDto.getStatus().equals(Const.received)) {
                                        getDirationsCall(my_location.getLatitude() + "," + my_location.getLongitude()
                                                , tripDto.getTo_lat_lng()
                                                ,ldata.getFname()+" "+ldata.getLname()
                                                ,tripDto.getTo_str());
                                        startTimer();
                                    }
                                    else{
                                        getDirationsCall(tripDto.getFrom_lat_lng()
                                                , tripDto.getTo_lat_lng()
                                                ,tripDto.getFrom_str()
                                                ,tripDto.getTo_str());
                                    }

                                    DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
                                    mDatabaseUser.child(tripDto.getRider_id()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue()!=null){
                                                try {
                                                    rider = dataSnapshot.getValue(UserDto.class);
                                                    txt1.setText(rider.getFname() + " " + rider.getLname());
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        
                    } else {

                        gps.showSettingsAlert();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            requestReadPhoneStatePermission();
        }
    }

    private void getDirationsCall(String from ,String to,String fromName,String toName) {
        try {
            mMap.clear();
            markers = new ArrayList<>();
            markerLocation = new HashMap<>();
            String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=" + from +
                        "&destination=" + to +
                        "&key=" + Const.google_api_key;

            LatLng latLng = MapTasks.coordinateForMarker(Double.parseDouble(from.split(",")[0])
                    , Double.parseDouble(from.split(",")[1]), 2, markerLocation);


            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
            markerLocation.put(from.split(",")[0] + "", "" + from.split(",")[1]);
            marker.setTitle(fromName);
            try {

                marker.setIcon(MapTasks.loadMarkImg(TrackingScreen.this, R.drawable.loc_icon));
            } catch (Exception e) {
                e.printStackTrace();
            }

            markers.add(latLng);
             latLng = MapTasks.coordinateForMarker(Double.parseDouble(to.split(",")[0])
                    , Double.parseDouble(to.split(",")[1]), 2, markerLocation);


             marker = mMap.addMarker(new MarkerOptions().position(latLng));
            markerLocation.put(to.split(",")[0] + "", "" + to.split(",")[1]);
            marker.setTitle(toName);
            try {

                marker.setIcon(MapTasks.loadMarkImg(TrackingScreen.this, R.drawable.loc_icon));
            } catch (Exception e) {
                e.printStackTrace();
            }

            markers.add(latLng);

            MapTasks.zoomCamera(mMap,markers);


                ApiCall.makeGET(this, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String mainObj) {
                        try {
                            System.out.println("asdfg---------------" + mainObj);
                            JSONObject jk = new JSONObject(mainObj);
                            if (jk.getString(Const.status).equals("OK")) {
                                JSONArray routes = jk.getJSONArray(MapConst.routes);
                                JSONObject overview_polyline = routes.getJSONObject(0).getJSONObject(MapConst.overview_polyline);
                                String encodedPoly = overview_polyline.getString(MapConst.points);
                                my_line = MapTasks.showPolyOnMap(TrackingScreen.this, encodedPoly, mMap, my_line);

                                JSONObject legs = routes.getJSONObject(0)
                                        .getJSONArray(MapConst.legs)
                                        .getJSONObject(0);



                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("asdfg----------error-----");
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
        startTracking(ldata);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void NavNow(View v){
        finish();
    }
}
