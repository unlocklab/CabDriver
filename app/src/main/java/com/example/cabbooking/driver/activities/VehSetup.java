package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.adapters.CatAdp;
import com.example.cabbooking.driver.dto.CatDto;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.intr.AdpListner;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.ImageCaching;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.other.RoundImage;
import com.example.cabbooking.driver.other.Validations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VehSetup extends AppCompatActivity implements AdpListner, ImagePickerCallback {

    private EditText et1,et3;
    private TextView et2;
    private ProgressDialog pd;
    private List<CatDto> l1 = new ArrayList<>();
    private Dialog dialog1 = null;
    private ImageView imv;
    private UserDto userDto = null;
    private File myFile;
    private String mime = "", file_name = "", fileType = "", fileUrl = "";
    private ImagePicker imagePicker;
    String str1[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void requestReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(VehSetup.this, str1,
                1);
    }

    public void PickNow(View v){
        if (checkIfAlreadyhavePermission()) {
//pic
            imagePicker = new ImagePicker(VehSetup.this);
            imagePicker.setFolderName("Random");
            imagePicker.setRequestId(1234);
            imagePicker.ensureMaxSize(1000, 1000);
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(VehSetup.this);
            Bundle bundle = new Bundle();
            bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
            imagePicker.pickImage();
        } else {
            requestReadPhoneStatePermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            }
        }

    }

    private boolean checkIfAlreadyhavePermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_setup);
        initPD();
        initUI();
        loadCats();
        setData();
    }

    private void setData() {
        try{
            userDto = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                    , UserDto.class);

            et1.setText(userDto.getVeh_no());
            et2.setText(userDto.getVeh_cat());
            et3.setText(userDto.getLic_no());


            ImageCaching.loadImage(imv,this,userDto.getVeh_image());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public void ChNow(View v){
        try{
            dialog1 = new Dialog(VehSetup.this);
            dialog1.setCancelable(true);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.list_pop);

            RecyclerView lv1 = dialog1.findViewById(R.id.lv1);

            CatAdp catAdp = new CatAdp(VehSetup.this,l1);

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
    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ChosenImage image = images.get(0);
        file_name = image.getDisplayName();
        mime = image.getMimeType();

        String path = image.getOriginalPath();
        if (path != null) {
            myFile = new File(path);

            imv.setImageBitmap(RoundImage.getCircularBitmap(RoundImage.createBitmapFromUri(this, Uri.fromFile(myFile))));
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(),message,5).show();
    }
    private void initUI() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);

        imv = findViewById(R.id.imv);

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

                if(myFile!=null){
                    uploadFile();
                }
                else {
                    up_pro();
                }

            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

    private void uploadFile() {
        pd.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage_url));

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(mime)
                .build();
        Uri uri = Uri.fromFile(myFile);
        UploadTask uploadTask = storageRef.child("files/" + uri.getLastPathSegment()).putFile(uri, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), getString(R.string.upload_field), 5).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    try {
                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                fileUrl = uri.toString();
                                userDto.setVeh_image(fileUrl);
                                up_pro();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void up_pro() {
        userDto.setVeh_no(et1.getText().toString());
        userDto.setVeh_cat(et2.getText().toString());
        userDto.setLic_no(et3.getText().toString());

        final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);
        pd.dismiss();
        Toast.makeText(getApplicationContext(),getString(R.string.profile_updated),6).show();
        new MySharedPref().saveData(getApplicationContext(),Const.ldata, new Gson().toJson(userDto)+"");
        mDatabaseUser.child(userDto.getUserId()).setValue(userDto);
        finish();
    }


    public void NavNow(View v){
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
