package com.example.mystashapp.mystashappproject.home.coupons_box;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.get_all_coupons_pojo.Coupon;
import com.example.mystashapp.mystashappproject.pojo.get_all_coupons_pojo.Get_All_Coupons;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coupons_Categories_Click extends AppCompatActivity {

    List<Coupon> couponObj;
    private ImageView imgBack;
    private ListView listView;
    private String catID;
    private TextView altTextCouponsSavedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons__categories__listview);
        catID = getIntent().getStringExtra("catID");
        init();
        clickEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCoupons();
        getSavedCoupons();
    }

    private void getSavedCoupons() {

    }

    private void getAllCoupons() {
        Users cid = CustomSharedPrefLogin.getUserObject(this);
        Log.d(Constant_util.LOG_TAG, cid.getId());
        Call<Get_All_Coupons> call = WebServicesFactory.getInstance().getAllCoupons(Constant_util.ACTION_GET_ALL_COUPONS_LIST, catID, cid.getId());
        call.enqueue(new Callback<Get_All_Coupons>() {
            @Override
            public void onResponse(Call<Get_All_Coupons> call, Response<Get_All_Coupons> response) {
                Get_All_Coupons allCoupons = response.body();
                if (allCoupons.getHeader().getSuccess().equals("1")) {
                    altTextCouponsSavedList.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new ListAdapter_Categorries(Coupons_Categories_Click.this, allCoupons.getBody().getCoupons()));
                } else {
                    altTextCouponsSavedList.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
//                    Toast.makeText(Coupons_Categories_Click.this, "Found: " + allCoupons.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Get_All_Coupons> call, Throwable t) {
                Toast.makeText(Coupons_Categories_Click.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Coupons_Categories_Click.this, CouponsList_Details.class);
                Coupon objCoupon = couponObj.get(position);
                intent.putExtra("couponObj", new Gson().toJson(objCoupon));
                startActivity(intent);
            }
        });
    }

    private void init() {
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        listView = (ListView) findViewById(R.id.listView_Categories_Coupons);
        altTextCouponsSavedList = (TextView) findViewById(R.id.altTextCouponsSavedList);
    }

    private class ListAdapter_Categorries extends BaseAdapter {
        private final List<Coupon> coupons;
        Context context;
        LayoutInflater layoutInflater;

        public ListAdapter_Categorries(Context context, List<Coupon> coupons) {
            this.context = context;
            this.coupons = coupons;
        }

        @Override
        public int getCount() {
            return coupons.size();
        }

        @Override
        public Object getItem(int position) {
            return coupons.get(position);
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
                convertView = layoutInflater.inflate(R.layout.row_categories_details_listview, parent, false);

            TextView listLabel = (TextView) convertView.findViewById(R.id.textView_row_list_coupons);
            ImageView listImage = (ImageView) convertView.findViewById(R.id.imageView_row_list_coupons);
            listLabel.setText(coupons.get(position).getCouponName());
            Picasso.with(context).load(coupons.get(position).getImgurl())
                    .placeholder(R.drawable.img_profile)
                    .error(R.drawable.img_profile)
                    .into(listImage);
            couponObj = coupons;
            return convertView;
        }
    }
}
