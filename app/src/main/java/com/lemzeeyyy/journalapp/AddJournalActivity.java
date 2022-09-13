package com.lemzeeyyy.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lemzeeyyy.journalapp.model.Journal;
import com.lemzeeyyy.journalapp.utils.JournalUser;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 1;
    private ImageView postImage;
    private TextView dateTextView, currentUserTextView;
    private Button postJournalBtn;
    private ImageView addPhoto;
    private EditText titleEditText, thoughtsEditText;
    private ProgressBar progressBar;

    //UserId & UserName
    private String currentUserName;
    private String currentUserId;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private StorageReference storageReference;
    private CollectionReference collectionReference = database.collection("Journal");
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        initWidgets();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        if(JournalUser.getInstance() != null){
            currentUserName = JournalUser.getInstance().getUsername();
            currentUserId = JournalUser.getInstance().getUserid();
            currentUserTextView.setText(currentUserName);
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null){

                }else {

                }
            }
        };
        postJournalBtn.setOnClickListener(view -> saveJournal());

        addPhoto.setOnClickListener(view -> {
            Intent galleryImage = new Intent(Intent.ACTION_GET_CONTENT);
            galleryImage.setType("image/*");
            startActivityForResult(galleryImage,GALLERY_CODE);
        });


    }

    private void saveJournal() {
        final String title = titleEditText.getText().toString().trim();
        final String thoughts = thoughtsEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)
        && !TextUtils.isEmpty(thoughts)
        && imageUri!=null){
            final StorageReference filePath = storageReference
                    .child("journal_images")
                    .child("my_image_"+ Timestamp.now().getSeconds());

            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Journal journal = new Journal();
                                    journal.setTitle(title);
                                    journal.setThoughts(thoughts);
                                    journal.setImageUrl(imageUrl);
                                    journal.setTimeAdded(new Timestamp(new Date()));
                                    journal.setUserName(currentUserName);
                                    journal.setUserId(currentUserId);
                                    collectionReference.add(journal)
                                            .addOnSuccessListener(documentReference -> {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                startActivity(new Intent(AddJournalActivity.this,
                                                        JournalListActivity.class));
                                                finish();
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });


                                }
                            }).addOnFailureListener(e -> {

                            });
                        }
                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.getData();
                postImage.setImageURI(imageUri);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void initWidgets() {
        postImage = findViewById(R.id.post_image);
        currentUserTextView = findViewById(R.id.username_tv);
        postJournalBtn = findViewById(R.id.save_journal_btn);
        addPhoto = findViewById(R.id.post_camera_btn);
        titleEditText = findViewById(R.id.post_title_ET);
        thoughtsEditText = findViewById(R.id.post_description_et);
        progressBar = findViewById(R.id.post_progresssBar);

    }
}