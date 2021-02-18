package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    EditText emailTV, passwordTV, passwordConfirmTV;
    Button btnRegister, btnLogin;
    FirebaseAuth fAuth;
    ProgressBar pB;

    // but FireBase requires more than 5 characters for password
    int MIN_LENGTH_PASSWORD = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(
                savedInstanceState);
        setContentView(R.layout .activity_register);
        emailTV = findViewById(R.id.TextEmail);
        passwordTV = findViewById(R.id.TextPass);
        passwordConfirmTV = findViewById(R.id.TextPassConfirm);

        fAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnAuth);

        pB = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String email = emailTV.getText().toString().trim();
                String password = passwordTV.getText().toString().trim();
                String passwordConfirm = passwordConfirmTV.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailTV.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordTV.setError("Password is requierd.");
                    return;
                }

                if(TextUtils.isEmpty(passwordConfirm)){
                    passwordConfirmTV.setError("Password confirmation is reqired.");
                    return;
                }

                if(password.length() < MIN_LENGTH_PASSWORD)
                {
                    passwordTV.setError("Password must be longer than "+MIN_LENGTH_PASSWORD+" characters.");
                    return;
                }

                if(! password.equals(passwordConfirm)){
                    passwordConfirmTV.setError("Please confirm password.");
                    return;
                }

                pB.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                                Toast.makeText(RegisterActivity.this, "Error ! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pB.setVisibility(View.GONE);
                        }


                    }
                });



            }

        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



    }




}
