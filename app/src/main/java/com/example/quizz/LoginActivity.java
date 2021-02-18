package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity<EditView> extends AppCompatActivity {

    Button btnAuth, btnRegister;
    TextView emailTV, passwordTV;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    int MIN_LENGTH_PASSWORD = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAuth = findViewById(R.id.btnAuth);
        btnRegister = findViewById(R.id.btnRegister);

        emailTV = findViewById(R.id.textEmail);
        passwordTV = findViewById(R.id.textPassword);
        progressBar = findViewById(R.id.progressBar2);

        fAuth = FirebaseAuth.getInstance();




        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTV.getText().toString().trim();
                String password = passwordTV.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailTV.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordTV.setError("Password is required.");
                    return;
                }

                if(password.length() < MIN_LENGTH_PASSWORD)
                {
                    passwordTV.setError("Password must be longer than "+MIN_LENGTH_PASSWORD+" characters.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error ! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }
}