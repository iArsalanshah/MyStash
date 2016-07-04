package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Program_Details extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView img1;//, img2;
    //    private LinearLayout img2_layout;
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
    private GridView gridView_img2;
    private TextView programTOC;

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
        gridView_img2.setAdapter(new ImageAdapter(this, stampObject));
    }

    private void init() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        img1 = (ImageView) findViewById(R.id.img1);
//        img2 = (ImageView) findViewById(R.id.img2);
//        img2_layout = (LinearLayout) findViewById(R.id.img2_layout);
        gridView_img2 = (GridView) findViewById(R.id.gridView_img2);
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
        programTOC = (TextView) findViewById(R.id.programTOC);
    }

    private void settingData() {

        //item objects
        Picasso.with(this)
                .load(pOthersObj.getLogourl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
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
            //time formation
            SimpleDateFormat start = new SimpleDateFormat("HH:mm:ss", Locale.US);
            SimpleDateFormat end = new SimpleDateFormat("HH:mm:ss", Locale.US);
            Date t1 = null;
            Date t2 = null;
            try {
                t1 = start.parse(stampObject.getStarttime());
                t2 = end.parse(stampObject.getEndtime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            start.applyPattern("h:mm");
            end.applyPattern("h:mm a");
            //now we got appropriate format e.g 12:00 am
            String startTime = start.format(t1);
            String endTime = end.format(t2);
            tvTiming.append(startTime + " - " + endTime);
        }
        tvRation.setText(stampObject.getStampcount() + "/" + stampObject.getTotalstamp());
        if (!stampObject.getToc().isEmpty() && stampObject.getToc() != null) {
            programTOC.setText(stampObject.getToc());
        }
    }

    private void clickEvents() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.RIGHT_LEFT); //http://genzeb.github.io/flip/
            }
        });
//        img2_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
//            }
//        });
        gridView_img2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.RIGHT_LEFT);
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

    private class ImageAdapter extends BaseAdapter {
        private final float px;
        Context context;
        int totalStamps;
        int filledStamps;
        int emptyStampImg = R.drawable.circle_stamp;
        int fillStampImg = R.drawable.circle_with_stamp;

        public ImageAdapter(Context context, Datum stampObject) {
            this.context = context;
            totalStamps = Integer.parseInt(stampObject.getTotalstamp());
            filledStamps = Integer.parseInt(stampObject.getStampcount());
            Resources r = Resources.getSystem();
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 58, r.getDisplayMetrics());
        }

        @Override
        public int getCount() {
            return totalStamps;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(context);

                imageView.setLayoutParams(new GridView.LayoutParams((int) px, (int) px));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(16, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            if (position < filledStamps) {
                imageView.setImageResource(fillStampImg);
            } else {
                imageView.setImageResource(emptyStampImg);
            }
            return imageView;
        }
    }
}
