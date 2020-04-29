package com.example.cabbooking.driver.Fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.activities.MainActivity;
import com.example.cabbooking.driver.activities.MyRides;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.MySharedPref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private ValueEventListener listener, listener1, listener2;
    public static String CHANNEL_ID = "Sinch Push Notification Channel";

    private NotificationChannel mChannel;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("remoteMessage-----------------------"+remoteMessage.getData().toString().contains("noti_type")+"------------------" + remoteMessage.getData());

        try {
            String str = remoteMessage.getData().get("my_data");
            if (str != null) {
                System.out.println("remoteMessage------------34-----------"+str+"------------------");
                parseData(new JSONObject(str));
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        try{
            UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata, "")
                    , UserDto.class);
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
            mDatabase.child(ldata.getUserId()).child(Const.token).setValue(token);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseData(final JSONObject data1) {
        try {
            MySharedPref sp = new MySharedPref();
            try {
                UserDto ldata = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(), Const.ldata, "")
                        , UserDto.class);

                final JSONObject jk = data1;
                List<String> list  = new ArrayList<>();
                switch (jk.getInt(Const.noti_type)) {
                    case 1:
                        if (jk.getString(Const.user_id).equals(ldata.getUserId())==false) {
                            list.add(jk.getString(Const.description));
                            Intent intent = new Intent(getApplicationContext(), MyRides.class);
                            intent.putExtra(Const.tripId,jk.getString(Const.tripId));
                            displayCustomNotificationForOrders(Const.getRand()
                                    , jk.getString(Const.user_id)
                                    , intent
                                    , jk.getString(Const.title)
                                    , list, null);

                        }
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayCustomNotificationForOrders(int x, String groupId, Intent intent, String title, List<String> description, Bitmap userImage) {
        try {

            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.InboxStyle ns = new NotificationCompat.InboxStyle();
            ns.setBigContentTitle(title);

            for(String desc:description) {
                ns.addLine(desc);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                System.out.println("show--------3-------");
                NotificationCompat.Builder builder;

                PendingIntent pendingIntent;
                int importance = NotificationManager.IMPORTANCE_HIGH;

                if (mChannel == null) {
                    mChannel = new NotificationChannel
                            (getString(R.string.app_name), title, importance);
                    mChannel.setDescription(getString(R.string.messages));
                    mChannel.setSound(null,null);
                    mChannel.setShowBadge(true);
                    mChannel.enableVibration(false);
                    notifManager.createNotificationChannel(mChannel);

                }
                builder = new NotificationCompat.Builder(this, getString(R.string.app_name));

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, x, intent, PendingIntent.FLAG_ONE_SHOT);
                builder
                        .setSmallIcon(R.drawable.logo) // required
                        .setContentTitle(title)
                        .setContentText(android.text.TextUtils.join("\n", description))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(ns)
                        .setSound(null)
                        .setAutoCancel(true)
                        .setGroup(groupId)
                        .setGroupSummary(true)
                        .setContentIntent(pendingIntent);
                Notification notification = builder.build();
                notifManager.notify(x, notification);
                System.out.println("show--------4-------");
            } else {
                System.out.println("show--------1-------");
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(this);
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

                //set
                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.drawable.logo);
                builder.setContentText(android.text.TextUtils.join("\n", description));
                builder.setContentTitle(title);
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_ALL);

                Notification notification = builder.build();
                nm.notify((int) System.currentTimeMillis(),notification);
                System.out.println("show--------2-------");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        playNotificationSound();
    }
    private void playNotificationSound() {

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.notification);

        try {
            Ringtone r = RingtoneManager.getRingtone(this, soundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
            Ringtone r = RingtoneManager.getRingtone(this, soundUri);
            r.play();
        }
    }


}
