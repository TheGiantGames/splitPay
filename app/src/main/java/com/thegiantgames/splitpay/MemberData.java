package com.thegiantgames.splitpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MemberData extends AppCompatActivity {


    TextView textView ,tv_okay , tv_member_list , btn_split;
    FloatingActionButton fab_person , fab_expense;
    ArrayList<String> arr_members;
    ArrayList<Expense> expenseArrayList;
    String groupname , username , transaction , paidBy;
    int total;

    ListView lv_expense;
    EditText et_member , et_transaction , et_total;
    OkHttpClient okHttpClient;
    Request request;
    ExpensesArrayAdaptor expensesArrayAdaptor;
    Dialog dialog , member_list_dialog , expenses_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        fab_expense = findViewById(R.id.fab_expenses);
        fab_person = findViewById(R.id.fab_person);
        tv_member_list = findViewById(R.id.member_list);
        lv_expense = findViewById(R.id.lv_expenses);
        groupname = getIntent().getStringExtra("clickPosition");
        username = getIntent().getStringExtra("username");
        arr_members = new ArrayList<>();

        getTransaction();



        expenseArrayList = new ArrayList<>();
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
//        expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 ," party at d3" , "Paid by: vishal"));
        expensesArrayAdaptor = new ExpensesArrayAdaptor(MemberData.this , expenseArrayList);
        lv_expense.setAdapter(expensesArrayAdaptor);
        lv_expense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MemberData.this , ExpenseData.class);
               intent.putExtra("username" ,username);
               intent.putExtra("group" , groupname);
               intent.putExtra("transaction" ,expenseArrayList.get(position).getExpenseName() );
               startActivity(intent);
            }
        });
        expensesArrayAdaptor.notifyDataSetChanged();



        dialog = new Dialog(MemberData.this);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT , WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.member_name_layout);
       // dialog.setCancelable(false);
        tv_okay = dialog.findViewById(R.id.btn_person_dialog_okay);
        fab_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        tv_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_member = dialog.findViewById(R.id.et_personname_dialog);
                String member = et_member.getText().toString();
                addPerson(member);
                dialog.cancel();
            }
        });
        //textView = findViewById(R.id.test);


        member_list_dialog = new Dialog(MemberData.this);
        member_list_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT , WindowManager.LayoutParams.MATCH_PARENT);
        member_list_dialog.setContentView(R.layout.member_list_dialog_layout);
       // member_list_dialog.setCancelable(false);
        tv_member_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr_members.clear();
                getMember();
                ListView listView = member_list_dialog.findViewById(R.id.lv_member_list);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MemberData.this , android.R.layout.simple_list_item_1 , arr_members);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                member_list_dialog.show();
            }
        });




      expenses_dialog = new Dialog(MemberData.this);
      expenses_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT , WindowManager.LayoutParams.MATCH_PARENT);
      expenses_dialog.setContentView(R.layout.expense_dialog);
      et_transaction = expenses_dialog.findViewById(R.id.et_transaction);
      et_total = expenses_dialog.findViewById(R.id.et_total);
      btn_split = expenses_dialog.findViewById(R.id.btn_split);
      fab_expense.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              ListView listView = expenses_dialog.findViewById(R.id.member_in_group);
              ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MemberData.this , android.R.layout.simple_list_item_multiple_choice , arr_members);
              listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
              listView.setAdapter(arrayAdapter);
              ArrayList<String> checkedMember = new ArrayList<>();
              listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      AppCompatCheckedTextView checkbox = (AppCompatCheckedTextView) view;
                      if (checkbox.isChecked() && !checkedMember.contains(checkbox.getText().toString())){
                          checkedMember.add(checkbox.getText().toString());
                      }else  if(!checkbox.isChecked() && checkedMember.contains(checkbox.getText().toString())){
                          checkedMember.remove(checkbox.getText().toString());
                      }
                      String s = checkbox.getText().toString();
                     // Log.v("name" , "" + s);
                      Log.v("name" , "" + checkedMember);
                  }
              });


              ListView listView1 = expenses_dialog.findViewById(R.id.lv_paidBy);
              ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(MemberData.this , android.R.layout.simple_list_item_single_choice , arr_members);
              listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
              listView1.setAdapter(arrayAdapter1);
              listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      AppCompatCheckedTextView radio = (AppCompatCheckedTextView) view;
                      paidBy = radio.getText().toString();
                      Log.v("Radio " , " " + radio.getText().toString());

                  }
              });

              expenses_dialog.show();
              btn_split.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      transaction =  et_transaction.getText().toString();
                      total =  Integer.parseInt(et_total.getText().toString());
                      int perHead = total/checkedMember.size();
                      for (int i =0; i < checkedMember.size() ; i++){
                          if (checkedMember.get(i).equals(paidBy)){
                              addTransaction(checkedMember.get(i),perHead,1 );
                          }else {
                              addTransaction(checkedMember.get(i),perHead,0 );
                          }
                      }
                      Handler handler = new Handler();
                      handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              finish();
                              startActivity(getIntent());
                          }
                      },1000);
                      expenses_dialog.cancel();
                      //getTransaction();
                     // expensesArrayAdaptor.notifyDataSetChanged();
                  }
              });
          }
      });

    }

    public void addPerson(String member) {
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname" , username);
        jsonObject.addProperty("gname" , groupname);
        jsonObject.addProperty("mname" , member);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/addMember").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
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

    public void getMember(){
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname" , username);
        jsonObject.addProperty("gname" , groupname);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/getMember").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray member = root.getJSONArray("member");
                    for (int i = 0 ; i < member.length() ; i++){
                        JSONObject object = member.getJSONObject(i);
                        if (!arr_members.contains(object.getString("member"))){
                            arr_members.add(object.getString("member"));
                        }

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.v("request" , ""+arr_members);
            }
        });
    }

    public void addTransaction(String member, int money, int paidBy) {
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname" , username);
        jsonObject.addProperty("gname" , groupname);
        jsonObject.addProperty("member" , member);
        jsonObject.addProperty("transaction" , transaction);
        jsonObject.addProperty("money" , money);
        jsonObject.addProperty("paidBy" , paidBy);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/addTransaction").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
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

    public void getTransaction() {
        okHttpClient = new OkHttpClient();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname" , username);
        jsonObject.addProperty("gname" , groupname);
        //jsonObject.addProperty("member" , member);
        //jsonObject.addProperty("transaction" , transaction);
       // jsonObject.addProperty("money" , money);
       // jsonObject.addProperty("paidBy" , paidBy);
        okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON , jsonObject.toString());
        request = new Request.Builder().url("http://192.168.29.218:8000/getTransaction").post(requestBody).addHeader("content-type", "application/json; charset=utf-8").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    JSONObject  root = new JSONObject(response.body().string());
                    JSONArray members = root.getJSONArray("member");
                    for (int i =0 ; i < members.length() ; i++){
                        JSONObject object  = members.getJSONObject(i);
                        String trans = object.getString("transaction");
                        String pb = object.getString("member");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                expenseArrayList.add(new Expense(R.drawable.baseline_restaurant_menu_24 , trans, "Paid by: " + pb ));
                                expensesArrayAdaptor.notifyDataSetChanged();
                            }
                        });


                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

             //   Log.v("request" , ""+response.body().string());

            }
        });
    }

}