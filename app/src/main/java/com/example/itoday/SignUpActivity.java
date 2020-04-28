package com.example.itoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email,password,confirm_password;
    String Email,Password,ConfirmPassword;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_si_text);
        password = findViewById(R.id.password_si_text);
        confirm_password = findViewById(R.id.confirm_password_si_text);
        signup = findViewById(R.id.signup_si_btn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                ConfirmPassword = confirm_password.getText().toString();
                Log.e("Email",Email);
                Log.e("Password",Password);
                Log.e("Confirm Password",ConfirmPassword);
                Signup(Email,Password,ConfirmPassword);
            }
        });
    }
    
    public void Signup(String Email,String Password,String ConfirmPassword)
    {
        Log.e("Password",Password);
        Log.e("Confirm Password",ConfirmPassword);
        if(Password.equals(ConfirmPassword)) {
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Message :", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUpActivity.this, "User Registration Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(i);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Error Message :", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(SignUpActivity.this, "Both Passwords are not same.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
