package com.citemenu.mystash.activity.mystash_box;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateAndReview extends AppCompatActivity {

    ProgressDialog progressDialog;
    private ImageView imageBack;
    private RatingBar rating;
    private Button submit;
    private float finalRating;
    private EditText etComments;
    private String uId, cId, bId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);
        uId = getIntent().getExtras().getString("writeUid");
        bId = getIntent().getExtras().getString("writeBid");
        cId = CustomSharedPref.getUserObject(this).getId();
        init();
        clickEvents();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
    }

    private void clickEvents() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                finalRating = rating;
//                Toast.makeText(RateAndReview.this, "Your Rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalRating > 0) {
                    submitRating();
                    if (!etComments.getText().toString().trim().equals("")) {
                        String comments = etComments.getText().toString().trim();
                        submitReview(comments);
                    }
                } else {
                    new AlertDialog.Builder(RateAndReview.this)
                            .setMessage("Required information not provided")
                            .setTitle("Message")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }
        });
    }

    private void submitReview(String comments) {
        Call<com.citemenu.mystash.pojo.add_review_pojo.ADDReview> call2 = WebServicesFactory.getInstance().getPostReview(Constant.ACTION_ADD_REVIEW, cId, bId, comments);
        call2.enqueue(new Callback<com.citemenu.mystash.pojo.add_review_pojo.ADDReview>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.add_review_pojo.ADDReview> call, Response<com.citemenu.mystash.pojo.add_review_pojo.ADDReview> response) {
                progressDialog.dismiss();
                com.citemenu.mystash.pojo.add_review_pojo.ADDReview rev = response.body();
                if (rev.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(RateAndReview.this, "Review submitted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RateAndReview.this, "" + rev.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.add_review_pojo.ADDReview> call, Throwable t) {
                progressDialog.dismiss();
//                                        Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void submitRating() {
        progressDialog.show();
        Call<com.citemenu.mystash.pojo.add_rating_pojo.ADDRating> call = WebServicesFactory.getInstance().getPostRating(Constant.ACTION_ADD_RATING, bId, finalRating);
        call.enqueue(new Callback<com.citemenu.mystash.pojo.add_rating_pojo.ADDRating>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.add_rating_pojo.ADDRating> call, Response<com.citemenu.mystash.pojo.add_rating_pojo.ADDRating> response) {
                progressDialog.dismiss();
                final com.citemenu.mystash.pojo.add_rating_pojo.ADDRating rat = response.body();
                if (rat.getHeader().getSuccess().equals("1")) {
                    if (etComments.getText().toString().trim().equals("")) {
                        Toast.makeText(RateAndReview.this, "Rating submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RateAndReview.this, "" + rat.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.add_rating_pojo.ADDRating> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RateAndReview.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        imageBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        rating = (RatingBar) findViewById(R.id.ratingBar_Customer_Rating);
        submit = (Button) findViewById(R.id.reviewSubmitBtn);
        etComments = (EditText) findViewById(R.id.etComments);
    }
}
