package com.example.cabbooking.driver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.AutoAdp;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.other.Validations;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText et1,et2,et3,et4,et5;
    private TextView cc_digit;
    private ProgressDialog pd;
    private AutoAdp sa;
    private BottomSheetDialog dialog = null;
    private JSONArray countries = null, countries1 = null;

    private JSONArray filterData(String s) {
        JSONArray lk = new JSONArray();
        try {
            for (int i = 0; i < countries.length(); i++) {
                String str = "";
                str = countries.getJSONObject(i).getString(Const.name).toLowerCase().trim();
                str.trim();
                if (str.toLowerCase().contains(s.toLowerCase().trim())) {
                    lk.put(countries.getJSONObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lk;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        try {
            countries = new JSONArray(Const.country_codes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI();
        initPD();

        cc_digit = findViewById(R.id.cc_digit);

        findViewById(R.id.cc_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cc_digit.performClick();
            }
        });

        cc_digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPop();
            }
        });
    }
    private void CountryPop() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            dialog = new BottomSheetDialog(this);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.country_code_popup);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            final ListView gv1 = dialog.findViewById(R.id.gv1);
            final EditText search_et = dialog.findViewById(R.id.search_et);


            sa = new AutoAdp(this, countries);
            gv1.setAdapter(sa);
            gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject country = countries.getJSONObject(position);
                        cc_digit.setText(country.getString(Const.dial_code));
//                        cname = country.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            search_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (search_et.getText().toString().length() > 0) {
                        countries1 = filterData(search_et.getText().toString());
                        sa = new AutoAdp(SignupActivity.this, countries1);
                        gv1.setAdapter(sa);
                        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject country = countries1.getJSONObject(position);
                                    cc_digit.setText(country.getString(Const.dial_code));
//                                    cname = country.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                    } else {
                        sa = new AutoAdp(SignupActivity.this, countries);
                        gv1.setAdapter(sa);
                        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject country = countries.getJSONObject(position);
                                    cc_digit.setText(country.getString(Const.dial_code));
//                                    cname = country.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        cc_digit = findViewById(R.id.cc_digit);
    }


    private void initPD() {
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));
    }

    public void LoginNow(View v){
        if (Validations.valEdit(this, et1, getString(R.string.fname))
                && Validations.valEdit(this, et2, getString(R.string.lname))
                && Validations.valEditEmail(this, et3, getString(R.string.email))
                && Validations.valEditMob(this, et4, getString(R.string.mobile_number))
                && Validations.valEditPassword(this, et5, getString(R.string.password))
                && Const.isOnline(this)) {
            try {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.rl_main).getWindowToken(), 0);

                JSONObject jk = new JSONObject();

                jk.put(UserConst.fname, et1.getText().toString());
                jk.put(UserConst.lname, et2.getText().toString());
                jk.put(UserConst.email, et3.getText().toString());
                jk.put(UserConst.mobile, cc_digit.getText().toString() + et4.getText().toString());
                jk.put(UserConst.password, et5.getText().toString());
                System.out.println("dfg-----------------"+jk);
                new MySharedPref().saveData(getApplicationContext(),Const.signup_data,jk+"");

                startActivity(new Intent(getApplicationContext(),SignupActivity1.class));
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
