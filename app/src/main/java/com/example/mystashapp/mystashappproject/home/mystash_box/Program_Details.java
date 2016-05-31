package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.animation.AnimationFactory;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Searchnearby;
import com.example.mystashapp.mystashappproject.pojo.program_stamps.Datum;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Program_Details extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView img1, img2;
    private String pid;
    private Datum stampObject;
    private ImageView img_item;
    private TextView tvTitle_item;
    private TextView tvDesc_item;
    private TextView tvPhone_item;
    private TextView tvAvailableDays;
    private TextView tvTiming;
    private TextView tvRation;
    private ImageView imgBack;
    private TextView tvShare;
    private TextView tvTitle;
    private String programOtherDetails;
    private Searchnearby pOthersObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program__details);
        pid = getIntent().getStringExtra("pdata");
        stampObject = new Gson().fromJson(pid, Datum.class);
        programOtherDetails = getIntent().getStringExtra("programOtherDetails");
        pOthersObj = new Gson().fromJson(programOtherDetails, Searchnearby.class);
        init();
        clickEvents();
        settingData();

    }

    private void settingData() {

        //item objects
        Picasso.with(this)
                .load(pOthersObj.getLogourl())
                .error(R.drawable.placeholder_shadow)
                .placeholder(R.drawable.placeholder_shadow)
                .into(img_item);
        tvTitle_item.setText(pOthersObj.getName());
        tvDesc_item.setText(pOthersObj.getAddress());
        tvPhone_item.setText(pOthersObj.getContact());

        //image items
        Picasso.with(this)
                .load(pOthersObj.getLogourl())
                .error(R.drawable.placeholder_shadow)
                .placeholder(R.drawable.placeholder_shadow)
                .into(img1);

        tvTitle.setText(stampObject.getProgramname());
        //Splitting Days
        StringBuilder stringBuilder = new StringBuilder();
        String allDaysEvaluated = "";
        String[] daysStrings = stampObject.getDays().split(",");
        for (int i = 0; i < daysStrings.length; i++) {
            switch (i) {
                case 0:
                    if (daysStrings[i].equals("1"))
                        stringBuilder.append("Mon");
                    break;
                case 1:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1"))
                            stringBuilder.append(",Tue");
                        else
                            stringBuilder.append("Tue");
                    }
                    break;

                case 2:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1"))
                            stringBuilder.append(",Wed");
                        else
                            stringBuilder.append("Wed");
                    }
                    break;
                case 3:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                || daysStrings[i - 3].equals("1"))
                            stringBuilder.append(",Thu");
                        else
                            stringBuilder.append("Thu");
                    }
                    break;
                case 4:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1"))
                            stringBuilder.append(",Fri");
                        else
                            stringBuilder.append("Fri");
                    }
                    break;
                case 5:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1")
                                || daysStrings[i - 5].equals("1"))
                            stringBuilder.append(",Sat");
                        else
                            stringBuilder.append("Sat");
                    }
                    break;
                case 6:
                    if (daysStrings[i].equals("1")) {
                        if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1")
                                || daysStrings[i - 5].equals("1") || daysStrings[i - 6].equals("1"))
                            stringBuilder.append(",Sun");
                        else
                            stringBuilder.append("Sun");
                    }
                    break;
                case 7:
                    if (daysStrings[i].equals("1")) {
                        allDaysEvaluated = "All Week";
                    }
                    break;
                default:
                    Log.d(Constant_util.LOG_TAG, "settingData: ");
                    break;
            }
        }
        if (!allDaysEvaluated.equals("All Week")) {
            allDaysEvaluated = stringBuilder.toString();
        }
        tvAvailableDays.append(allDaysEvaluated);
        if (stampObject.getAllDay().equals("1")) {
            tvTiming.append("All Day");
        } else {
            tvTiming.append(stampObject.getStarttime() + "-" + stampObject.getEndtime());
        }
        tvRation.setText(stampObject.getStampcount() + "/" + stampObject.getTotalstamp());
    }

    private void clickEvents() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT); //http://genzeb.github.io/flip/
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvShare.setOnClickListener(new View.OnClickListener() {
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
    }

    private void init() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img_item = (ImageView) findViewById(R.id.img_programDetails_item);
        tvTitle_item = (TextView) findViewById(R.id.tv_programDetails_item_title);
        tvDesc_item = (TextView) findViewById(R.id.tv_programDetails_item_address);
        tvPhone_item = (TextView) findViewById(R.id.tv_programDetails_item_phone);
        tvRation = (TextView) findViewById(R.id.textRatio);
        tvAvailableDays = (TextView) findViewById(R.id.textAvailableDays);
        tvTiming = (TextView) findViewById(R.id.textTiming);
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        tvShare = (TextView) findViewById(R.id.tv_programDetails_share);
        tvTitle = (TextView) findViewById(R.id.tv_programDetails_Title);
    }
}
