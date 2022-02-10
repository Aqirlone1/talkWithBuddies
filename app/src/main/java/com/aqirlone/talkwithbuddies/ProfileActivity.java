package com.aqirlone.talkwithbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aqirlone.talkwithbuddies.databinding.ActivityProfileBinding;
import com.aqirlone.talkwithbuddies.databinding.ActivitySetProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

   ActivityProfileBinding binding;
   FirebaseDatabase firebaseDatabase;
   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   StorageReference storageReference;
   private String imageURIAccessToken;
   FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();


        binding.backbuttonofviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        storageReference=firebaseStorage.getReference();
       storageReference.child("Images").child(firebaseAuth.getUid()).child("profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURIAccessToken=uri.toString();
                Picasso.get().load(uri)

                        .into(binding.viewuserimageinimageview);


            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfileModel userProfileModel=snapshot.getValue(UserProfileModel.class);
                binding.viewusername.setText(userProfileModel.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed To Fetch", Toast.LENGTH_SHORT).show();

            }
        });
        binding.movetoupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,UpdateProfile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //user is now offline
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //user is now offline
            }
        });

    }
}