package com.thegiantgames.splitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.thegiantgames.splitpay.TransactionAdaptor;

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

public class ExpenseData extends AppCompatActivity {

    OkHttpClient okHttpClient;
    Request request;
    TransactionAdaptor transactionAdaptor;
    ArrayList<Transaction> arrayList;
    ArrayList<Integer> set;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_data);

        set = new ArrayList<>();

        //Button  button = findViewById(R.id.btn_settle);
        //button.setText("hello");
        arrayList = new ArrayList<Transaction>();
        transactionAdaptor = new TransactionAdaptor(ExpenseData.this , arrayList);

        transactionAdaptor.USERNAME = getIntent().getStringExtra("username");
        transactionAdaptor.GROUPNAME = getIntent().getStringExtra("group");
        transactionAdaptor.TRANSACTION = getIntent().getStringExtra("transaction");
        SharedPreferences.Editor editor = getSharedPreferences("user" , MODE_PRIVATE).edit();
        editor.putString("uu" ,transactionAdaptor.USERNAME);
        editor.putString("gg" , transactionAdaptor.GROUPNAME);
        editor.putString("tt" , transactionAdaptor.TRANSACTION);
        editor.apply();
        ListView listView = findViewById(R.id.lv_transaction);

        getExpense();
        transactionAdaptor.notifyDataSetChanged();
        //transactionAdaptor.notifyDataSetChanged();


//        arrayList.add(new Transaction("Vishal" , R.drawable.baseline_person_4_24 , "settle"));
//        arrayList.add(new Transaction("abhi" , R.drawable.baseline_person_4_24 , "settle"));
//        arrayList.add(new Transaction("chi" , R.drawable.baseline_person_4_24 , "settle"));
//        arrayList.add(new Transaction("ai" , R.drawable.baseline_person_4_24 , "settle"));
//        arrayList.add(new Transaction("ri" , R.drawable.baseline_person_4_24 , "settle"));


        listView.setAdapter(transactionAdaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String s = arrayList.get(position).getName();
               // Log.v("hello" , s + " printed");
            }
        });


    }

    public void getExpense() {
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        SharedPreferences preferences = getSharedPreferences("user" , MODE_PRIVATE);
        String uu = preferences.getString("uu" ,transactionAdaptor.USERNAME );
        String gg = preferences.getString("gg" ,transactionAdaptor.GROUPNAME );
        String tt = preferences.getString("tt" ,transactionAdaptor.TRANSACTION );
        jsonObject.addProperty("uname" ,uu);
        jsonObject.addProperty("gname" ,gg);
        //jsonObject.addProperty("member" , member);
        jsonObject.addProperty("transaction" , tt);
        // jsonObject.addProperty("money" , money);
        // jsonObject.addProperty("paidBy" , paidBy);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/getExpense").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String s = response.body().string();
                Log.v("response" , s + " ");
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray members = root.getJSONArray("member");

                    for (int i =0 ; i < members.length() ; i++){
                        JSONObject object  = members.getJSONObject(i);
                        String pb = object.getString("member");

                         set.add(object.getInt("settle"));
                        runOnUiThread(new Runnable() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void run() {
                                arrayList.add(new Transaction( pb ,R.drawable.baseline_person_4_24 , "settle" ));
                                transactionAdaptor.check = set;
                                transactionAdaptor.notifyDataSetChanged();
//                                if (set == 1){
//                                    transactionAdaptor.SETTLE.setText("settled");
//                                    transactionAdaptor.SETTLE.setBackgroundColor(R.color.black);
//                                }else {
//                                    transactionAdaptor.SETTLE.setText("settle");
//                                }
                                transactionAdaptor.notifyDataSetChanged();
                            }
                        });


                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

//                //   Log.v("request" , ""+response.body().string());

            }
        });
    }

//public  void relode(){
//    Handler handler = new Handler();
//    handler.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//            finish();
//            startActivity(getIntent());
//        }
//    },1000);
//}

}