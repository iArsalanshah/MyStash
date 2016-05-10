package com.example.mystashapp.mystashappproject.home.coupons_box;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mystashapp.mystashappproject.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_list__details);
        init();
        clickEvents();
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
                new AlertDialog.Builder(CouponsList_Details.this)
                        .setCancelable(true)
                        .setTitle("Title")
                        .setMessage("Dialog Message")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        //Button Remind me
        btnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CouponsList_Details.this)
                        .setCancelable(true)
                        .setTitle("Title")
                        .setMessage("Dialog Message")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Remind", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
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

                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
                        .setMessage("Are you sure you want to redeem this Coupons?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
