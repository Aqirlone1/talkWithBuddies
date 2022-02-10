package com.aqirlone.talkwithbuddies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aqirlone.talkwithbuddies.databinding.ActivitySetProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class setProfile extends AppCompatActivity {

    ActivitySetProfileBinding binding;
    private final static int PICK_IMAGE=123;
    private Uri imagepath;
    private  FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriAccessToken;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();


        binding.getuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        binding.saveprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=binding.getusername.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Name Is Empty", Toast.LENGTH_SHORT).show();
                }else if(imagepath==null){
                    Toast.makeText(getApplicationContext(), "Image is empty", Toast.LENGTH_SHORT).show();
                }else{

                    binding.progressbarofsetprofile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    binding.progressbarofsetprofile.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(setProfile.this,chatActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){

            imagepath=data.getData();                                   ////looooooooooooooooooooooooook heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrreeeeeeeeee
            binding.getuserimageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendDataForNewUser(){
        sendDataToRealTimeDatabase();
    }



    private void  sendDataToRealTimeDatabase(){
        name=binding.getusername.getText().toString();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=
                firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfileModel userProfileModel=new UserProfileModel(name,firebaseAuth.getUid());
        databaseReference.setValue(userProfileModel);
       // Toast.makeText(getApplicationContext(), "User Profile Added Successfully", Toast.LENGTH_SHORT).show();
        sendImageToStorage();

    }
    private void sendImageToStorage(){

        StorageReference imagrref=storageReference.child("Images").child(firebaseAuth.getUid()).child("profile Pic");
    //Image Compression Code
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
        }catch (IOException e){

            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        // putting image to storage
        UploadTask uploadTask=imagrref.putBytes(data);

uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        imagrref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageUriAccessToken=uri.toString();
             //   Toast.makeText(getApplicationContext(), "uri get successful", Toast.LENGTH_SHORT).show();
                sendDataToCloudFireStore();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "uri get Failed", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e)
    {
        Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
    }
});




            }

    private void sendDataToCloudFireStore() {

        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String ,Object> userData=new HashMap<>();
        userData.put("name",name);
        userData.put("image",ImageUriAccessToken);
        userData.put("uid",firebaseAuth.getUid());
        userData.put("status","Online");

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
               // Toast.makeText(getApplicationContext(), "Data On Cloud FireStorage Send Successfully ", Toast.LENGTH_SHORT).show();

            }
        });


    }
}

