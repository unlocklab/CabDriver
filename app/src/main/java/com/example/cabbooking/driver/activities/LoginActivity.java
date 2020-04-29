package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.other.Validations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private EditText et1,et2;
    private ProgressDialog pd;
    private ValueEventListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initPD();
        initUI();
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
    }


    private void initPD() {
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));
    }

    public void ForNow(View v){
        startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
    }
    public void LoginNow(View v){
        if (Validations.valEditEmail(this, et1, getString(R.string.email))
                && Validations.valEdit(this, et2, getString(R.string.password))
                && Const.isOnline(this)) {
            try {

                pd.show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.rl_main).getWindowToken(), 0);

                final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);


                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabaseUser.removeEventListener(listener);
                        pd.dismiss();
                        if(dataSnapshot.getValue()!=null){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                try {
                                    UserDto userDto1 = dataSnapshot1.getValue(UserDto.class);
                                    if (userDto1 != null && userDto1.getUser_type().equals(Const.driver)) {
                                        if(userDto1.getPassword().equals(et2.getText().toString())){
                                           new MySharedPref().saveData(getApplicationContext(),Const.ldata, new Gson().toJson(userDto1)+"");

                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),getString(R.string.invalid_password),6).show();

                                        }
                                    }
                                    else {

                                        Toast.makeText(getApplicationContext(),getString(R.string.invalid_email),6).show();
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{

                            Toast.makeText(getApplicationContext(),getString(R.string.invalid_email),6).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mDatabaseUser.removeEventListener(listener);
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),getString(R.string.invalid_email),6).show();

                    }
                };

                mDatabaseUser.orderByChild(UserConst.email).equalTo(et1.getText().toString()).addValueEventListener(listener);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void RegNow(View v){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
