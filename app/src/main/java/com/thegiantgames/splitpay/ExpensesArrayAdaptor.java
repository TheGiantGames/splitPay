package com.thegiantgames.splitpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ExpensesArrayAdaptor extends ArrayAdapter<Expense> {
    public ExpensesArrayAdaptor(@NonNull Context context, ArrayList<Expense> arrayList) {
        super(context, 0 ,arrayList);
    }


    public View getView(int position , View convertView , ViewGroup parent){
        View currentItemView = convertView;

        if (currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.group_listview_layout, parent, false);
        }

        Expense currentExpense = getItem(position);
        ImageView expenseImage = currentItemView.findViewById(R.id.group_image_lv);
        expenseImage.setImageResource(currentExpense.getImageView());


        TextView expenseName = currentItemView.findViewById(R.id.group_name_lv);
        expenseName.setText(currentExpense.getExpenseName());

        TextView paidBy = currentItemView.findViewById(R.id.owe_lv);
        paidBy.setText(currentExpense.getPaidBy());

        return currentItemView;

    }


}
