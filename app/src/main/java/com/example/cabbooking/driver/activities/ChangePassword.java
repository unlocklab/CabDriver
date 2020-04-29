package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class ChangePassword extends AppCompatActivity {

    private EditText et1,et2,et3;
    private ProgressDialog pd;
    private UserDto userDto = null;
    private ValueEventListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_password);
        initPD();
        initUI();
        userDto = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                , UserDto.class);
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
        if (Validations.valEdit(this, et1, getString(R.string.old_pass))
                && Validations.valEdit2(this, et2, getString(R.string.new_pass))
                && Validations.valEdit(this, et3, getString(R.string.re_new_pass))
                && Const.isOnline(this)) {
            try {
                pd.show();
                if(et2.getText().toString().equals(et3.getText().toString())) {
                    final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

                    listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDatabaseUser.removeEventListener(listener);
                            if(dataSnapshot.getValue()!=null){
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    UserDto userDto = dataSnapshot1.getValue(UserDto.class);
                                    if(userDto.getPassword().equals(et1.getText().toString())){
                                        userDto.setPassword(et2.getText().toString());

                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.password_ch), 6).show();
                                        new MySharedPref().saveData(getApplicationContext(), Const.ldata, new Gson().toJson(userDto) + "");
                                        mDatabaseUser.child(userDto.getUserId()).setValue(userDto);
                                        finish();
                                    }
                                    else{
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_old_password), 6).show();

                                    }
                                }
                            }
                            else{
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), 6).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mDatabaseUser.removeEventListener(listener);
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), 6).show();

                        }
                    };

                    mDatabaseUser.orderByChild(UserConst.email).equalTo(userDto.getEmail()).addValueEventListener(listener);



                }
                else{
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.same_pass_error), 6).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }


    public void NavNow(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
