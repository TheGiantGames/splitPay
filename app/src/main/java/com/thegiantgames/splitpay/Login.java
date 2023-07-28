package com.thegiantgames.splitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    EditText et_username , et_password , et_email;
    TextView btn_back, btn_login;
    String username , password , email  ,res;


    OkHttpClient okHttpClient;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                email = et_email.getText().toString();
                requests();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.v("failed" , " Failed");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.code() == 200){
                                Intent intent = new Intent(Login.this, UserData.class);
                                intent.putExtra("profile" , response.body().string());
                                startActivity(intent);
                              // startActivity(new Intent(Login.this , UserData.class));
                               Login.this.finish();
                            }
                            if (response.code() == 500){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "Invalid Credentials ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                    }
                });
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this , Start.class));
            }
        });


    }

    public void init(){
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        btn_login = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_back);
    }

    public void requests(){
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , username);
        jsonObject.addProperty("password" , password);
        jsonObject.addProperty("email" , email);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/login").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
    }

}