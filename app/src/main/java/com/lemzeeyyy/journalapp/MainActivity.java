package com.lemzeeyyy.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lemzeeyyy.journalapp.utils.JournalUser;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button create_account;
    private EditText password;
    private AutoCompleteTextView email;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        create_account = findViewById(R.id.create_account);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
        create_account.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Sign_up.class);
            startActivity(intent);
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmailAndPassword(
                        email.getText().toString().trim(),
                        password.getText().toString().trim());

            }
        });
    }

    private void loginEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert  user!= null;
                            final String currentUserId = user.getUid();
                            collectionReference.whereEqualTo("userId",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error!=null){

                                            }
                                            assert value!=null;
                                            if(!value.isEmpty()){
                                                for(QueryDocumentSnapshot snapshot : value){
                                                    JournalUser journalUser = JournalUser.getInstance();
                                                    journalUser.setUsername(snapshot.getString("username"));
                                                    journalUser.setUserid(snapshot.getString("userId"));
                                                    //go to listActivity after successful login
                                                    startActivity(new Intent(MainActivity.this,JournalListActivity.class));
                                                }
                                            }
                                        }
                                    })


                        }
                    })
        }
    }
}