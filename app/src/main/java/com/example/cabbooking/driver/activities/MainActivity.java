package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.MenuAdp;
import com.example.cabbooking.driver.dto.MenuData;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MapTasks;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.services.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, AdpListner {

    private DrawerLayout drawer;
    private TextView txt1,txt2;
    private NavigationView navigationView;
    private RecyclerView menu_lv1;
    private ValueEventListener listener;
    private Location my_location = null;
    private List<Location> drivers = new ArrayList<>();
    private List<String> driverIds = new ArrayList<>();
    private List<Marker> driverMarkers = new ArrayList<>();
    private List<MenuData> menus = new ArrayList<>();
    private GoogleMap mMap;
    private HashMap<String, String> markerLocation = new HashMap<>();
    private List<LatLng> markers = new ArrayList<>();
    private GPSTracker gps = null;
    String str[] = new String[]{
            android.Manifest.permission.INTERNET
            , android.Manifest.permission.ACCESS_NETWORK_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
    };
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
        ActivityCompat.requestPermissions(MainActivity.this, str,
                1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initUI();
        setMenu();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkStatus();

        ((Switch)findViewById(R.id.search_icon)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                        , UserDto.class);
                final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
                if(isChecked){
                    mDatabaseUser.child(ldata.getUserId()).child(UserConst.status).setValue("1");
                }
                else {
                    mDatabaseUser.child(ldata.getUserId()).child(UserConst.status).setValue("0");
                }
            }
        });
    }

    private void setMenu() {
        try {
            menus = new ArrayList<>();
            menus.add(new MenuData(getString(R.string.home),R.drawable.home_icon));
            menus.add(new MenuData(getString(R.string.vehical_setup),R.drawable.veh_icon));
            menus.add(new MenuData(getString(R.string.ride_history),R.drawable.history_icon));
            menus.add(new MenuData(getString(R.string.scheduled_rides),R.drawable.user_icon));
            menus.add(new MenuData(getString(R.string.settings),R.drawable.settings_icon));
            menus.add(new MenuData(getString(R.string.payout),R.drawable.wallet_icon));
            menus.add(new MenuData(getString(R.string.logout),R.drawable.logout_icon));



            MenuAdp catAdp = new MenuAdp(MainActivity.this,menus);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            menu_lv1.setLayoutManager(layoutManager);
            menu_lv1.setItemAnimator(new DefaultItemAnimator());
            menu_lv1.setNestedScrollingEnabled(false);
            menu_lv1.setAdapter(catAdp);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void checkStatus() {
        try{
            UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                    , UserDto.class);
            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
            mDatabaseUser.child(ldata.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        try {
                            UserDto userDto = dataSnapshot.getValue(UserDto.class);
                            if (userDto.getStatus().equals("1")) {
                                ((Switch) findViewById(R.id.search_icon)).setChecked(true);
                            } else {
                                ((Switch) findViewById(R.id.search_icon)).setChecked(false);
                            }
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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
        startTracking(ldata);


    }

    private void getNearByDrivers(final Location location1) {
        try{
            markers = new ArrayList<>();
            final LatLng myLoc = new LatLng(location1.getLatitude(),location1.getLongitude());

            Marker marker1 = mMap.addMarker(new MarkerOptions().position(myLoc));
            markerLocation.put(location1.getLatitude() + "", "" + location1.getLongitude());
            try{

                marker1.setIcon(MapTasks.loadMarkImg(MainActivity.this,R.drawable.loc_icon));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            markers.add(myLoc);

            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
             listener = new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabaseUser.removeEventListener(listener);
                    if(dataSnapshot.getValue()!=null){
                        drivers = new ArrayList<>();
                        driverIds = new ArrayList<>();
                        driverMarkers = new ArrayList<>();
                        markerLocation = new HashMap<>();

                        mMap.clear();


                        Marker marker1 = mMap.addMarker(new MarkerOptions().position(myLoc));
                        markerLocation.put(location1.getLatitude() + "", "" + location1.getLongitude());
                        try{

                            marker1.setIcon(MapTasks.loadMarkImg(MainActivity.this,R.drawable.loc_icon));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            try {
                                UserDto userDto = dataSnapshot1.getValue(UserDto.class);
                                if(userDto.getStatus().equals("1")) {
                                    JSONObject jk = new JSONObject(userDto.getLocation());
                                    Location location = new Location("");

                                    location.setLatitude(jk.getDouble("mLatitude"));
                                    location.setLongitude(jk.getDouble("mLongitude"));
                                    location.setAltitude(jk.getDouble("mAltitude"));


                                    System.out.println("data----------" + location1.getLatitude() + "-------------" + location1.getLongitude());
                                    if (location1.distanceTo(location) < (5 * 1000)) {
                                        drivers.add(location);
                                        driverIds.add(dataSnapshot1.getKey());

                                        LatLng latLng = MapTasks.coordinateForMarker(location.getLatitude(), location.getLongitude(), 2, markerLocation);


                                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                                        markerLocation.put(location.getLatitude() + "", "" + location.getLongitude());

                                        try {

                                            marker.setIcon(MapTasks.loadMarkImg(MainActivity.this, R.drawable.car_icon));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        driverMarkers.add(marker);
                                        markers.add(latLng);
                                        System.out.println("data----done------" + marker.getId());
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    MapTasks.zoomCamera(mMap, markers);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mDatabaseUser.removeEventListener(listener);
                    MapTasks.zoomCamera(mMap, markers);
                }
            };
            mDatabaseUser.orderByChild(UserConst.user_type).equalTo(Const.driver).addValueEventListener(listener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private void initUI(){

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menu_lv1 = navigationView.getRootView().findViewById(R.id.menu_lv1);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);

    }


    @Override
    protected void onResume() {
        super.onResume();
        try{
            UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                    , UserDto.class);

            txt1.setText(ldata.getFname()+" "+ldata.getLname());
            txt2.setText(ldata.getMobile());
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
            FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
            String token = instanceID.getToken();
            mDatabase.child(ldata.getUserId()).child(Const.token).setValue(token);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(mMap!=null){
                System.out.println("get-----------stt----");
                UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata, "")
                        , UserDto.class);
                startTracking(ldata);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startTracking(UserDto ldata) {
        if(checkIfAlreadyhavePermission()) {
            if (gps == null
                    || my_location==null) {
                try {
                    gps = new GPSTracker(MainActivity.this, ldata);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {
                        getLoca(ldata);

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

    private void getLoca(UserDto ldata) {
        my_location = gps.getLocation();
        if(my_location!=null){


            markerLocation = new HashMap<>();
            markerLocation.put(my_location.getLatitude()+"",""+my_location.getLongitude());

            DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
            mDatabaseUser.child(ldata.getUserId()).child(UserConst.location).setValue(new Gson().toJson(my_location)+"");


            getNearByDrivers(my_location);
        }
        else{
            getLoca(ldata);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
        startTracking(ldata);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(findViewById(R.id.search_rl).getVisibility()==View.GONE) {
                new AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else{
                findViewById(R.id.search_rl).setVisibility(View.GONE);
            }
        }
    }

    public void NavNow(View v){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void SrchNow(View v){
        try{
            findViewById(R.id.search_rl).setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void click1(int position) {

    }


    @Override
    public void click2(int position) {
        switch (menus.get(position).getIcon()){
            case R.drawable.home_icon:

                break;
            case R.drawable.veh_icon:
                startActivity(new Intent(getApplicationContext(),VehSetup.class));
                break;
            case R.drawable.history_icon:
                startActivity(new Intent(getApplicationContext(),MyRides.class));
                break;
            case R.drawable.user_icon:
                startActivity(new Intent(getApplicationContext(),RecentRides.class));
                break;
            case R.drawable.settings_icon:
                startActivity(new Intent(getApplicationContext(),UserSettings.class));
                break;
            case R.drawable.wallet_icon:
                startActivity(new Intent(getApplicationContext(),Payout.class));
                break;
            case R.drawable.logout_icon:
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new MySharedPref().saveData(getApplicationContext(),Const.ldata,"");
                                startActivity(new Intent(getApplicationContext(),SplashScreen.class));
                                finish();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;

            default:
                break;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
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
