package com.example.myapplication2;

//import
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

        textViewName = findViewById(com.example.myapplication2.R.id.tv_name);
        textViewLastName = findViewById(com.example.myapplication2.R.id.tv_last_name);
        textViewEmail = findViewById(com.example.myapplication2.R.id.tv_email);
        textViewRemoteId = findViewById(com.example.myapplication2.R.id.tv_remote_id);
        imageView = findViewById(com.example.myapplication2.R.id.img_user);
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String lastName = intent.getStringExtra("last_name");
        String email = intent.getStringExtra("email");
        int remoteId = intent.getIntExtra("remoteId", 0);
        String avatar = intent.getStringExtra("avatar");

        textViewName.setText(" " + name);
        textViewLastName.setText(" " + lastName);
        textViewEmail.setText(" " + email);
        textViewRemoteId.setText("id = " + remoteId);
        Picasso.with(UserActivity.this).load(avatar).into(imageView);
    }

}