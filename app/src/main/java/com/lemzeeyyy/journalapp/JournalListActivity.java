package com.lemzeeyyy.journalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class JournalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView noPostTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        recyclerView = findViewById(R.id.recyclerView);
        noPostTV = findViewById(R.id.list_no_post);
    }
}