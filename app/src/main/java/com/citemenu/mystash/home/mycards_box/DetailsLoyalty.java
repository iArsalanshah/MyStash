package com.citemenu.mystash.home.mycards_box;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.pojo.delete_loyalty_card.DeleteLoyaltyCard;
import com.citemenu.mystash.pojo.getmycards_pojo.Loyaltycard;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsLoyalty extends AppCompatActivity implements View.OnClickListener {
    public static boolean is_Edit = false;
    ViewPager viewPager;
    TextView etCard, etName, etBusiness, etDetails;
    InkPageIndicator inkPageIndicator;
    Button edit;
    private Loyaltycard convertedObjEdit;
    private String cardNumber, urName, cardName, cardNote, frontCard, backCard;
    private int[] image_resources = {R.drawable.placeholder_img_not_found, R.drawable.placeholder_img_not_found};
    //    private Getloyalty getloyalty;
    private ImageView imgChangeConfiguration;
    private ImageView deleteLoyaltyImg;
    private String loyaltyPosition;
    private String isRegistered;

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
            loyaltyPosition = getIntent().getStringExtra("loyaltyPosition");
            cardNote = getIntent().getStringExtra("cardNotes");
            frontCard = getIntent().getStringExtra("frontCard");
            backCard = getIntent().getStringExtra("backCard");
            isRegistered = getIntent().getStringExtra("isRegistered");
        }
//        barcodeImage = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("barcodeImage", "none");

        //
        //Extra Intent from Edit
        //
        String objectEdit = getIntent().getStringExtra("editLoyaltyObject");
        convertedObjEdit = new Gson().fromJson(objectEdit, Loyaltycard.class);

//        String addLoyaltyObj = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("", "addLoyaltyObject"); //todo id delete issue
//        getloyalty = new Gson().fromJson(addLoyaltyObj, Getloyalty.class);

        init();

        viewPager.setAdapter(new ViewPagerLayaltyAdapter(this));
        //page Indicator
        if (inkPageIndicator != null) {
            inkPageIndicator.setViewPager(viewPager);
        }
        settingData();
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_Loyalty);
        etCard = (EditText) findViewById(R.id.edittext_loyalty_cardNo);
        etName = (EditText) findViewById(R.id.edittext_loyalty_name);
        etBusiness = (EditText) findViewById(R.id.edittext_loyalty_business);
        etDetails = (EditText) findViewById(R.id.edittext_loyalty_details);
        edit = (Button) findViewById(R.id.button_loyalty_edit);
        inkPageIndicator = (InkPageIndicator) findViewById(R.id.ink_indicator_loyalty);
        imgChangeConfiguration = (ImageView) findViewById(R.id.imgChangeConfiguration);
        deleteLoyaltyImg = (ImageView) findViewById(R.id.deleteLoyaltyImg);
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
            imgChangeConfiguration.setOnClickListener(this);
            deleteLoyaltyImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailsLoyalty.this);
                    dialog.setTitle("Confirmation");
                    dialog.setMessage("Are you sure you want to remove this loyalty card");
                    dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (is_Edit)
                                onDeleteLoyaltyCard(convertedObjEdit.getId());
                            else onDeleteLoyaltyCard(loyaltyPosition);
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    private void onDeleteLoyaltyCard(final String position) {

        // webservice should be called here.
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Deleting...");
        dialog.show();
        Call<DeleteLoyaltyCard> call = WebServicesFactory.getInstance()
                .deleteLoyaltyCard(Constant_util.ACTION_DELETE_LOYALTY_CARD,
                        position);
        call.enqueue(new Callback<DeleteLoyaltyCard>() {
            @Override
            public void onResponse(Call<DeleteLoyaltyCard> call, Response<DeleteLoyaltyCard> response) {
                dialog.dismiss();
                DeleteLoyaltyCard deleteLoyaltyCard = response.body();

                if (deleteLoyaltyCard.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(DetailsLoyalty.this, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailsLoyalty.this, MyCards.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Toast.makeText(DetailsLoyalty.this, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteLoyaltyCard> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DetailsLoyalty.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void imgBack_LoyaltyDetails(View view) {
        startActivity(new Intent(DetailsLoyalty.this, MyCards.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void loyalty_details_img(View view) {
//        Toast.makeText(DetailsLoyalty.this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_loyalty_edit:
                Intent intent;
                SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
                editor.putBoolean("updateLoyaltyCard", true);
//                if (convertedObjEdit.getImageurl().length() > 0) {
                if (is_Edit) {
                    editor.putString("loyaltyID", convertedObjEdit.getId());
                    if (convertedObjEdit.getIsRegisterdCompany().equals("0")) {
                        intent = new Intent(DetailsLoyalty.this, CreateACard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        TakeLoyaltyNameDetails.is_Created = true;
                        intent.putExtra("frontCard", convertedObjEdit.getFrontimage());
                    } else {
                        editor.putString("frontImage", convertedObjEdit.getFrontimage());
                        intent = new Intent(DetailsLoyalty.this, TakeLoyaltyBarCode.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        TakeLoyaltyNameDetails.is_Created = false;
                    }
                    editor.putString("backCard", convertedObjEdit.getBackimage());
                    editor.putString("cardNumber", convertedObjEdit.getCardno());
                    editor.putString("cardUrName", convertedObjEdit.getCardname());
                    editor.putString("cardName", convertedObjEdit.getCarddetail());
                    editor.putString("cardNote", convertedObjEdit.getNotes()).apply();

                    intent.putExtra("comesFromDetail", true);
                    startActivity(intent);
                } else {
                    editor.putString("loyaltyID", loyaltyPosition);
                    if (isRegistered.equals("0")) {
                        intent = new Intent(DetailsLoyalty.this, CreateACard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        TakeLoyaltyNameDetails.is_Created = true;
                        intent.putExtra("frontCard", frontCard);
                    } else {
                        intent = new Intent(DetailsLoyalty.this, TakeLoyaltyBarCode.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        TakeLoyaltyNameDetails.is_Created = false;
                    }
                    editor.putString("cardNumber", cardNumber);
                    editor.putString("cardUrName", urName);
                    editor.putString("cardName", cardName);
                    editor.putString("cardNote", cardNote);
                    editor.putString("backCard", backCard).apply();
                    intent.putExtra("comesFromDetail", true);
                    startActivity(intent);
                }
//                } else {
//                    intent.putExtra("comesFromDetail", false);
//                    startActivity(intent);
//                }
                break;
            case R.id.imgChangeConfiguration:
//                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//                final int orientation = display.getOrientation();
//                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }
//                else {
//                    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DetailsLoyalty.this, MyCards.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                if (convertedObjEdit.getFrontimage() != null &&
                        convertedObjEdit.getBackimage() != null &&
                        !convertedObjEdit.getFrontimage().isEmpty() &&
                        !convertedObjEdit.getBackimage().isEmpty()) {
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
