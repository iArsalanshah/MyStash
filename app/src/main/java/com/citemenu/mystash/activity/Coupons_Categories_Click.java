package com.citemenu.mystash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.get_all_coupons_pojo.Coupon;
import com.citemenu.mystash.pojo.get_all_coupons_pojo.Get_All_Coupons;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coupons_Categories_Click extends AppCompatActivity {

    List<Coupon> couponObj;
    private ImageView imgBack, imgSavedCoupons;
    private ListView listView;
    private String catID;
    private TextView altTextCouponsSavedList, titleSavedCoupon;
    private boolean isTextSavedClick;
    private boolean isCouponByAdmin;
    private Users cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons__categories__listview);
        catID = getIntent().getStringExtra("catID");
        init();
        clickEvents();
        isTextSavedClick = getIntent().getBooleanExtra("textSavedClick", false);
        isCouponByAdmin = getIntent().getBooleanExtra("couponByAdmin", false);
        cid = CustomSharedPref.getUserObject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTextSavedClick) {
            titleSavedCoupon.setText(getString(R.string.saved_coupons));
            imgSavedCoupons.setVisibility(View.GONE);
            getSavedCoupons();
        } else if (isCouponByAdmin) {
            String bid = getIntent().getStringExtra("adminIDforCoupon");
            try {
                getCouponsByAdmin(bid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            imgSavedCoupons.setVisibility(View.GONE);
        } else
            getAllCoupons();
    }

    private void getCouponsByAdmin(String bid) {
        Call<Get_All_Coupons> call = WebServicesFactory.getInstance().getCouponsByAdmin(Constant.ACTION_GET_COUPONS_BY_ADMIN, cid.getId(), bid);
        call.enqueue(new Callback<Get_All_Coupons>() {
            @Override
            public void onResponse(Call<Get_All_Coupons> call, Response<Get_All_Coupons> response) {
                Get_All_Coupons coupons = response.body();
                if (coupons.getHeader().getSuccess().equals("1")) {
                    listView.setAdapter(new ListAdapter_Categorries(Coupons_Categories_Click.this, coupons.getBody().getCoupons()));
                } else {
                    altTextCouponsSavedList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Get_All_Coupons> call, Throwable t) {
                Toast.makeText(Coupons_Categories_Click.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSavedCoupons() {
//        Log.d(Constant.LOG_TAG, cid.getId());
        Call<Get_All_Coupons> call = WebServicesFactory.getInstance().getSavedCoupons(Constant.ACTION_GET_MY_SAVED_COUPONS, cid.getId());
        call.enqueue(new Callback<Get_All_Coupons>() {
            @Override
            public void onResponse(Call<Get_All_Coupons> call, Response<Get_All_Coupons> response) {
                Get_All_Coupons savedCoupons = response.body();
                if (savedCoupons.getHeader().getSuccess().equals("1")) {
                    altTextCouponsSavedList.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new ListAdapter_Categorries(Coupons_Categories_Click.this, savedCoupons.getBody().getCoupons()));
                } else {
                    altTextCouponsSavedList.setText(getResources().getString(R.string.savedCouponsAltText));
                    altTextCouponsSavedList.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Get_All_Coupons> call, Throwable t) {
                Toast.makeText(Coupons_Categories_Click.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllCoupons() {
        Users cid = CustomSharedPref.getUserObject(this);
//        Log.d(Constant.LOG_TAG, cid.getId());
        Call<Get_All_Coupons> call = WebServicesFactory.getInstance().getAllCoupons(Constant.ACTION_GET_ALL_COUPONS_LIST, catID, cid.getId());
        call.enqueue(new Callback<Get_All_Coupons>() {
            @Override
            public void onResponse(Call<Get_All_Coupons> call, Response<Get_All_Coupons> response) {
                Get_All_Coupons allCoupons = response.body();
                if (allCoupons == null) {

                } else if (allCoupons.getHeader().getSuccess().equals("1")) {
                    altTextCouponsSavedList.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new ListAdapter_Categorries(Coupons_Categories_Click.this, allCoupons.getBody().getCoupons()));
                } else {
                    listView.setVisibility(View.GONE);
                    altTextCouponsSavedList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Get_All_Coupons> call, Throwable t) {
                Toast.makeText(Coupons_Categories_Click.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        imgSavedCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupons_Categories_Click.this, Coupons_Categories_Click.class);
                intent.putExtra("textSavedClick", true);
                startActivity(intent);
            }
        });
    }

    private void init() {
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        imgSavedCoupons = (ImageView) findViewById(R.id.img_Saved_Coupons);
        listView = (ListView) findViewById(R.id.listView_Categories_Coupons);
        altTextCouponsSavedList = (TextView) findViewById(R.id.altTextCouponsSavedList);
        titleSavedCoupon = (TextView) findViewById(R.id.titleSavedCoupon);
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
            TextView tv_item_details = (TextView) convertView.findViewById(R.id.tv_item_details);
            ImageView listImage = (ImageView) convertView.findViewById(R.id.imageView_row_list_coupons);
            ImageUtil.setImageWithResource(context, listImage, coupons.get(position).getImgurl());
            if (isCouponByAdmin) {
                if (coupons.get(position).getCouponName() != null)
                    listLabel.setText(coupons.get(position).getCouponName());
                if (coupons.get(position).getCouponDesc() != null)
                    tv_item_details.setText(coupons.get(position).getCouponDesc());
//                if (coupons.get(position).getImgurl() != null && !coupons.get(position).getImgurl().isEmpty())
//                    Picasso.with(context).load(coupons.get(position).getImgurl())
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.placeholder)
//                            .into(listImage);
            } else {
                if (coupons.get(position).getName() != null)
                    listLabel.setText(coupons.get(position).getName());
                if (coupons.get(position).getCouponName() != null)
                    tv_item_details.setText(coupons.get(position).getCouponName());
//                if (coupons.get(position).getImgurl() != null && !coupons.get(position).getImgurl().isEmpty())
//                    Picasso.with(context).load(coupons.get(position).getImgurl())
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.placeholder)
//                            .into(listImage);
            }

            couponObj = coupons;
            return convertView;
        }
    }
}
