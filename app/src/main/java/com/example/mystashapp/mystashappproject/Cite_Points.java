package com.example.mystashapp.mystashappproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Cite_Points extends AppCompatActivity {
    ListView listView;
    TextView tvCPNumber;
    ArrayList<String> listDate, listBname, listPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cite_point);
        init();
        listPoints = new ArrayList<>();
        listBname = new ArrayList<>();
        listDate = new ArrayList<>();

        listPoints.add("1234");
        listPoints.add("3214");
        listPoints.add("56456");
        listPoints.add("1234");
        listPoints.add("3214");
        listPoints.add("56456");
        listPoints.add("1234");
        listPoints.add("3214");
        listPoints.add("56456");
        listDate.add("2001-12-20");
        listDate.add("2014-25-22");
        listDate.add("2016-23-01");
        listDate.add("2001-12-20");
        listDate.add("2014-25-22");
        listDate.add("2016-23-01");
        listDate.add("2001-12-20");
        listDate.add("2014-25-22");
        listDate.add("2016-23-01");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        listBname.add("River View");
        //List<CitePoints> citePoints = new List<CitePoints>;
        listView.setAdapter(new CitePointsAdapter(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView_citePointsHistory);
        tvCPNumber = (TextView) findViewById(R.id.textView_CitePointsNumber);
        tvCPNumber.setText("56209");
    }

    public void imgBack(View view) {
        finish();
    }

    private class CitePointsAdapter extends BaseAdapter {
        Activity context;
        private LayoutInflater inflater;
        //List<CitePoints> citePointses;

        public CitePointsAdapter(Cite_Points cite_points) {
            context = cite_points;
        }

        @Override
        public int getCount() {
            return listPoints.size();
        }

        @Override
        public Object getItem(int position) {
            return listPoints.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = inflater.inflate(R.layout.row_citepoints_listview, parent, false);

            TextView tvPoints = (TextView) convertView.findViewById(R.id.textView_row_citepoint);
            TextView tvBusinessName = (TextView) convertView.findViewById(R.id.textView_row_citepoint_businessName);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_row_citePoints_dateTime);

            String ext = " Points | ";
            tvPoints.setText(listPoints.get(position) + ext);
            tvBusinessName.setText(listBname.get(position));
            tvTime.setText(listDate.get(position));

            return convertView;
        }
    }
}
