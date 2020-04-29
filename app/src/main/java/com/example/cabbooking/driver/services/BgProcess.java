package com.example.cabbooking.driver.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.other.ApiCall;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;


public class BgProcess extends IntentService {

    private static final String ACTION_FOO = "roplar.app.voice.sunil.action.FOO";
    private static final String ACTION_FOO1 = "roplar.app.voice.sunil.action.FOO1";
    private static final String ACTION_FOO2 = "roplar.app.voice.sunil.action.FOO2";
    private static final String ACTION_FOO3 = "roplar.app.voice.sunil.action.FOO3";
    private ValueEventListener listener;

    public BgProcess() {
        super("BgProcess");
    }


    public static void sendNotification(Context context, String userId,String tripId, String title, String des) {
        Intent intent = new Intent(context, BgProcess.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(Const.user_id, userId);
        intent.putExtra(Const.title, title);
        intent.putExtra(Const.description, des);
        intent.putExtra(Const.tripId, tripId);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String title = intent.getStringExtra(Const.title);
                final String user_id = intent.getStringExtra(Const.user_id);
                final String description = intent.getStringExtra(Const.description);
                final String tripId = intent.getStringExtra(Const.tripId);
                MyBgTask(user_id,title,description,tripId);
            }
        }
    }

    private void MyBgTask(String user_id, final String title, final String description, final String tripId) {
        final UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata, "")
                , UserDto.class);
        final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
        final boolean[] done = {true};
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseUser.removeEventListener(listener);
                if(dataSnapshot.getValue()!=null && done[0]){
                    done[0] = false;
                    UserDto userDto = dataSnapshot.getValue(UserDto.class);
                    JSONArray tokens = new JSONArray();
                    tokens.put(userDto.getToken());

                    sendBulkNoti(tokens
                    ,title
                    ,description
                    ,tripId
                    ,ldata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDatabaseUser.removeEventListener(listener);
            }
        };

        mDatabaseUser.child(user_id).addValueEventListener(listener);
    }


    private void sendBulkNoti(JSONArray tokens, String title, String description,String tripId,UserDto ldata) {
        try {
            String url = Const.fcmUrl;

            JSONObject data = new JSONObject();
            data.put(Const.noti_type,1);
            data.put(Const.title,title);
            data.put(Const.description,description);
            data.put(Const.user_id,ldata.getUserId());
            data.put(Const.tripId,tripId);
            data.put(Const.datetime,Const.getLocalTime(Const.getUtcTime()));

            String strJsonBody = "{\n" +
                    "  \"registration_ids\" : " + tokens + ",\n" +
                    "  \"data\" : {\n" +
                    "  \"message\" : \"ssdn\",\n" +
                    "\"my_data\" :" + data + "" +
                    "  }\n" +
                    "}";

            ApiCall.makePostData(this, url, new JSONObject(strJsonBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {
                    System.out.println("tcheck------2--------"+data);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
