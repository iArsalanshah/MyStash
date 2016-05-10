package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
        tvAvailableDays.append(stampObject.getDays());
        tvTiming.append(stampObject.getAllDay());
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
