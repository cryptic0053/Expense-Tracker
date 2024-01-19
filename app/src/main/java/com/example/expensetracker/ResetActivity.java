package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    Button btnReset,btnBack;
    EditText edtEmail;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    String strEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        btnBack=findViewById(R.id.btnForgotPasswordBack);
        btnReset=findViewById(R.id.btnReset);
        edtEmail=findViewById(R.id.edtForgotPasswordEmail);
        progressBar=findViewById(R.id.forgetPasswordProgressbar);

        mAuth=FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail=edtEmail.getText().toString().trim();
                if(!TextUtils.isEmpty(strEmail)){
                    ResetPassword();
                }
                else {
                    edtEmail.setError("Email field can't be Empty");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void ResetPassword(){
        progressBar.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetActivity.this,"Reset Password link has been sent to your Email",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetActivity.this,"Error:-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btnReset.setVisibility(View.VISIBLE);

                    }

                });
    }
}
