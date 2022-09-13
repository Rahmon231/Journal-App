package com.lemzeeyyy.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lemzeeyyy.journalapp.utils.JournalUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sign_up extends AppCompatActivity {

    private Button signup;
    private EditText password_signup;
    private AutoCompleteTextView email_signup;
    private EditText username_signup;

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
        firebaseAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signup);
        password_signup = findViewById(R.id.password_signup);
        email_signup = findViewById(R.id.email_signup);
        username_signup = findViewById(R.id.username_signup);

        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if(currentUser!=null){
                //User Already Logged In
            }else {
                //No user yet
            }
        };
        signup.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(email_signup.getText().toString()) &&
                    !TextUtils.isEmpty(password_signup.getText().toString())){

                String email = email_signup.getText().toString().trim();
                String password = password_signup.getText().toString().trim();
                String username = username_signup.getText().toString().trim();

                createUserWithEmailAndPassword(email,password,username);
            }else {
                Toast.makeText(Sign_up.this, "Empty Fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password,final String username) {
        if(!TextUtils.isEmpty(email_signup.getText().toString()) &&
                !TextUtils.isEmpty(password_signup.getText().toString())){
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){

                            //Take User To Add Journal Activity

                            currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser!=null;
                            final String currentUserId = currentUser.getUid();

                            Map<String,String> userObj = new HashMap<>();
                            userObj.put("userId",currentUserId);
                            userObj.put("username",username);
                            Log.d("TAGY", "onSuccess: code got here"+userObj.get("username"));
                            reference.add(userObj)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("TAGY", "onSuccess: code got here"+userObj.get("username"));
                                            documentReference.get()
                                                    .addOnCompleteListener(task1 -> {
                                                        Log.d("TAGY", "createUserWithEmailAndPassword: Code got here" + ":present");
                                                        Log.d("TAGY", "createUserWithEmailAndPassword: " + task1.getResult().exists());
                                                        if (Objects.requireNonNull(task1.getResult().exists())) {
                                                            String name = task1.getResult().getString("username");
                                                            Log.d("TAGY", "createUserWithEmailAndPassword: " + name);
                                                            //If user is registered successfully,
                                                            // move to AddJournalActivity
                                                            JournalUser journalUser = JournalUser.getInstance();
                                                            journalUser.setUserid(currentUserId);
                                                            journalUser.setUsername(name);
                                                            Intent intent = new Intent(Sign_up.this,
                                                                    AddJournalActivity.class);
                                                            intent.putExtra("username", name);
                                                            intent.putExtra("userid", currentUserId);
                                                            Sign_up.this.startActivity(intent);

                                                        }
                                                    }).addOnFailureListener(e ->
                                                            Log.d("TAGY", "createUserWithEmailAndPassword: "+e.getMessage()));
                                        }
                                    }).addOnFailureListener(e ->
                                            Log.d("TAGY", "createUserWithEmailAndPassword: "+e.getMessage()));
                        }

                    }).addOnFailureListener(e -> {
                        Log.d("TAGY", "createUserWithEmailAndPassword: "+e.getMessage());
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}