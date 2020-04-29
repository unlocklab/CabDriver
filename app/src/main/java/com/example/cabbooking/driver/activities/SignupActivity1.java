package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.CatAdp;
import com.example.cabbooking.driver.adapters.RidesAdp;
import com.example.cabbooking.driver.dto.CatDto;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.other.Validations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity1 extends AppCompatActivity implements AdpListner {

    private EditText et1,et3;
    private TextView et2;
    private ProgressDialog pd;
    private List<CatDto> l1 = new ArrayList<>();
    private Dialog dialog1 = null;
    private ValueEventListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup1);
        initPD();
        initUI();
        loadCats();
    }

    public void ChNow(View v){
        try{
            dialog1 = new Dialog(SignupActivity1.this);
            dialog1.setCancelable(true);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.list_pop);

            RecyclerView lv1 = dialog1.findViewById(R.id.lv1);

            CatAdp catAdp = new CatAdp(SignupActivity1.this,l1);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            lv1.setLayoutManager(mLayoutManager);
            lv1.setItemAnimator(new DefaultItemAnimator());
            lv1.setNestedScrollingEnabled(false);

            lv1.setAdapter(catAdp);

            dialog1.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadCats() {
        try{
            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.cat_tbl);

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        l1 = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            l1.add(dataSnapshot1.getValue(CatDto.class));
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

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
    }


    private void initPD() {
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));
    }


    public void LoginNow(View v){
        if (Validations.valEdit(this, et1, getString(R.string.veh_number))
                && Validations.valEdit2(this, et2, getString(R.string.car_category))
                && Validations.valEdit(this, et3, getString(R.string.licence_no))
                && Const.isOnline(this)) {
            try {
                pd.show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.rl_main).getWindowToken(), 0);
                final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

                JSONObject jk = new JSONObject(new MySharedPref().getData(getApplicationContext(),Const.signup_data,""));

                UserDto userDto = new UserDto();


                userDto.setUserId(mDatabaseUser.push().getKey());
                userDto.setFname(jk.getString(UserConst.fname));
                userDto.setLname(jk.getString(UserConst.lname));
                userDto.setEmail(jk.getString(UserConst.email));
                userDto.setMobile(jk.getString(UserConst.mobile));
                userDto.setPassword(jk.getString(UserConst.password));
                userDto.setDatetime(Const.getUtcTime());
                userDto.setUser_type(Const.driver);
                userDto.setVeh_no(et1.getText().toString());
                userDto.setVeh_cat(et2.getText().toString());
                userDto.setLic_no(et3.getText().toString());
                userDto.setStatus("1");

                System.out.println("dfg-----------------"+jk.getString(UserConst.email));

                userDto.setMilage("0");
                userDto.setBalance("0");
                userDto.setPayment_type("Cash & Card");
                userDto.setStart(false);
                userDto.setEconomy(false);
                userDto.setComfort(false);


                checkKeyValue(UserConst.email,userDto.getEmail(),userDto);

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void checkKeyValue(final String key, final String value, final UserDto userDto) {
        try{
            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabaseUser.removeEventListener(listener);
                    if(dataSnapshot.getValue()!=null){
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            UserDto userDto1 = dataSnapshot1.getValue(UserDto.class);
                            if(key.equals(UserConst.email)){
                                if(userDto1.getEmail().equals(value)){
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),getString(R.string.email_exist),6).show();
                                }
                                else{
                                    checkKeyValue(UserConst.mobile,userDto.getMobile(),userDto);
                                }
                            }
                            else{
                                if(userDto1.getMobile().equals(value)){
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),getString(R.string.mob_exist),6).show();
                                }
                                else{
                                    CreateUser(userDto);
                                }
                            }
                        }
                    }
                    else{
                        if(key.equals(UserConst.email)){
                            checkKeyValue(UserConst.mobile,userDto.getMobile(),userDto);
                        }
                        else{
                            CreateUser(userDto);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mDatabaseUser.removeEventListener(listener);
                    if(key.equals(UserConst.email)){
                        checkKeyValue(UserConst.mobile,userDto.getMobile(),userDto);
                    }
                    else{
                        CreateUser(userDto);
                    }
                }
            };

            mDatabaseUser.orderByChild(key).equalTo(value).addValueEventListener(listener);
        }
        catch (Exception e){
            e.printStackTrace();
            pd.dismiss();
        }
    }

    private void CreateUser(UserDto userDto) {
        pd.dismiss();

        final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

        Toast.makeText(getApplicationContext(),getString(R.string.signup_succ),6).show();
        new MySharedPref().saveData(getApplicationContext(),Const.ldata, new Gson().toJson(userDto)+"");

        System.out.println("tiger--------------"+new Gson().toJson(userDto));
        mDatabaseUser.child(userDto.getUserId()).setValue(userDto);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void click1(int position) {
        try {
            et2.setText(l1.get(position).getCat_name());
            if(dialog1!=null) {
                dialog1.dismiss();
            }
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
