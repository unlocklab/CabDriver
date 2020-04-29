package com.example.cabbooking.driver.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.SettAdp;
import com.example.cabbooking.driver.dto.CommDto;
import com.example.cabbooking.driver.dto.SetViewDto;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
/*
 * Settings screen
 * */

public class UserSettings extends AppCompatActivity implements AdpListner {

    String str1[] = new String[]{
            Manifest.permission.CAMERA
    };

    private void requestReadPhoneStatePermission1() {

        Toast.makeText(getApplicationContext(),"Please Accept Camera Permission",6).show();
        ActivityCompat.requestPermissions(UserSettings.this, str1,
                2);
    }

    private boolean checkIfAlreadyhavePermission1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * init Ui in class
     *
     * */
    private RecyclerView lv1;
    private List<SetViewDto> l1 = new ArrayList<>();
    private ProgressDialog pd;
    private CommDto commDto = null;
    private UserDto userDto = null;

    /*
     * this method connects Ui xml file with java file
     * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        userDto = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
        initUI();
    }

    /*
     * Ui init with settings opt cofiguration
     * and adapter configuration
     * */
    private void initUI() {

        lv1 = findViewById(R.id.lv1);


        initPD();
        loadData1();
        loadData();
    }

    private void loadData1() {
        try{
            final DatabaseReference mDatabaseUser1 = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

            mDatabaseUser1.child(userDto.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        userDto = dataSnapshot.getValue(UserDto.class);
                        loadData();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.commision_tbl);
            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        commDto = dataSnapshot.getValue(CommDto.class);
                        loadData();
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


    private void loadData() {
        try {

            /*
             * Creating option list
             * */
            l1 = new ArrayList<>();
            l1.add(new SetViewDto(1, getString(R.string.vehical_setup), ""));

            if(commDto!=null){
                l1.add(new SetViewDto(3, getString(R.string.partner_fee), commDto.getCom_per_trip()+" %"));

            }
            else {
                l1.add(new SetViewDto(3, getString(R.string.partner_fee), "0%"));
            }

            l1.add(new SetViewDto(3, getString(R.string.milage), ""+userDto.getMilage()));
            l1.add(new SetViewDto(3, getString(R.string.balance), "$"+userDto.getBalance()));
            l1.add(new SetViewDto(3, getString(R.string.payment_type), ""+userDto.getPayment_type()));

            l1.add(new SetViewDto(1, getString(R.string.comfort_level),""));
            l1.add(new SetViewDto(2, getString(R.string.start1),""+userDto.isStart()));
            l1.add(new SetViewDto(2, getString(R.string.economy),""+userDto.isEconomy()));
            l1.add(new SetViewDto(2, getString(R.string.comfort),""+userDto.isComfort()));

            l1.add(new SetViewDto(1, getString(R.string.user_settings),""));
            l1.add(new SetViewDto(0, getString(R.string.profile),""));
            l1.add(new SetViewDto(0, getString(R.string.reviews),""));
            l1.add(new SetViewDto(0, getString(R.string.change_password),""));


            l1.add(new SetViewDto(1, getString(R.string.other),""));
            l1.add(new SetViewDto(0, getString(R.string.support_request),""));
            l1.add(new SetViewDto(0, getString(R.string.privacy_policy),""));
            l1.add(new SetViewDto(0, getString(R.string.terms),""));
            l1.add(new SetViewDto(0, getString(R.string.faq),""));
            l1.add(new SetViewDto(0, getString(R.string.about),""));


            /*
             * connecting adapter with list
             * */
            SettAdp sa = new SettAdp(this, l1);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            lv1.setLayoutManager(mLayoutManager);
            lv1.setItemAnimator(new DefaultItemAnimator());
            lv1.setNestedScrollingEnabled(false);
            lv1.setAdapter(sa);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(checkIfAlreadyhavePermission1()){

        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Camera")
                    .setMessage("Please allow camera permission for opening scanner")

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                          requestReadPhoneStatePermission1();
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /*
     * init progress dialog for future functionalities
     * */
    private void initPD() {
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));
    }

    /*
     * back icon click listner
     * */
    public void NavNow(View v) {
        finish();
    }

    /*
     * back button click listner
     * */
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void click1(int position) {
        if(l1.get(position).getValue().equals(getString(R.string.profile))){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        else if(l1.get(position).getValue().equals(getString(R.string.change_password))){
            startActivity(new Intent(getApplicationContext(),ChangePassword.class));
        }
        else if(l1.get(position).getValue().equals(getString(R.string.location))){
//                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        else if(l1.get(position).getValue().equals(getString(R.string.wallet))){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        else if(l1.get(position).getValue().equals(getString(R.string.reviews))){
            startActivity(new Intent(getApplicationContext(),ReviewsScreen.class));
        }
        else if(l1.get(position).getValue().equals(getString(R.string.support_request))){
            startActivity(new Intent(getApplicationContext(),RequestSupport.class));
        }
        else {
            Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
            intent.putExtra(Const.data,l1.get(position).getValue());
            startActivity(intent);
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
