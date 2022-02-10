package com.aqirlone.talkwithbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aqirlone.talkwithbuddies.databinding.ActivityOtpAuthenticationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuthentication extends AppCompatActivity {
ActivityOtpAuthenticationBinding binding;

String enteredOtp;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();

        binding.changenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(otpAuthentication.this,MainActivity.class);
               startActivity(intent);
            }
        });

        binding.verifytop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredOtp=binding.getotp.getText().toString();

                if (enteredOtp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your OTP first", Toast.LENGTH_SHORT).show();
                }else {
                    binding.progressbarofotpauth.setVisibility(View.VISIBLE);

                    String codeReceived=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(codeReceived,enteredOtp);
                    signInWithPhoneAuthCredential(credential);


                }


            }
        });




    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful()){
                    binding.progressbarofotpauth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpAuthentication.this,setProfile.class);
                    startActivity(intent);
                    finish();
                }else{
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}