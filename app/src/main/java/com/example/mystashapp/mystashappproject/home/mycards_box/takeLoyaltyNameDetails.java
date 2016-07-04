package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.addloyalty_pojo.AddLoyalty;
import com.example.mystashapp.mystashappproject.pojo.editloyalty_pojo.EditLoyalty;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.Getloyalty;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class takeLoyaltyNameDetails extends AppCompatActivity implements View.OnClickListener {
    public static boolean is_Created = false;
    ImageView backArrow;
    EditText ur_name, card_name, card_notes;
    Button saveButton;
    String barcodeImage;
    Getloyalty getloyaltyObj;
    String frontImage;
    String loyaltyID;
    private Users cid;
    private String card_Number;
    private boolean isUpdateLoyalty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_name_details);
        card_Number = getIntent().getStringExtra("cardNumber");
        barcodeImage = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("barcodeImage", "none");
        isUpdateLoyalty = getSharedPreferences(Constant_util.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        loyaltyID = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("loyaltyID", null);
        initialization();
        clickEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!is_Created) {
            card_name.setCursorVisible(false);
            card_notes.setCursorVisible(false);
            card_name.setText(getloyaltyObj.getCardname());
            card_notes.setText(getloyaltyObj.getCarddetail());
            card_name.setFocusable(false);
            card_name.setClickable(false);
            card_notes.setFocusable(false);
            card_notes.setClickable(false);
        }
    }

    private void initialization() {
        backArrow = (ImageView) findViewById(R.id.imageview_backTopbar);
        ur_name = (EditText) findViewById(R.id.edittext_loyaltDetails_yourName);
        card_name = (EditText) findViewById(R.id.edittext_loyaltDetails_cardName);
        card_notes = (EditText) findViewById(R.id.edittext_loyaltDetails_cardNotes);
        saveButton = (Button) findViewById(R.id.button_loyaltDetails_save);
        cid = CustomSharedPrefLogin.getUserObject(takeLoyaltyNameDetails.this);
        String objectLoyalty = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("addLoyaltyObject", "");//getIntent().getStringExtra("addLoyaltyObject");
        getloyaltyObj = new Gson().fromJson(objectLoyalty, Getloyalty.class);
        if (is_Created) {
            //overwrite frontImage from takeImage.class
            frontImage = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("frontImage", "");
            Log.d("LOG_TAG", "initialization: front image is_created part working");
        } else {
            //overwrite frontImage from GsonObject
            frontImage = getloyaltyObj.getImageurl();
        }
    }

    private void clickEvents() {
        saveButton.setOnClickListener(this);
        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_backTopbar:
                startActivity(new Intent(takeLoyaltyNameDetails.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.button_loyaltDetails_save:
                if (ur_name.getText().toString().length() > 0 &&
                        card_name.getText().toString().length() > 0) {
                    if (isUpdateLoyalty) {
                        updateLoyaltyCard(loyaltyID);
                    } else
                        addLoyaltyCard();
                } else
                    Toast.makeText(takeLoyaltyNameDetails.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void updateLoyaltyCard(final String loyaltyID) {
        Call<EditLoyalty> call = WebServicesFactory.getInstance().getEditLoyalty(Constant_util.ACTION_EDIT_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(), "", "",
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage, "", loyaltyID);
        call.enqueue(new Callback<EditLoyalty>() {
            @Override
            public void onResponse(Call<EditLoyalty> call, Response<EditLoyalty> response) {
                EditLoyalty editLoyalty = response.body();
                if (editLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(takeLoyaltyNameDetails.this, DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("loyaltyPosition", loyaltyID);
                    startActivity(intent);
                } else {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditLoyalty> call, Throwable t) {
                Toast.makeText(takeLoyaltyNameDetails.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLoyaltyCard() {
        Call<AddLoyalty> call = WebServicesFactory.getInstance().getAddLoyalty(Constant_util.ACTION_ADD_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(),
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage);
        call.enqueue(new Callback<AddLoyalty>() {
            @Override
            public void onResponse(Call<AddLoyalty> call, Response<AddLoyalty> response) {
                AddLoyalty addLoyalty = response.body();
                if (addLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(takeLoyaltyNameDetails.this, DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("loyaltyPosition", addLoyalty.getBody().getAddloyaltycards().getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddLoyalty> call, Throwable t) {
                Toast.makeText(takeLoyaltyNameDetails.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(takeLoyaltyNameDetails.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
