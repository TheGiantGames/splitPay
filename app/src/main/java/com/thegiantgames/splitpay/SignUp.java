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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    EditText et_username ,et_password, et_email;
    TextView btn_signup , btn_back;
    String res , username , password, email;
    OkHttpClient okhttpclient;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_password.getText().toString();
                email = et_email.getText().toString();
                apiRequests();

                if (!username.equals("")  && !password.equals("") && !email.equals("")){
                    okhttpclient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            Log.v("failed" , " " + "failed");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            Log.v("response" , "" + response.body().string());
                            if (response.code() == 500){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(SignUp.this, "Username already exist", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            if (response.code() == 200){
                                startActivity(new Intent(SignUp.this , MainActivity.class));
                                SignUp.this.finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SignUp.this, "All Fields required", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this , Start.class));
            }
        });

    }

    public  void  init(){
        et_email = findViewById(R.id.et_email_signup);
        et_password = findViewById(R.id.et_password_signup);
        et_username = findViewById(R.id.et_username_signup);
        btn_signup  = findViewById(R.id.btn_signup);
        btn_back = findViewById(R.id.btn_back);
    }

    public void apiRequests(){
        okhttpclient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , username);
        jsonObject.addProperty("password" , password);
        jsonObject.addProperty("email" , email);
        okhttpclient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/signup").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();

    }

}