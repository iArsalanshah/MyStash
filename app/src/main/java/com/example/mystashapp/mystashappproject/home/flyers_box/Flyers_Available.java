package com.example.mystashapp.mystashappproject.home.flyers_box;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.R;

import java.util.ArrayList;

public class Flyers_Available extends AppCompatActivity {

    private GridView gridView;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyers__available);
        init();
        gridView.setAdapter(new GridCustomAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Flyers_Available.this, "" + position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Flyers_Available.this, Flyers_pdfview.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.gridView);
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
    }

    private class GridCustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        int[] imgResources = {R.drawable.food_1, R.drawable.food_2, R.drawable.food_3, R.drawable.food_2, R.drawable.food_1};
        ArrayList<String> testString1 = new ArrayList<>();
        ArrayList<String> testString2 = new ArrayList<>();

        public GridCustomAdapter(Context context) {
            this.context = context;
            testString1.add("1 days left");
            testString1.add("13 days left");
            testString1.add("90 days left");
            testString1.add("50 days left");
            testString1.add("134 days left");
            testString2.add("Flyer");
            testString2.add("Test Flyer");
            testString2.add("Flyer");
            testString2.add("Flyer");
            testString2.add("Flyer");
        }

        @Override
        public int getCount() {
            return testString1.size();
        }

        @Override
        public Object getItem(int position) {
            return testString1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.row_flyers_gridview, parent, false);
            TextView Label1 = (TextView) convertView.findViewById(R.id.testView_Flyer_row1);
            TextView Label2 = (TextView) convertView.findViewById(R.id.testView_Flyer_row2);
            ImageView listImage = (ImageView) convertView.findViewById(R.id.image1_flyer_row);
            Label1.setText(testString1.get(position));
            Label2.setText(testString2.get(position));
            listImage.setImageResource(imgResources[position]);
            return convertView;
        }
    }
}
