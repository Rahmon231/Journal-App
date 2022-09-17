package com.lemzeeyyy.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.lemzeeyyy.journalapp.adapter.JournalListAdapter;
import com.lemzeeyyy.journalapp.model.Journal;
import com.lemzeeyyy.journalapp.utils.JournalUser;

import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView noPostTV;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Journal> journalList;
    private CollectionReference collectionReference = database.collection("Journal");
    private JournalListAdapter journalListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        //Widgets
        recyclerView = findViewById(R.id.recyclerView);
        noPostTV = findViewById(R.id.list_no_post);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



        //PostArrayList
        journalList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(user!=null && firebaseAuth!=null){
                    startActivity(new Intent(
                            JournalListActivity.this,
                            AddJournalActivity.class));
                }
                break;

            case R.id.sign_out:
                if(user!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(
                            JournalListActivity.this,
                            MainActivity.class
                    ));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId", JournalUser.getInstance().getUserid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot journals : queryDocumentSnapshots){
                                Journal journal = journals.toObject(Journal.class);
                                journalList.add(journal);

                            }
                            JournalListAdapter journalListAdapter = new JournalListAdapter(getApplicationContext(),journalList);
                            recyclerView.setAdapter(journalListAdapter);
                            journalListAdapter.notifyDataSetChanged();
                        }else {
                            noPostTV.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JournalListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}