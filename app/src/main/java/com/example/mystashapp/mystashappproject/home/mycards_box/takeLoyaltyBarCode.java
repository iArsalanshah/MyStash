package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mystashapp.mystashappproject.R;

public class takeLoyaltyBarCode extends AppCompatActivity implements View.OnClickListener {

    ImageView imagview_backTopbar, imageView_captureBarcode;
    EditText editText_generator_barcode;
    private Button button_generate_barcode, button_next_barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_bar_code);
        //initialization of views
        init();
    }

    private void init() {
        imagview_backTopbar = (ImageView) findViewById(R.id.imagview_backTopbar);
        imageView_captureBarcode = (ImageView) findViewById(R.id.imageView_captureBarcode);
        editText_generator_barcode = (EditText) findViewById(R.id.editText_generator_barcode);
        button_generate_barcode = (Button) findViewById(R.id.button_generate_barcode);
        button_next_barcode = (Button) findViewById(R.id.button_next_barcode);
    }

    //onClick Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagview_backTopbar:
                finish();
                break;
            default:
                break;
        }
    }
}
