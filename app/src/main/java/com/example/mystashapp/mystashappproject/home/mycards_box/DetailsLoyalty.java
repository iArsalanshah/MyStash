package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.Getloyalty;
import com.example.mystashapp.mystashappproject.pojo.getmycards_pojo.Loyaltycard;
import com.google.gson.Gson;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.squareup.picasso.Picasso;

public class DetailsLoyalty extends AppCompatActivity implements View.OnClickListener {
    public static boolean is_Edit = false;
    ViewPager viewPager;
    TextView etCard, etName, etBusiness, etDetails;
    InkPageIndicator inkPageIndicator;
    Button edit;
    private Class<?> mClass;
    private Loyaltycard convertedObjEdit;
    private String cardNumber, urName, cardName, cardNote, frontCard, backCard;
    //String barcodeImage;
    private int[] image_resources = {R.drawable.ic_loyalty_card_front, R.drawable.ic_loyalty_card_bk};
    private Getloyalty getloyalty;
    private ImageView gotViewForConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_loyalty);

        //
        //Extra Intent from save loyalty
        //
        if (!is_Edit) {
            cardNumber = getIntent().getStringExtra("cardNumber");
            urName = getIntent().getStringExtra("urName");
            cardName = getIntent().getStringExtra("cardName");
            cardNote = getIntent().getStringExtra("cardNotes");
            frontCard = getIntent().getStringExtra("frontCard");
            backCard = getIntent().getStringExtra("backCard");
        }
//        barcodeImage = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("barcodeImage", "none");

        //
        //Extra Intent from Edit
        //
        String objectEdit = getIntent().getStringExtra("editLoyaltyObject");
        convertedObjEdit = new Gson().fromJson(objectEdit, Loyaltycard.class);

        String addLoyaltyObj = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("addLoyaltyObject", "");
        getloyalty = new Gson().fromJson(addLoyaltyObj, Getloyalty.class);

        init();

        viewPager.setAdapter(new ViewPagerLayaltyAdapter(this));
        //page Indicator
        if (inkPageIndicator != null) {
            inkPageIndicator.setViewPager(viewPager);
        }
        settingData();
    }

    private void settingDataForEdit() {

    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_Loyalty);
        etCard = (EditText) findViewById(R.id.edittext_loyalty_cardNo);
        etName = (EditText) findViewById(R.id.edittext_loyalty_name);
        etBusiness = (EditText) findViewById(R.id.edittext_loyalty_business);
        etDetails = (EditText) findViewById(R.id.edittext_loyalty_details);
        edit = (Button) findViewById(R.id.button_loyalty_edit);
        inkPageIndicator = (InkPageIndicator) findViewById(R.id.ink_indicator_loyalty);
    }

    private void settingData() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (is_Edit) {
                etBusiness.setText(convertedObjEdit.getCarddetail());
                etDetails.setText(convertedObjEdit.getNotes());
                etCard.setText(convertedObjEdit.getCardno());
                etName.setText(convertedObjEdit.getCardname());
            } else {
                etCard.setText(cardNumber);
                etName.setText(urName);
                etBusiness.setText(cardName);
                etDetails.setText(cardNote);
            }
            edit.setOnClickListener(this);
        }
    }

    public void imgBack_LoyaltyDetails(View view) {
        finish();
    }

    public void loyalty_details_img(View view) {
        Toast.makeText(DetailsLoyalty.this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_loyalty_edit:
                Intent intent = new Intent(DetailsLoyalty.this, CreateACard.class);
                if (getloyalty.getImageurl().length() > 0) {
                    if (is_Edit) {
                        intent.putExtra("frontCard", convertedObjEdit.getFrontimage());
                        intent.putExtra("comesFromDetail", true);
                        startActivity(intent);
                    } else {
                        intent.putExtra("frontCard", frontCard);
                        intent.putExtra("comesFromDetail", true);
                        startActivity(intent);
                    }
                } else {
                    intent.putExtra("comesFromDetail", false);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //TODO need to implement flow
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(DetailsLoyalty.this, "Landscape", Toast.LENGTH_SHORT).show();
////            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300,120);
////            viewPager.setLayoutParams(layoutParams);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(DetailsLoyalty.this, "Portrait", Toast.LENGTH_SHORT).show();
////            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////            viewPager.setLayoutParams(layoutParams);
//        }
//    }

    private class ViewPagerLayaltyAdapter extends PagerAdapter {
        Context context;
        String[] imgs = {"1", "2"};
        private LayoutInflater layoutInflater;

        public ViewPagerLayaltyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item_view = layoutInflater.inflate
                    (R.layout.swipe_layout_loyalty_details, container, false);
            ImageView imageView = (ImageView) item_view.findViewById(R.id.swipe_view_Images_loyalty);
            gotViewForConfig = imageView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            break;
                        default:
                            break;
                    }
                }
            });
            if (!is_Edit) {
                imgs[0] = frontCard;
                imgs[1] = backCard;
                Picasso.with(context)
                        .load(imgs[position])
                        .placeholder(R.drawable.placeholder_shadow)
                        .into(imageView);
            } else {
                if (convertedObjEdit.getFrontimage().length() > 0 && convertedObjEdit.getBackimage().length() > 0) {
                    imgs[0] = convertedObjEdit.getFrontimage();
                    imgs[1] = convertedObjEdit.getBackimage();
                    Picasso.with(context)
                            .load(imgs[position])
                            .placeholder(R.drawable.placeholder_shadow)
                            .into(imageView);
                } else imageView.setImageResource(image_resources[position]);

            }
            container.addView(item_view);
            return item_view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public int getCount() {
            return image_resources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
