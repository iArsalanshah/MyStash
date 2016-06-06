package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.addloyalty_pojo.AddLoyalty;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class takeLoyaltyNameDetails extends AppCompatActivity implements View.OnClickListener {
    ImageView backArrow;
    EditText ur_name, card_name, card_notes;
    Button saveButton;
    private Users cid;
    private String card_Number;
    String barcodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_name_details);
        card_Number = getIntent().getStringExtra("cardNumber");
        barcodeImage = getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("barcodeImage", "none");
        initialization();
        clickEvents();
    }

    private void initialization() {
        backArrow = (ImageView) findViewById(R.id.imageview_backTopbar);
        ur_name = (EditText) findViewById(R.id.edittext_loyaltDetails_yourName);
        card_name = (EditText) findViewById(R.id.edittext_loyaltDetails_cardName);
        card_notes = (EditText) findViewById(R.id.edittext_loyaltDetails_cardNotes);
        saveButton = (Button) findViewById(R.id.button_loyaltDetails_save);
        cid = CustomSharedPrefLogin.getUserObject(takeLoyaltyNameDetails.this);
    }

    private void clickEvents() {
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_backTopbar:
                finish();
                break;
            case R.id.button_loyaltDetails_save:
                if (ur_name.getText().toString().length() > 0 &&
                        card_name.getText().toString().length() > 0 &&
                        card_notes.getText().toString().length() > 0) {
                    Call<AddLoyalty> call = WebServicesFactory.getInstance().getAddLoyalty(Constant_util.ACTION_ADD_LOYALTY_CARD,
                            cid.getId(), ur_name.getText().toString(), card_name.getText().toString(),
                            card_Number, card_notes.getText().toString());
                    call.enqueue(new Callback<AddLoyalty>() {
                        @Override
                        public void onResponse(Call<AddLoyalty> call, Response<AddLoyalty> response) {
                            AddLoyalty addLoyalty = response.body();
                            if (addLoyalty.getHeader().getSuccess().equals("1")) {
                                Toast.makeText(takeLoyaltyNameDetails.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(takeLoyaltyNameDetails.this, DetailsLoyalty.class);
                                intent.putExtra("cardNumber", card_Number);
                                intent.putExtra("urName", ur_name.getText().toString());
                                intent.putExtra("cardName", card_name.getText().toString());
                                intent.putExtra("cardNotes", card_notes.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(takeLoyaltyNameDetails.this, "Something went wrong please try again later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddLoyalty> call, Throwable t) {
                            Toast.makeText(takeLoyaltyNameDetails.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(takeLoyaltyNameDetails.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
