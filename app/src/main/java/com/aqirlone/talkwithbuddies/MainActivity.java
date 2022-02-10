package com.aqirlone.talkwithbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aqirlone.talkwithbuddies.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

ActivityMainBinding binding;

String countrycode;
String phonenumber;
FirebaseAuth auth;

PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
String codesent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      auth=FirebaseAuth.getInstance();

      countrycode=binding.countrcodeypicker.getSelectedCountryCodeWithPlus();
      binding.countrcodeypicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
          @Override
          public void onCountrySelected() {
              countrycode=binding.countrcodeypicker.getSelectedCountryCodeWithPlus();
          }
      });
        binding.setotpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  number=binding.getphonenumber.getText().toString();
                if (number.isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter your number", Toast.LENGTH_LONG).show();
                }else if (number.length()<10){
                    Toast.makeText(getApplicationContext(), "please enter correct number", Toast.LENGTH_LONG).show();
                }else
                {
                    binding.progressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber=countrycode+number;

                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60l, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);


                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                // for automatic verification type code here

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {


            }


            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_LONG).show();
                binding.progressbarofmain.setVisibility(View.INVISIBLE);
                codesent=s;
                Intent intent =new Intent(MainActivity.this,otpAuthentication.class);
                intent.putExtra("otp",codesent);
                startActivity(intent);

            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent =new Intent(MainActivity.this,chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}