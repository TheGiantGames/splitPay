package com.thegiantgames.splitpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

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
import com.thegiantgames.splitpay.ExpenseData;

public class TransactionAdaptor extends ArrayAdapter<Transaction> {

    OkHttpClient okHttpClient;
    Request request;
    int success;
    public  String USERNAME , GROUPNAME , MEMBERNAME , TRANSACTION ;
    public  ArrayList<Integer> check;
    public Boolean cc = false;
    public  Button SETTLE;
    ArrayList<Transaction> items;
    TransactionAdaptor adaptor;
    Context context;
    Transaction item;
    View currentItemView;
    public TransactionAdaptor(@NonNull Context context, ArrayList<Transaction> arrayList) {
        super(context, 0 , arrayList);
        this.items = arrayList;
        this.adaptor = this;
        this.context = context;

    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position , View convertView , ViewGroup parent){
        currentItemView = convertView;

        if (currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.transactions_layout, parent, false);
        }

        Transaction currentExpense = getItem(position);
        ImageView transactionImage = currentItemView.findViewById(R.id.iv_transaction);
        transactionImage.setImageResource(currentExpense.getImageId());


        TextView transactionName = currentItemView.findViewById(R.id.tv_name);
        transactionName.setText(currentExpense.getName());

        SETTLE = currentItemView.findViewById(R.id.btn_settle);
        SETTLE.setText(currentExpense.getSettle());
        if (check.get(position) == 1){
            SETTLE.setText("settled");
            SETTLE.setBackgroundColor(R.color.black);

        }
        //cc= false;

        SETTLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MEMBERNAME = currentExpense.getName();
                update();
                SETTLE.setText("settled");
                SETTLE.setBackgroundColor(R.color.black);
            }
        });

//        SETTLE.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//               // ExpenseData expenseData = new ExpenseData();
//               // expenseData.relode();
//                MEMBERNAME = currentExpense.getName();
//                Log.v("print" , USERNAME + "   " + GROUPNAME + "    " + MEMBERNAME +"     " + TRANSACTION);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setMessage("Are you sure?");
//                builder.setPositiveButton("Yes" , (DialogInterface.OnClickListener ) (dialog, which) ->{
//                    update();
//
//                        SETTLE.setBackgroundColor(R.color.black);
//                        SETTLE.setText("settled");
//
//                    dialog.cancel();
//                        });
//                builder.setNegativeButton("No" ,  (DialogInterface.OnClickListener ) (dialog, which) ->{
//
//                    dialog.cancel();
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//            }
//        });


        return currentItemView;

    }

    public void update() {

        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname" , USERNAME);
        jsonObject.addProperty("gname" , GROUPNAME);
        jsonObject.addProperty("member" , MEMBERNAME);
        jsonObject.addProperty("transaction" , TRANSACTION);
        // jsonObject.addProperty("money" , money);
        // jsonObject.addProperty("paidBy" , paidBy);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/update").put(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200){
                    success = 200;
                }


            }
        });
    }
}
