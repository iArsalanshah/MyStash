package com.citemenu.mystash.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.get_all_coupons_pojo.Coupon;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.redeem_coupon.RedeemCoupon;
import com.citemenu.mystash.pojo.remindme_coupon.RemindMe;
import com.citemenu.mystash.pojo.to_save_coupon_pojo.ToSaveCoupon;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.utils.SelectShareIntent;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

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
//        Log.d(Constant.LOG_TAG, objCoupon);
        cObj = new Gson().fromJson(objCoupon, Coupon.class);
        cid = CustomSharedPref.getUserObject(CouponsList_Details.this);
        init();
        clickEvents();
        settingData();
    }

    private void settingData() {
        if (cObj.getCouponName() != null)
            textViewCouponsName.setText(cObj.getCouponName());
        if (cObj.getCouponDesc() != null)
            textViewCouponsDesc.append(cObj.getCouponDesc());
        if (cObj.getCouponExpdate() != null)
            textViewCouponsExp.append(cObj.getCouponExpdate());
        if (cObj.getTotalCount() != null && cObj.getRedeemedCount() != null)
            textViewCouponsUSED.append(cObj.getRedeemedCount() + "/" + cObj.getTotalCount());
        ImageUtil.setImageWithResource(this, imgView, cObj.getImgurl());
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
                SelectShareIntent.selectIntent(CouponsList_Details.this,
                        Constant.SHARE_PROGRAM_STAMP_TEXT_START
                                + cObj.getName());
            }
        });

        //Button Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<ToSaveCoupon> call = WebServicesFactory.getInstance().getToSaveCoupon(Constant.ACTION_SAVE_A_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
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
                        Toast.makeText(CouponsList_Details.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
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
                                Call<RedeemCoupon> call = WebServicesFactory.getInstance().getRedeemCoupon(Constant.ACTION_REDEEM_COUPON, cObj.getUid(), cid.getId(), cObj.getId());
                                call.enqueue(new Callback<RedeemCoupon>() {
                                    @Override
                                    public void onResponse(Call<RedeemCoupon> call, Response<RedeemCoupon> response) {
                                        progressDialog.dismiss();
                                        RedeemCoupon redeemCoupon = response.body();
                                        if (redeemCoupon.getHeader().getSuccess().equals("1")) {
                                            new AlertDialog.Builder(CouponsList_Details.this)
                                                    .setTitle("Message")
                                                    .setMessage("Redemption request has been sent to admin.")
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
                                        Toast.makeText(CouponsList_Details.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
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
//                Log.d(Constant.LOG_TAG, "Reminder Data1: " + df);

                Date result = null;
                Date soonResult = null;
                Date cDate = null;
                try {
                    result = df.parse(cObj.getCouponExpdate());
//                    Log.d(Constant.LOG_TAG, "Reminder Data2: " + result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
//                Log.d(Constant.LOG_TAG, "Reminder Data3: " + cal);

                cal.setTime(result);
//                Log.d(Constant.LOG_TAG, "Reminder Data4: " + cal);

                cal.add(Calendar.DATE, -3);
//                Log.d(Constant.LOG_TAG, "Reminder Data5: " + cal);

                result = cal.getTime();
                soonResult = result;
//                Log.d(Constant.LOG_TAG, "Reminder Data6: " + result);
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
//                Log.d(Constant.LOG_TAG, "Reminder Data7: " + formatter);

                Calendar cal2 = Calendar.getInstance();
//                Log.d(Constant.LOG_TAG, "Reminder Data9: " + cal2);
                cDate = cal2.getTime();
                Date date = null;
                try {
                    date = formatter.parse(result.toString());
//                    Log.d(Constant.LOG_TAG, "Reminder Data8: " + date + " " + cDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cal2.setTime(date);
//                Log.d(Constant.LOG_TAG, "Reminder Data10: " + cal2);

                final String formatedDate = cal2.get(Calendar.YEAR) + "-" + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DATE);
//                Log.d(Constant.LOG_TAG, "Reminder Data11: " + formatedDate);
                if (cDate != null && cDate.after(date)) {
//                    Log.d(Constant.LOG_TAG, "onClick: SUCCESS");
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
                                    Call<RemindMe> call = WebServicesFactory.getInstance().getRemindCoupon(Constant.ACTION_REMINDME_COUPON, cid.getId(), cObj.getId(), cObj.getUid(), formatedDate, msg);
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
                                            Toast.makeText(CouponsList_Details.this,
                                                    "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
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

//    //Share on Facebook
//    private void facebookShare() {
//        //todo need to update Content
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentTitle(Constant.SHARE_SUBJECT)
//                    .setContentDescription(Constant.SHARE_TEXT)
//                    .setContentUrl(Uri.parse("http://mystash.ca/openurl.php"))
//                    .build();
//            shareDialog.show(linkContent);
//        }
//    }
}
