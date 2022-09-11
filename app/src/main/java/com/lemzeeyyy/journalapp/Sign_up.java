package com.lemzeeyyy.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_up extends AppCompatActivity {

    private Button signup;
    private EditText password_signup;
    private AutoCompleteTextView email_signup;

    //Firebase Auth
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    //Firebase Firestore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference reference = database.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.signup);
        password_signup = findViewById(R.id.password_signup);
        email_signup = findViewById(R.id.email_signup);
        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if(currentUser!=null){
                //User Already Logged In
            }else {
                //No user yet
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email_signup.getText().toString()) && 
                        !TextUtils.isEmpty(password_signup.getText().toString())){
                    
                    String email = email_signup.getText().toString();
                    String password = password_signup.getText().toString();
                    
                    createUserWithEmailAndPassword(email,password);
                }else {
                    Toast.makeText(Sign_up.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password) {

    }
}