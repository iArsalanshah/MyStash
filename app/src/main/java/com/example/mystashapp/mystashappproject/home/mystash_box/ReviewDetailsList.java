package com.example.mystashapp.mystashappproject.home.mystash_box;

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

import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Review;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Searchnearby;
import com.google.gson.Gson;

import java.util.List;

public class ReviewDetailsList extends AppCompatActivity {
    ListView listView;
    ReviewListAdapter adapter;
    private Searchnearby jsonObject;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_details_list_activity);

        String s = getIntent().getStringExtra("key");
        jsonObject = new Gson().fromJson(s, Searchnearby.class);
        reviews = jsonObject.getReviews();

        //Listview
        adapter = new ReviewListAdapter(this, reviews);
        listView = (ListView) findViewById(R.id.listview_ReviewDetails);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void backImage_mystashReviews(View view) {
        finish();
    }

    private class ReviewListAdapter extends BaseAdapter {
        Activity context;
        List<Review> review;
        private LayoutInflater inflater;

        public ReviewListAdapter(ReviewDetailsList reviewDetailsList, List<Review> reviews) {
            context = reviewDetailsList;
            this.review = reviews;
        }

        @Override
        public int getCount() {
            return review.size();
        }

        @Override
        public Object getItem(int position) {
            return review.get(position);
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
                convertView = inflater.inflate(R.layout.row_review_details_listview, parent, false);

            Review r = review.get(position);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.textView_Review_Title);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.textView_Review_Desc);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_Review_dateTime);
            tvTitle.setText(r.getCustomerName());
            tvDesc.setText(r.getReview().trim());
            tvTime.setText(r.getDateTime());
            return convertView;
        }
    }
}
