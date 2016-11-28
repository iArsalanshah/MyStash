package com.citemenu.mystash.home.mystash_box;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.home.businessMapActivity;
import com.citemenu.mystash.home.coupons_box.Coupons_Categories_Click;
import com.citemenu.mystash.home.mystash_box.viewpager_adapter.ViewPagerAdapter;
import com.citemenu.mystash.pojo.add_stash.AddStash;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby;
import com.citemenu.mystash.pojo.remove_stash.RemoveStash;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.SelectShareIntent;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDetails_MyStash extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private RelativeLayout rootLayout;
    private ImageView imgPrograms, imgCoupons, imgShare, imgWrite, imgRemove, imgAdd, topbarImageBack;
    private TextView tvName, tvAddress, tvCity, tvMobile, tvDesc, tvReviews;
    private RatingBar rating;
    private String s;
    private Searchnearby gsonBusiness;
    private JSONObject obj;
    private Users users_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details__mystash);

        //Getting Intent StringExtra
        s = getIntent().getStringExtra("id");
        users_obj = CustomSharedPref.getUserObject(this);

        //String to Json to check Reviews.has method
        try {
            obj = new JSONObject(s);
//            Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, s);
        } catch (Throwable t) {
//            Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "Could not parse malformed JSON: \"" + s + "\"");
        }

        //Json String to Object form
        gsonBusiness = new Gson().fromJson(s, Searchnearby.class);

        //Initialization of Views
        init();

        //settings values for views
        settingData(gsonBusiness);
        clickEvents(gsonBusiness);
        adapter = new ViewPagerAdapter(this, gsonBusiness);
        viewPager.setAdapter(adapter);
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.ink_indicator);
        try {
            if (inkPageIndicator != null) {
                inkPageIndicator.setViewPager(viewPager);
            }
        } catch (NegativeArraySizeException ex) {
            ex.printStackTrace();
        }
    }

    private void clickEvents(final Searchnearby searchnearby) {
        imgPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDetails_MyStash.this, ProgramsList.class);
                i.putExtra("programID", new Gson().toJson(searchnearby));
                startActivity(i);
            }
        });
        imgCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDetails_MyStash.this, Coupons_Categories_Click.class);
                intent.putExtra("adminIDforCoupon", searchnearby.getId());
                intent.putExtra("couponByAdmin", true);
                startActivity(intent);
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullMsg = Constant_util.SHARE_BUSINESS_START + searchnearby.getName();
                SelectShareIntent.selectIntent(ListDetails_MyStash.this, fullMsg);
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, com.citemenu.mystash.helper.Constant_util.SHARE_SUBJECT);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, com.citemenu.mystash.helper.Constant_util.SHARE_TEXT);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
            }
        });

        imgWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(ListDetails_MyStash.this, RateAndReview.class);
                writeIntent.putExtra("writeUid", searchnearby.getUid());
                writeIntent.putExtra("writeCid", users_obj.getId());
                writeIntent.putExtra("writeBid", searchnearby.getId());
                startActivity(writeIntent);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStash(searchnearby);
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ListDetails_MyStash.this)
                        .setMessage("Are you sure you want to remove this\n" +
                                "business from your stash, doing so will\n" +
                                "prevent you from receiving specials\n" +
                                "and VIP offers from this merchant.")
                        .setTitle("Confirmation")
                        .setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeStash(searchnearby);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    public void removeStash(Searchnearby searchnearby) {
        Call<RemoveStash> call = WebServicesFactory.getInstance().getRemoveStash(Constant_util.ACTION_REMOVE_STASH, searchnearby.getId(), users_obj.getId());
        call.enqueue(new Callback<RemoveStash>() {
            @Override
            public void onResponse(Call<RemoveStash> call, Response<RemoveStash> response) {
                RemoveStash stash = response.body();
                if (stash == null) {
                    Toast.makeText(ListDetails_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                } else if (stash.getHeader().getSuccess().equals("1")) {
                    imgRemove.setVisibility(View.GONE);
                    imgAdd.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ListDetails_MyStash.this, stash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RemoveStash> call, Throwable t) {
                Toast.makeText(ListDetails_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addStash(Searchnearby searchnearby) {
        Call<AddStash> call = WebServicesFactory.getInstance().getAddStash(Constant_util.ACTION_ADD_STASH, searchnearby.getId(), users_obj.getId());
        call.enqueue(new Callback<AddStash>() {
            @Override
            public void onResponse(Call<AddStash> call, Response<AddStash> response) {
                AddStash stash = response.body();
                if (stash == null) {
                    Toast.makeText(ListDetails_MyStash.this, stash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                } else if (stash.getHeader().getSuccess().equals("1")) {
                    new AlertDialog.Builder(ListDetails_MyStash.this)
                            .setMessage("Add my stash successful")
                            .setTitle("Message")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                    imgAdd.setVisibility(View.GONE);
                    imgRemove.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ListDetails_MyStash.this, stash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddStash> call, Throwable t) {
                Toast.makeText(ListDetails_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        rootLayout = (RelativeLayout) findViewById(R.id.rootContainerStashDetails);
        topbarImageBack = (ImageView) findViewById(R.id.topbarImageBack);
        imgPrograms = (ImageView) findViewById(R.id.imageView_programs_mystashDetails);
        imgCoupons = (ImageView) findViewById(R.id.imageView_Coupons_mystashDetails);
        imgShare = (ImageView) findViewById(R.id.imageView_Share_mystashDetails);
        imgWrite = (ImageView) findViewById(R.id.imageView_WriteReview_mystashDetails);
        imgRemove = (ImageView) findViewById(R.id.imageView_RemoveStash_mystashDetails);
        imgAdd = (ImageView) findViewById(R.id.imageView_AddStash_mystashDetails);
        tvName = (TextView) findViewById(R.id.textView_AreaName_myStashDetails);
        tvAddress = (TextView) findViewById(R.id.textView_AreaAddress_myStashDetails);
        tvCity = (TextView) findViewById(R.id.textView_AreaPostalCity_myStashDetails);
        tvMobile = (TextView) findViewById(R.id.textView_phoneNumber_myStashDetails);
        tvDesc = (TextView) findViewById(R.id.textView_description_myStashDetails);
        tvReviews = (TextView) findViewById(R.id.textView_reviews_myStashDetails);
        rating = (RatingBar) findViewById(R.id.ratingBar_myStashDetails);
        topbarImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void settingData(Searchnearby searchnearby) {
        if (searchnearby.getName() != null)
            tvName.setText(searchnearby.getName());
        if (searchnearby.getAddress() != null)
            tvAddress.setText(searchnearby.getAddress());
        if (searchnearby.getPostalcode() != null && searchnearby.getCity() != null)
            tvCity.setText(searchnearby.getPostalcode() + " " + searchnearby.getCity());
        if (searchnearby.getContact() != null)
            tvMobile.setText(searchnearby.getContact());
        if (searchnearby.getRatingvalue() != null) {
            int starRating = Math.round(searchnearby.getRatingvalue());
            rating.setRating(starRating);
        }
        if (gsonBusiness.getReviews() != null) {
            int size = gsonBusiness.getReviews().size();
            if (obj.has("reviews") && size != 0) {
                tvReviews.setText(size + " Reviews");
            } else {
                tvReviews.setText("No Reviews");
            }
        }
    }

    private void swappingStashImg(Searchnearby searchnearby) {
        if (searchnearby.getIsstash().equals("0")) {
            imgAdd.setVisibility(View.VISIBLE);
            imgRemove.setVisibility(View.GONE);
        } else if (searchnearby.getIsstash().equals("1")) {
            imgAdd.setVisibility(View.GONE);
            imgRemove.setVisibility(View.VISIBLE);
        }
    }

    public void textViewReviews(View view) {

        /*checking if there is any review
        in the Webservice response data*/
        int size = gsonBusiness.getReviews().size();
        if (obj.has("reviews") && size != 0) {
            Intent intent = new Intent(this, ReviewDetailsList.class);
            intent.putExtra("key", s);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        swappingStashImg(gsonBusiness);
    }

    public void onClickMapIcon(View view) {
        Intent intent = new Intent(this, businessMapActivity.class);
        if (gsonBusiness.getLat() != null
                && !gsonBusiness.getLat().equals("")
                && gsonBusiness.getLongt() != null
                && !gsonBusiness.getLongt().equals("")) {
            intent.putExtra("lat", gsonBusiness.getLat());
            intent.putExtra("lng", gsonBusiness.getLongt());
            intent.putExtra("businessName", gsonBusiness.getName());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Can't get location", Toast.LENGTH_SHORT).show();
        }
    }

    public void callContactNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + gsonBusiness.getContact()));
        startActivity(intent);
    }
}
