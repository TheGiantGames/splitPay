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

public class GroupArrayAdaptor extends ArrayAdapter<Group> {
    public GroupArrayAdaptor(@NonNull Context context, ArrayList<Group> arrayList) {
        super(context,0, arrayList);
    }


    public View getView(int position , View convertView , ViewGroup parent){
        View currentItemView = convertView;

        if (currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.group_listview_layout, parent, false);
        }

        Group currentGroup=getItem(position);
        ImageView groupImage = currentItemView.findViewById(R.id.group_image_lv);
        groupImage.setImageResource(currentGroup.getImageView());


        TextView groupName = currentItemView.findViewById(R.id.group_name_lv);
        groupName.setText(currentGroup.getGroupName());

        TextView owe = currentItemView.findViewById(R.id.owe_lv);
        owe.setText(currentGroup.getOwe());

        return currentItemView;

    }
}
