package com.example.mystashapp.mystashappproject.home.coupons_box;

import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_list__details);
        String objCoupon = getIntent().getStringExtra("couponObj");
//        Log.d(Constant_util.LOG_TAG, objCoupon);
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
                .error(R.drawable.placeholder_img_not_found)
                .placeholder(R.drawable.placeholder_shadow)
                .into(imgView);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
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
        progressDialog = new ProgressDialog(CouponsList_Details.this);
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Who does'nt like rewards? With the STASH APP, you can get rewards at all of your favorite retailers when you shop. Download it today for So many great deals and offers.\n" +
                        "www.mystashapp.com");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Stash App");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        //Button Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<ToSaveCoupon> call = WebServicesFactory.getInstance().getToSaveCoupon(Constant_util.ACTION_SAVE_A_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
                call.enqueue(new Callback<ToSaveCoupon>() {
                    @Override
                    public void onResponse(Call<ToSaveCoupon> call, Response<ToSaveCoupon> response) {
                        progressDialog.dismiss();
                        ToSaveCoupon saveCoupon = response.body();
                        if (saveCoupon.getHeader().getSuccess().equals("1")) {
                            new AlertDialog.Builder(CouponsList_Details.this)
                                    .setTitle("Message")
                                    .setMessage("Coupon saved successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(CouponsList_Details.this, saveCoupon.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ToSaveCoupon> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(CouponsList_Details.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
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
                                progressDialog.show();
                                Call<RedeemCoupon> call = WebServicesFactory.getInstance().getRedeemCoupon(Constant_util.ACTION_REDEEM_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
                                call.enqueue(new Callback<RedeemCoupon>() {
                                    @Override
                                    public void onResponse(Call<RedeemCoupon> call, Response<RedeemCoupon> response) {
                                        progressDialog.dismiss();
                                        RedeemCoupon redeemCoupon = response.body();
                                        if (redeemCoupon.getHeader().getSuccess().equals("1")) {
                                            new AlertDialog.Builder(CouponsList_Details.this)
                                                    .setTitle("Message")
                                                    .setMessage("Coupon redeemed successfully")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            finish();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Toast.makeText(CouponsList_Details.this, redeemCoupon.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RedeemCoupon> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CouponsList_Details.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        //Button Remind me
        btnRemind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String msg = "Reminder of Coupon";

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Log.d(Constant_util.LOG_TAG, "Reminder Data1: " + df);

                Date result = null;
                Date soonResult = null;
                Date cDate = null;
                try {
                    result = df.parse(cObj.getCouponExpdate());
                    Log.d(Constant_util.LOG_TAG, "Reminder Data2: " + result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                Log.d(Constant_util.LOG_TAG, "Reminder Data3: " + cal);

                cal.setTime(result);
                Log.d(Constant_util.LOG_TAG, "Reminder Data4: " + cal);

                cal.add(Calendar.DATE, -3);
                Log.d(Constant_util.LOG_TAG, "Reminder Data5: " + cal);

                result = cal.getTime();
                soonResult = result;
                Log.d(Constant_util.LOG_TAG, "Reminder Data6: " + result);
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                Log.d(Constant_util.LOG_TAG, "Reminder Data7: " + formatter);

                Calendar cal2 = Calendar.getInstance();
                Log.d(Constant_util.LOG_TAG, "Reminder Data9: " + cal2);
                cDate = cal2.getTime();
                Date date = null;
                try {
                    date = formatter.parse(result.toString());
                    Log.d(Constant_util.LOG_TAG, "Reminder Data8: " + date + " " + cDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cal2.setTime(date);
                Log.d(Constant_util.LOG_TAG, "Reminder Data10: " + cal2);

                final String formatedDate = cal2.get(Calendar.YEAR) + "-" + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DATE);
                Log.d(Constant_util.LOG_TAG, "Reminder Data11: " + formatedDate);
                if (cDate != null && cDate.after(date)) {
                    Log.d(Constant_util.LOG_TAG, "onClick: SUCCESS");
                    new AlertDialog.Builder(CouponsList_Details.this)
                            .setTitle("Message")
                            .setMessage("This coupon will be expire Soon")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(CouponsList_Details.this)
                            .setTitle("Message")
                            .setMessage("You will be notified, 03 days before this coupon expires")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.show();
                                    Call<RemindMe> call = WebServicesFactory.getInstance().getRemindCoupon(Constant_util.ACTION_REMINDME_COUPON, cid.getId(), cObj.getId(), cObj.getUid(), formatedDate, msg);
                                    call.enqueue(new Callback<RemindMe>() {
                                        @Override
                                        public void onResponse(Call<RemindMe> call, Response<RemindMe> response) {
                                            progressDialog.dismiss();
                                            RemindMe remindMe = response.body();
                                            if (remindMe.getHeader().getSuccess().equals("1")) {
                                                remindMeDialog("Message", "Reminder has been set for coupon");
                                            } else {
                                                Toast.makeText(CouponsList_Details.this, remindMe.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RemindMe> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(CouponsList_Details.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).show();
                }
            }
        });
    }

    public void remindMeDialog(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
