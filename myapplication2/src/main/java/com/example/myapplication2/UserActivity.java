package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    TextView textViewName, textViewLastName, textViewEmail, textViewRemoteId;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication2.R.layout.activity_user);

        textViewName = findViewById(com.example.myapplication2.R.id.textViewName);
        textViewLastName = findViewById(com.example.myapplication2.R.id.textViewLastName);
        textViewEmail = findViewById(com.example.myapplication2.R.id.textViewEmail);
        textViewRemoteId = findViewById(com.example.myapplication2.R.id.textViewRemoteId);
        imageView = findViewById(com.example.myapplication2.R.id.imageView);
        Intent intent = getIntent();

        String Name = intent.getStringExtra("name");
        String LastName = intent.getStringExtra("last_name");
        String Email = intent.getStringExtra("email");
        int RemoteId = intent.getIntExtra("remoteId", 0);
        String avatar = intent.getStringExtra("avatar");

        textViewName.setText(" " + Name);
        textViewLastName.setText(" " + LastName);
        textViewEmail.setText(" " + Email);
        textViewRemoteId.setText("id = " + RemoteId);
        Picasso.with(UserActivity.this).load(avatar).into(imageView);

    }
}