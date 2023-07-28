package com.thegiantgames.splitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserData extends AppCompatActivity {

    FloatingActionButton fab_plus , fab_expenses, fab_group, fab_person;
    ImageView iv_userprofile;
    TextView tv_group , tv_person , tv_expenses , tv_intent;

    OkHttpClient okHttpClient;
    Request request;
    GroupArrayAdaptor adaptor;
    Handler handler ,handler1;
    ArrayList<Group> arrayList;
    String username , email , password ,res;

    ArrayList<String> groupNames;

    Boolean fab_visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        handler = new Handler();
        handler1 = new Handler();
        groupNames = new ArrayList<>();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        init();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                requestGroups();
//            }
//        },1000);

        arrayList = new ArrayList<Group>();
        //arrayList.add(new Group(R.drawable.back , groupNames.get(0), "fhkdj"));
        //arrayList.add(new Group(R.drawable.back , groupNames.get(1) , "fhkdj"));
        adaptor = new GroupArrayAdaptor(this,arrayList);
        ListView listView = findViewById(R.id.lv_group_list);
        listView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();
        requestGroups();
        adaptor.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserData.this, MemberData.class);
                intent.putExtra("clickPosition" , arrayList.get(position).getGroupName());
                intent.putExtra("username" , username);
                startActivity(intent);
            }
        });



        iv_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(UserData.this, UserProfile.class);
               intent.putExtra("profile" , getIntent().getStringExtra("profile"));
               startActivity(intent);
            }
        });

        fab_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(UserData.this);
                dialog.setContentView(R.layout.group_name_dialog);
                dialog.getWindow().setLayout((int) (width), WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.show();
                EditText editText = dialog.findViewById(R.id.et_personname_dialog);
                TextView btn_okay = dialog.findViewById(R.id.btn_person_dialog_okay);
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String group = editText.getText().toString();
                        createGroups(group);
                        arrayList.clear();
                        adaptor.notifyDataSetChanged();
                        dialog.cancel();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(getIntent());
                            }
                        },1000);
                    }
                });

            }
        });



        //tv_intent.setText(user);


        String user = getIntent().getStringExtra("profile");
        try {
            JSONObject root = new JSONObject(user);
            JSONObject us = root.getJSONObject("user");
            username = us.getString("username");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void init(){

       // fab_expenses = findViewById(R.id.fab_expenses);
        fab_group  = findViewById(R.id.fab_group);
       // fab_person = findViewById(R.id.fab_person);
        //fab_plus = findViewById(R.id.fab_plus);
        tv_expenses = findViewById(R.id.tv_expenses);
        tv_group = findViewById(R.id.tv_group);
        tv_person = findViewById(R.id.tv_person);
        iv_userprofile = findViewById(R.id.iv_userprofile);
        //tv_intent = findViewById(R.id.tv_intent);
        fab_visible = false;
    }

    public void requestGroups() {
        String user = getIntent().getStringExtra("profile");
        try {
            JSONObject root = new JSONObject(user);
            JSONObject us = root.getJSONObject("user");
            user = us.getString("username");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , user);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/getGroups").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                res = response.body().string();
                Log.v("response" , res+ "");
                //Handler handler = new Handler();
                int i;
                try {
                    JSONObject root = new JSONObject(res);
                    JSONArray group = root.getJSONArray("group");
                    for (i=0; i<group.length();i++){
                        JSONObject groupnames = group.getJSONObject(i);
                        String s = groupnames.getString("groupName");
                        int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        groupNames.add(s);
                                        arrayList.add(new Group(R.drawable.user , groupNames.get(finalI), "dsf"));
                                        adaptor.notifyDataSetChanged();
                                    }
                                });
                            }
                        },1000);


                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }

    public void createGroups(String group) {
        String user = getIntent().getStringExtra("profile");
        try {
            JSONObject root = new JSONObject(user);
            JSONObject us = root.getJSONObject("user");
            user = us.getString("username");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , user);
        jsonObject.addProperty("groupName" , group);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/group").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.v("request" , ""+response.body().string());
            }
        });
    }

    public void addPerson(String group) {
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , username);
        jsonObject.addProperty("groupName" , group);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/group").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.v("request" , ""+response.body().string());
            }
        });
    }

}