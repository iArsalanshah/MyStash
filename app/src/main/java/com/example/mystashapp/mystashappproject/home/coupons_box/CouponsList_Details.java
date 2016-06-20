package com.example.mystashapp.mystashappproject.home.coupons_box;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.get_all_coupons_pojo.Coupon;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.pojo.redeem_coupon.RedeemCoupon;
import com.example.mystashapp.mystashappproject.pojo.remindme_coupon.RemindMe;
import com.example.mystashapp.mystashappproject.pojo.to_save_coupon_pojo.ToSaveCoupon;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsList_Details extends AppCompatActivity {

    private Button btnRedeemNow;
    private ImageView imgView;
    private Button btnShare;
    private Button btnRemind;
    private Button btnSave;
    private ImageView imgBack;
    private TextView textViewCouponsName;
    private TextView textViewCouponsExp;
    private TextView textViewCouponsUSED;
    private TextView textViewCouponsDesc;
    private Coupon cObj;
    private Users cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_list__details);
        String objCoupon = getIntent().getStringExtra("couponObj");
        Log.d(Constant_util.LOG_TAG, objCoupon);
        cObj = new Gson().fromJson(objCoupon, Coupon.class);
        cid = CustomSharedPrefLogin.getUserObject(CouponsList_Details.this);
        init();
        clickEvents();
        settingData();
    }

    private void settingData() {
        textViewCouponsName.setText(cObj.getCouponName());
        textViewCouponsDesc.setText(cObj.getCouponDesc());
        textViewCouponsExp.append(cObj.getCouponExpdate());
        textViewCouponsUSED.append(cObj.getRedeemedCount() + "/" + cObj.getTotalCount());
        Picasso.with(this)
                .load(cObj.getImgurl())
                .error(R.drawable.placeholder_shadow)
                .placeholder(R.drawable.placeholder_shadow)
                .into(imgView);
    }

    private void clickEvents() {
        //ImageView Back Toolbar
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Buttton Share
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MyStash share.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        //Button Remind me
        btnRemind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = "Reminder of Coupon";

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date result = null;
                try {
                    result = df.parse(cObj.getCouponExpdate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(result);
                cal.add(Calendar.DATE, -1);
                result = cal.getTime();
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date date = null;
                try {
                    date = formatter.parse(result.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date);
                String formatedDate = cal2.get(Calendar.YEAR) + "-" + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DATE);
                Log.d(Constant_util.LOG_TAG, "" + formatedDate);
                Call<RemindMe> call = WebServicesFactory.getInstance().getRemindCoupon(Constant_util.ACTION_REMINDME_COUPON, cid.getId(), cObj.getId(), cObj.getUid(), formatedDate, msg);
                call.enqueue(new Callback<RemindMe>() {
                    @Override
                    public void onResponse(Call<RemindMe> call, Response<RemindMe> response) {
                        RemindMe remindMe = response.body();
                        if (remindMe.getHeader().getSuccess().equals("1")) {
                            Toast.makeText(CouponsList_Details.this, "Reminder Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CouponsList_Details.this, "Found Error:" + remindMe.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RemindMe> call, Throwable t) {
                        Toast.makeText(CouponsList_Details.this, "Network Error:", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Button Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CouponsList_Details.this)
                        .setCancelable(true)
                        .setTitle("Title")
                        .setMessage("Dialog Message")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<ToSaveCoupon> call = WebServicesFactory.getInstance().getToSaveCoupon(Constant_util.ACTION_SAVE_A_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
                                call.enqueue(new Callback<ToSaveCoupon>() {
                                    @Override
                                    public void onResponse(Call<ToSaveCoupon> call, Response<ToSaveCoupon> response) {
                                        ToSaveCoupon saveCoupon = response.body();
                                        if (saveCoupon.getHeader().getSuccess().equals("1")) {
                                            Toast.makeText(CouponsList_Details.this, "Coupon Saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(CouponsList_Details.this, "Failed: " + saveCoupon.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ToSaveCoupon> call, Throwable t) {
                                        Toast.makeText(CouponsList_Details.this, "Network Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        //Button Redeem Now
        btnRedeemNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CouponsList_Details.this)
                        .setCancelable(true)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to redeem this Coupon?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<RedeemCoupon> call = WebServicesFactory.getInstance().getRedeemCoupon(Constant_util.ACTION_REDEEM_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
                                call.enqueue(new Callback<RedeemCoupon>() {
                                    @Override
                                    public void onResponse(Call<RedeemCoupon> call, Response<RedeemCoupon> response) {
                                        RedeemCoupon redeemCoupon = response.body();
                                        if (redeemCoupon.getHeader().getSuccess().equals("1")) {
                                            Toast.makeText(CouponsList_Details.this, "Coupon Redeemed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(CouponsList_Details.this, "Can't Redeem Coupon: \n" + redeemCoupon.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RedeemCoupon> call, Throwable t) {
                                        Toast.makeText(CouponsList_Details.this, "Network Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
    }

    private void init() {
        btnRedeemNow = (Button) findViewById(R.id.button_redeemNow);
        btnShare = (Button) findViewById(R.id.btn_Share_coupons);
        btnRemind = (Button) findViewById(R.id.btn_RemindMe_coupons);
        btnSave = (Button) findViewById(R.id.btn_Save_coupons);
        imgView = (ImageView) findViewById(R.id.imageView_Coupons_Details);
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        textViewCouponsName = (TextView) findViewById(R.id.textView_Coupons_Details_Name);
        textViewCouponsExp = (TextView) findViewById(R.id.textView_Coupons_Details_expiry);
        textViewCouponsUSED = (TextView) findViewById(R.id.textView_Coupons_Details_usedDate);
        textViewCouponsDesc = (TextView) findViewById(R.id.textView_Coupons_Details_Desc);
    }
}
