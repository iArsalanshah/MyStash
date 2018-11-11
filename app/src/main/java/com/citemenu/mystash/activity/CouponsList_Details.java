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
        progressDialog.setMessage(getString(R.string.please_wait));
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
                String fullMsg = getString(R.string.checkout_this_great_company) + cObj.getName() + getString(R.string.that_i_found);
                SelectShareIntent.selectIntent(CouponsList_Details.this, fullMsg);
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
                                    .setTitle(getString(R.string.message))
                                    .setMessage(getString(R.string.coupon_saved_success))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                        Toast.makeText(CouponsList_Details.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
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
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(getString(R.string.redeem_coupon_message))
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
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
                                                    .setTitle(getString(R.string.message))
                                                    .setMessage(getString(R.string.coupon_redemption_request_sent))
                                                    .setCancelable(false)
                                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(CouponsList_Details.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
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

                final String formatedDate = cal2.get(Calendar.YEAR) + "-"
                        + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DATE);
//                Log.d(Constant.LOG_TAG, "Reminder Data11: " + formatedDate);
                if (cDate != null && cDate.after(date)) {
//                    Log.d(Constant.LOG_TAG, "onClick: SUCCESS");
                    new AlertDialog.Builder(CouponsList_Details.this)
                            .setTitle(getString(R.string.message))
                            .setMessage(getString(R.string.coupon_expire_message))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(CouponsList_Details.this)
                            .setTitle(getString(R.string.message))
                            .setMessage(getString(R.string.notification_reminder_message))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                                                remindMeDialog(getString(R.string.message), getString(R.string.reminder_set_for_coupon));
                                            } else {
                                                Toast.makeText(CouponsList_Details.this, remindMe.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RemindMe> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(CouponsList_Details.this,
                                                    getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
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
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
