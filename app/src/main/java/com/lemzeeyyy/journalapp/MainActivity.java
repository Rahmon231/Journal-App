package com.lemzeeyyy.journalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button create_account;
    private EditText password;
    private AutoCompleteTextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        create_account = findViewById(R.id.create_account);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        create_account.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,Sign_up.class);
            startActivity(intent);
        });
    }
}