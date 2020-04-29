package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        createUser();

    }
    private void createUser() {
        try {
            mAuth.signInWithEmailAndPassword(Const.admin_email, Const.admin_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkLogin();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), 6).show();
                            }

                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
    private void checkLogin() {
        try{

                new Thread(){

                    @Override
                    public void run() {
                        try{
                            if(new MySharedPref().getData(getApplicationContext(), Const.ldata,"").length()>0){
                                sleep(1000);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                            else {
                                sleep(2000);
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoginNow(View v){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    public void RegNow(View v){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }
}
