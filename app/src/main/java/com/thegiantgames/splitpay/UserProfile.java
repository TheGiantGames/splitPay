package com.thegiantgames.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends AppCompatActivity {

    TextView tv_username_profile, tv_email_profile ;
    ImageView iv_back;

    String username , email;
    public static String USER ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();

        String res = getIntent().getStringExtra("profile");
        try {
            JSONObject root = new JSONObject(res);
            JSONObject user = root.getJSONObject("user");
            USER = user.getString("username");
            email = user.getString("email");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        tv_username_profile.setText("Username: - " + username);
        tv_email_profile.setText("Email: - " + email);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this , UserData.class));
            }
        });

    }

    public void init(){
        tv_email_profile = findViewById(R.id.tv_email_profile);
        tv_username_profile = findViewById(R.id.tv_username_profile);
        iv_back = findViewById(R.id.iv_back);
    }
}