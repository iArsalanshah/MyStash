package com.citemenu.mystash.home.mycards_box;

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
import com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty;
import com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.webservicefactory.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
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
    com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty getloyaltyObj;
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
        barcodeImage = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("barcodeImage", "none");
        isUpdateLoyalty = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        loyaltyID = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("loyaltyID", null);
        initialization();
        isComesFromDetail = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        if (isComesFromDetail) {
            ur_name.setText(getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("cardUrName", "null"));
            card_name.setText(getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("cardName", "null"));
            card_notes.setText(getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("cardNote", "null"));
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
        cid = CustomSharedPref.getUserObject(takeLoyaltyNameDetails.this);
        String objectLoyalty = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("addLoyaltyObject", "");//getIntent().getStringExtra("addLoyaltyObject");
        getloyaltyObj = new Gson().fromJson(objectLoyalty, com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty.class);
        if (is_Created) {
            isRegistered = "0";
            //overwrite frontImage from takeImage.class
            frontImage = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString("frontImage", "");
            Log.d("LOG_TAG", "initialization: front image is_created part working");
        } else {
            //overwrite frontImage from GsonObject
            isRegistered = "1";
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
                startActivity(new Intent(takeLoyaltyNameDetails.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        Call<com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty> call = WebServicesFactory.getInstance().getEditLoyalty(com.citemenu.mystash.helper.Constant_util.ACTION_EDIT_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(), "", "",
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage, isRegistered, loyaltyID);
        call.enqueue(new Callback<com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty> call, Response<EditLoyalty> response) {
                com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty editLoyalty = response.body();
                if (editLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(takeLoyaltyNameDetails.this, com.citemenu.mystash.home.mycards_box.DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("isRegistered", isRegistered);
                    intent.putExtra("loyaltyPosition", loyaltyID);
                    com.citemenu.mystash.home.mycards_box.DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                } else {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + editLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.editloyalty_pojo.EditLoyalty> call, Throwable t) {
                Toast.makeText(takeLoyaltyNameDetails.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLoyaltyCard() {
        Call<com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty> call = WebServicesFactory.getInstance().getAddLoyalty(com.citemenu.mystash.helper.Constant_util.ACTION_ADD_LOYALTY_CARD,
                cid.getId(), ur_name.getText().toString(), card_name.getText().toString(),
                card_Number, card_notes.getText().toString(), frontImage, barcodeImage, isRegistered);
        call.enqueue(new Callback<com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty> call, Response<AddLoyalty> response) {
                com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty addLoyalty = response.body();
                if (addLoyalty.getHeader().getSuccess().equals("1")) {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(takeLoyaltyNameDetails.this, com.citemenu.mystash.home.mycards_box.DetailsLoyalty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", card_Number);
                    intent.putExtra("urName", ur_name.getText().toString());
                    intent.putExtra("cardName", card_name.getText().toString());
                    intent.putExtra("cardNotes", card_notes.getText().toString());
                    intent.putExtra("frontCard", frontImage);
                    intent.putExtra("backCard", barcodeImage);
                    intent.putExtra("isRegistered", isRegistered);
                    intent.putExtra("loyaltyPosition", addLoyalty.getBody().getAddloyaltycards().getId());
                    com.citemenu.mystash.home.mycards_box.DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                } else {
                    Toast.makeText(takeLoyaltyNameDetails.this, "" + addLoyalty.getHeader().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.addloyalty_pojo.AddLoyalty> call, Throwable t) {
                Toast.makeText(takeLoyaltyNameDetails.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(takeLoyaltyNameDetails.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
