package com.citemenu.mystash.activity.mystash_box;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.citemenu.mystash.R;
import com.citemenu.mystash.animation.AnimationFactory;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.program_stamps.Datum;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.utils.SelectShareIntent;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Program_Details extends AppCompatActivity {

    public static boolean activity = false;
    private ViewFlipper viewFlipper;
    private ImageView img1;//, img2;
    private BroadcastReceiver mMyBroadcastReceiver;
    private FrameLayout img2_layout;
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
    private com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby pOthersObj;
    private GridView gridView_img2;
    private TextView programTOC, programDsc;
    private String cid;
    private AlertDialog dialogP;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program__details);
        pid = getIntent().getStringExtra("pdata");
        stampObject = new Gson().fromJson(pid, Datum.class);
        programOtherDetails = getIntent().getStringExtra("programOtherDetails");
        pOthersObj = new Gson().fromJson(programOtherDetails, com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby.class);
        init();
        clickEvents();
        settingData();
        adapter = new ImageAdapter(this, stampObject, stampObject.getStampcount());
        gridView_img2.setAdapter(adapter);
        cid = CustomSharedPref.getUserObject(this).getId();
    }

    private void init() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        img1 = (ImageView) findViewById(R.id.img1);
//        img2 = (ImageView) findViewById(R.id.img2);
        img2_layout = (FrameLayout) findViewById(R.id.img2_layout);
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
        programDsc = (TextView) findViewById(R.id.programDsc);
        dialogP = new AlertDialog.Builder(this)
                .setMessage("Please wait...")
                .setCancelable(false).create();
    }

    private void settingData() {
        String mon = getString(R.string.day_Mon);
        String tue = getString(R.string.day_Thu);
        String wed = getString(R.string.day_Wed);
        String thu = getString(R.string.day_Thu);
        String fri = getString(R.string.day_Fri);
        String sat = getString(R.string.day_Sat);
        String sun = getString(R.string.day_Sun);
        String allWeek = getString(R.string.all_week);
        String allDay = getString(R.string.all_day);

        //item objects
        ImageUtil.setImageWithResource(this, img_item, pOthersObj.getLogourl());
//        if (pOthersObj.getLogourl() != null && !pOthersObj.getLogourl().isEmpty())
//            Picasso.with(this)
//                    .load(pOthersObj.getLogourl())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
//                    .into(img_item);
        if (pOthersObj.getName() != null)
            tvTitle_item.setText(pOthersObj.getName());
        if (pOthersObj.getAddress() != null)
            tvDesc_item.setText(pOthersObj.getAddress());
        if (pOthersObj.getContact() != null)
            tvPhone_item.setText(pOthersObj.getContact());

        //image items
        ImageUtil.setImageWithResource(this, img1, pOthersObj.getLogourl());
//        if (pOthersObj.getLogourl() != null && !pOthersObj.getLogourl().isEmpty())
//            Picasso.with(this)
//                    .load(pOthersObj.getLogourl())
//                    .error(R.drawable.placeholder_shadow)
//                    .placeholder(R.drawable.placeholder_shadow)
//                    .into(img1);

        if (stampObject.getProgramname() != null)
            tvTitle.setText(stampObject.getProgramname());
        //Splitting Days
        StringBuilder stringBuilder = new StringBuilder();
        String allDaysEvaluated = "";
        boolean allday = false;
        if (stampObject.getDays() != null) {

            String[] daysStrings = stampObject.getDays().split(",");
            for (int i = 0; i < daysStrings.length; i++) {
                if (allday) break;
                switch (i) {
                    case 0:
                        if (daysStrings[i].equals("1")) {
                            allDaysEvaluated = allWeek;
                            allday = true;
                        }
                        break;
                    case 1:
                        if (daysStrings[i].equals("1")) {
//                        if (daysStrings[i - 1].equals("1"))
//                            stringBuilder.append(",Tue");
//                        else
                            stringBuilder.append(mon);
                        }
                        break;

                    case 2:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1"))
                                stringBuilder.append("," + tue);
                            else
                                stringBuilder.append(tue);
                        }
                        break;
                    case 3:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1"))
                                stringBuilder.append("," + wed);
                            else
                                stringBuilder.append(wed);
                        }
                        break;
                    case 4:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                    || daysStrings[i - 3].equals("1"))
                                stringBuilder.append("," + thu);
                            else
                                stringBuilder.append(thu);
                        }
                        break;
                    case 5:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                    || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1"))
                                stringBuilder.append("," + fri);
                            else
                                stringBuilder.append(fri);
                        }
                        break;
                    case 6:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                    || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1")
                                    || daysStrings[i - 5].equals("1"))
                                stringBuilder.append("," + sat);
                            else
                                stringBuilder.append(sat);
                        }
                        break;
                    case 7:
                        if (daysStrings[i].equals("1")) {
                            if (daysStrings[i - 1].equals("1") || daysStrings[i - 2].equals("1")
                                    || daysStrings[i - 3].equals("1") || daysStrings[i - 4].equals("1")
                                    || daysStrings[i - 5].equals("1") || daysStrings[i - 6].equals("1"))
                                stringBuilder.append("," + sun);
                            else
                                stringBuilder.append(sun);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if (!allDaysEvaluated.equals(allWeek)) {
            allDaysEvaluated = stringBuilder.toString();
        }
        tvAvailableDays.append(allDaysEvaluated);
        if (stampObject.getAllDay() != null)
            if (stampObject.getAllDay().equals("1")) {
                tvTiming.append(allDay);
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
        if (stampObject.getStampcount() != null && stampObject.getTotalstamp() != null)
            tvRation.setText(stampObject.getStampcount() + "/" + stampObject.getTotalstamp());
        if (stampObject.getToc() != null && !stampObject.getToc().isEmpty()) {
            programTOC.setText(stampObject.getToc());
        }
        if (stampObject.getDesc() != null && !stampObject.getDesc().isEmpty()) {
            programDsc.setText(stampObject.getDesc());
        }
    }

    private void clickEvents() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.RIGHT_LEFT); //http://genzeb.github.io/flip/
            }
        });

        findViewById(R.id.img_gridview_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//@"Check out this great company i found on MyStash.\n%@\n%@\n%@\n%@\n\nhttp://www.mystashapp.ca/"
                String fullMsg = getString(R.string.share_message_program) + "\n"//Constant.SHARE_PROGRAM_STAMP_TEXT_START
                        + pOthersObj.getName() + "\n"
                        + stampObject.getProgramname() + "\n"
                        + pOthersObj.getAddress() + "\n"
                        + pOthersObj.getPostalcode() + "\n\n";
                SelectShareIntent.selectIntent(Program_Details.this, fullMsg);
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, com.citemenu.mystash.constant.Constant.SHARE_SUBJECT);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, com.citemenu.mystash.constant.Constant.SHARE_TEXT);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = true;
        mMyBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Here you can refresh your listview or other UI
                updateStamps();
            }
        };
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMyBroadcastReceiver, new IntentFilter("your_action"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStamps() {
        dialogP.show();
        Call<com.citemenu.mystash.pojo.stampCount.StampCountWebService> call = WebServicesFactory.getInstance().getStampCount(Constant.ACTION_GET_STAMPS_COUNT, cid, stampObject.getPid());
        call.enqueue(new Callback<com.citemenu.mystash.pojo.stampCount.StampCountWebService>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.stampCount.StampCountWebService> call, Response<com.citemenu.mystash.pojo.stampCount.StampCountWebService> response) {
                dialogP.dismiss();
                com.citemenu.mystash.pojo.stampCount.StampCountWebService resp = response.body();
                if (resp.getHeader().getSuccess().equals("1")) {
                    tvRation.setText(resp.getBody().getStampcount() + "/" + stampObject.getTotalstamp());
                    adapter = new ImageAdapter(Program_Details.this, stampObject, resp.getBody().getStampcount());
                    gridView_img2.setAdapter(adapter);
                } else {
                    Toast.makeText(Program_Details.this, resp.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.stampCount.StampCountWebService> call, Throwable t) {
                dialogP.dismiss();
                Toast.makeText(Program_Details.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyBroadcastReceiver);
    }

    private class ImageAdapter extends BaseAdapter {
        private final float px;
        Context context;
        int totalStamps;
        int filledStamps;
        int emptyStampImg = R.drawable.circle_stamp;
        int fillStampImg = R.drawable.circle_with_stamp;

        public ImageAdapter(Context context, Datum stampObject, String stampcount) {
            this.context = context;
            totalStamps = Integer.parseInt(stampObject.getTotalstamp());
            filledStamps = Integer.parseInt(stampcount);
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
