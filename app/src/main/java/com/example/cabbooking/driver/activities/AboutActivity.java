package com.example.cabbooking.driver.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.other.Const;


/**
 * Created by Roshani
 * its about screen or we can say content screen where we will show content
 * category wise like terms and condition ,about us etc
 */

public class AboutActivity extends AppCompatActivity {

    /*
    * init Ui in class
    *
    * */
    private WebView wv1;
    public void NavNow(View v){
        finish();
    }
    private String title = "";

    /*
    * this method connects Ui xml file with java file
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        title = getIntent().getExtras().getString(Const.data);
        initUI();
    }

    /*
    * loading content in Webview because text align justify doesnot works in android
    * and this code is supportable for all devices 4.4 to max
    * */
    private void initUI() {
//        TextView title_txt= (TextView) findViewById(R.id.title_txt);
//        title_txt.setText(title);
        wv1 = (WebView) findViewById(R.id.wv1);


        String str = "<html>" +
                "<body bgcolor=\"#ffffff\" text=\"#4a4949\" style=\"text-align: justify;\">" +
                "<b>All in One Solution application for Delhi Metro and DTC Bus Routes and Fares!" +
                "</br></br>\n" +
                "Features-</b>" +
                "</br> \n" +
                "1. Delhi Public Transport App provides all the Routes, Fare, Journey Time in between any two Bus or Metro stations.\n" +
                "</br> \n" +
                "2. It provides all Delhi Metro and Bus Route Details.\n" +
                "</br> \n" +
                "3. It doesn’t require any internet connection to find out any routes or fares.\n" +
                "</br> \n" +
                "4. Get the nearest Metro Station and DTC Bus Stop from your Current location in just one click.\n" +
                "</br> \n" +
                "5. Airport & Rapid Metro Line Gurgaon also integrated.\n" +
                "</br> \n" +
                "6. Get multiple Bus or Metro  routes from source to destination.\n" +
                "</br> \n" +
                "7. Latest fare chart updated.\n" +
                "</body>" +
                "</html>";

        wv1.loadDataWithBaseURL(null,str,"text/html", "utf-8", null);
    }

}
