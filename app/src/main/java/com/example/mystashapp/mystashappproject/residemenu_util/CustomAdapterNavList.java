package com.example.mystashapp.mystashappproject.residemenu_util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.MainActivity;

public class CustomAdapterNavList extends ArrayAdapter<String> {
    Activity context;
    String[] names;
    int[] imgs;

    public CustomAdapterNavList(MainActivity mainActivity, String[] listName, int[] listImages) {
        super(mainActivity, R.layout.row_navitems_listview, listName);
        context = mainActivity;
        names = listName;
        imgs = listImages;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_navitems_listview, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textViewNavListItem);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewNavListItem);
        txtTitle.setText(names[position]);
        imageView.setImageResource(imgs[position]);
        return rowView;
    }

}

