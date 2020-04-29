package com.example.cabbooking.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabbooking.driver.R;
import com.example.cabbooking.driver.dto.UserConst;
import com.example.cabbooking.driver.dto.UserDto;
import com.example.cabbooking.driver.other.Const;
import com.example.cabbooking.driver.other.ImageCaching;
import com.example.cabbooking.driver.other.MySharedPref;
import com.example.cabbooking.driver.other.RoundImage;
import com.example.cabbooking.driver.other.Validations;
import com.google.android.gms.maps.model.LatLng;
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
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.MediaPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ImagePickerCallback {

    private EditText et1,et2,et3,et4;
    private ProgressDialog pd;
    private ValueEventListener listener = null;
    private UserDto userDto = null;
    private File myFile;
    private ImageView imv;
    private String mime = "", file_name = "", fileType = "", fileUrl = "";
    private ImagePicker imagePicker;
    String str1[] = new String[]{
             Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void requestReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(ProfileActivity.this, str1,
                1);
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

    public void PickNow(View v){
        if (checkIfAlreadyhavePermission()) {
//pic
            imagePicker = new ImagePicker(ProfileActivity.this);
            imagePicker.setFolderName("Random");
            imagePicker.setRequestId(1234);
            imagePicker.ensureMaxSize(1000, 1000);
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(ProfileActivity.this);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initPD();
        initUI();
        setData();
    }

    private void setData() {
        try{
            userDto = new Gson().fromJson(new MySharedPref().getData(getApplicationContext(),Const.ldata,"")
                    , UserDto.class);

            et1.setText(userDto.getFname());
            et2.setText(userDto.getLname());
            et3.setText(userDto.getEmail());
            et4.setText(userDto.getMobile());

            ImageCaching.loadImage(imv,this,userDto.getImage());
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

            imv.setImageBitmap(RoundImage.getCircularBitmap(RoundImage.createBitmapFromUri(this,Uri.fromFile(myFile))));
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
        et4 = findViewById(R.id.et4);
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
                && Validations.valEdit(this, et4, getString(R.string.mobile_number))
                && Const.isOnline(this)) {
            try {
                pd.show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.rl_main).getWindowToken(), 0);

                if(myFile!=null){
                    uploadFile();
                }
                else {
                    up_pro();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void up_pro() {
        userDto.setFname(et1.getText().toString());
        userDto.setLname(et2.getText().toString());
        userDto.setEmail(et3.getText().toString());
        userDto.setMobile(et4.getText().toString());

        checkKeyValue(UserConst.email,userDto.getEmail(),userDto);

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
                                if(userDto1.getEmail().equals(value)
                                        && userDto1.getUserId().equals(userDto.getUserId())==false){
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),getString(R.string.email_exist),6).show();
                                }
                                else{
                                    checkKeyValue(UserConst.mobile,userDto.getMobile(),userDto);
                                }
                            }
                            else{
                                if(userDto1.getMobile().equals(value)
                                        && userDto1.getUserId().equals(userDto.getUserId())==false){
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),getString(R.string.mob_exist),6).show();
                                }
                                else{
                                    updateUser(userDto);
                                }
                            }
                        }
                    }
                    else{
                        if(key.equals(UserConst.email)){
                            checkKeyValue(UserConst.mobile,userDto.getMobile(),userDto);
                        }
                        else{
                            updateUser(userDto);
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
                        updateUser(userDto);
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

    private void updateUser(UserDto userDto){
        final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference(Const.user_tbl);

        Toast.makeText(getApplicationContext(),getString(R.string.profile_updated),6).show();
        new MySharedPref().saveData(getApplicationContext(),Const.ldata, new Gson().toJson(userDto)+"");
        mDatabaseUser.child(userDto.getUserId()).setValue(userDto);
        finish();
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
                                userDto.setImage(fileUrl);
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



    public void NavNow(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
