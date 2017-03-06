package com.citemenu.mystash.activity.mycards_box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty;
import com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty;
import com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeLoyaltyNameDetails extends AppCompatActivity implements View.OnClickListener {
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
    private String isRegistered;
    private boolean isComesFromDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_name_details);
        card_Number = getIntent().getStringExtra("cardNumber");
        barcodeImage = getSharedPreferences(Constant.PREFS_NAME, 0).getString("barcodeImage", "none");
        isUpdateLoyalty = getSharedPreferences(Constant.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        loyaltyID = getSharedPreferences(Constant.PREFS_NAME, 0).getString("loyaltyID", null);
        initialization();
        isComesFromDetail = getSharedPreferences(Constant.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        if (isComesFromDetail) {
            ur_name.setText(getSharedPreferences(Constant.PREFS_NAME, 0).getString("cardUrName", "null"));
            card_name.setText(getSharedPreferences(Constant.PREFS_NAME, 0).getString("cardName", "null"));
            card_notes.setText(getSharedPreferences(Constant.PREFS_NAME, 0).getString("cardNote", "null"));
        }
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
        cid = CustomSharedPref.getUserObject(TakeLoyaltyNameDetails.this);
        String objectLoyalty = getSharedPreferences(Constant.PREFS_NAME, 0).getString("addLoyaltyObject", "");//getIntent().getStringExtra("addLoyaltyObject");
        getloyaltyObj = new Gson().fromJson(objectLoyalty, Getloyalty.class);
        if (is_Created) {
            isRegistered = "0";
            //overwrite frontImage from takeImage.class
            frontImage = getSharedPreferences(Constant.PREFS_NAME, 0).getString("frontImage", "");
            Log.d("LOG_TAG", "initialization: front image is_created part working");
        } else {
            //overwrite frontImage from GsonObject
            isRegistered = "1";
            if (isComesFromDetail) { //todo BUG Need to solve
                frontImage = getSharedPreferences(Constant.PREFS_NAME, 0).getString("frontImage", "");
            } else
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
                startActivity(new Intent(TakeLoyaltyNameDetails.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.button_loyaltDetails_save:
                if (ur_name.getText().toString().length() > 0 &&
                        card_name.getText().toString().length() > 0) {
                    if (isUpdateLoyalty) {
                        updateLoyaltyCard(loyaltyID);
                    } else
                        addLoyaltyCard();
                } else
                    Toast.makeText(TakeLoyaltyNameDetails.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void updateLoyaltyCard(final String loyaltyID) {
        Call<EditLoyalty> call = WebServicesFactory.getInstance().getEditLoyalty(Constant.ACTION_EDIT_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(), "", "",
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage, isRegistered, loyaltyID);
        call.enqueue(new Callback<EditLoyalty>() {
            @Override
            public void onResponse(Call<EditLoyalty> call, Response<EditLoyalty> response) {
                com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty editLoyalty = response.body();
                if (editLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(TakeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TakeLoyaltyNameDetails.this, DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("isRegistered", isRegistered);
                    intent.putExtra("loyaltyPosition", loyaltyID);
                    DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                } else {
                    Toast.makeText(TakeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditLoyalty> call, Throwable t) {
                Toast.makeText(TakeLoyaltyNameDetails.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLoyaltyCard() {
        Call<AddLoyalty> call = WebServicesFactory.getInstance().getAddLoyalty(Constant.ACTION_ADD_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(),
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage, isRegistered);
        call.enqueue(new Callback<AddLoyalty>() {
            @Override
            public void onResponse(Call<AddLoyalty> call, Response<AddLoyalty> response) {
                AddLoyalty addLoyalty = response.body();
                if (addLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(TakeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TakeLoyaltyNameDetails.this, DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("isRegistered", isRegistered);
                    intent.putExtra("loyaltyPosition", addLoyalty.getBody().getAddloyaltycards().getId());
                    DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                } else {
                    Toast.makeText(TakeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddLoyalty> call, Throwable t) {
                Toast.makeText(TakeLoyaltyNameDetails.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TakeLoyaltyNameDetails.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
