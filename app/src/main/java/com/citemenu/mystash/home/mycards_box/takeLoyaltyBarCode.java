package com.citemenu.mystash.home.mycards_box;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class takeLoyaltyBarCode extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_PERMISSION = 1;
    Bitmap bitmap = null;
    ImageView imageview_backTopbar, imageView_captureBarcode;
    EditText editText_generator_barcode;
    TextView button_generate_barcode;
    private Class<?> mClass;
    private Button button_next_barcode;
    private boolean isComesFromDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_bar_code);
        //initialization of views
        init();
        isComesFromDetail = getSharedPreferences(Constant_util.PREFS_NAME, 0).getBoolean("updateLoyaltyCard", false);
        if (isComesFromDetail) {
            editText_generator_barcode.setText(getSharedPreferences(Constant_util.PREFS_NAME, 0).getString("cardNumber", "null"));
            String img = getSharedPreferences
                    (Constant_util.PREFS_NAME, 0).getString("backCard", null);
            if (img != null && !img.isEmpty())
                Picasso.with(this).load(img)
                        .placeholder(R.drawable.placeholder_shadow)
                        .error(R.drawable.placeholder_shadow)
                        .into(imageView_captureBarcode);
        }
        clickListenerBind();
    }

    private void init() {
        imageview_backTopbar = (ImageView) findViewById(R.id.imagview_backTopbar);
        imageView_captureBarcode = (ImageView) findViewById(R.id.imageView_captureBarcode);
        editText_generator_barcode = (EditText) findViewById(R.id.editText_generator_barcode);
        button_generate_barcode = (TextView) findViewById(R.id.button_generate_barcode);
        button_next_barcode = (Button) findViewById(R.id.button_next_barcode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!com.citemenu.mystash.helper.SimpleScannerActivity.barcodeText.equals("")) {
            editText_generator_barcode.setText(com.citemenu.mystash.helper.SimpleScannerActivity.barcodeText);
            String barcode = editText_generator_barcode.getText().toString();
            generateBarcode(barcode);
            if (bitmap == null) {
                Toast.makeText(takeLoyaltyBarCode.this, "Barcode format not supported", Toast.LENGTH_SHORT).show();
            } else
                imageView_captureBarcode.setImageBitmap(bitmap);
        } else if (!isComesFromDetail) {
            editText_generator_barcode.setText("");
        }
    }

    private void clickListenerBind() {
        imageview_backTopbar.setOnClickListener(this);
        imageView_captureBarcode.setOnClickListener(this);
        editText_generator_barcode.setOnClickListener(this);
        button_generate_barcode.setOnClickListener(this);
        button_next_barcode.setOnClickListener(this);
    }

    //onClick Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagview_backTopbar:
                startActivity(new Intent(takeLoyaltyBarCode.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.button_generate_barcode:
                if (editText_generator_barcode.getText().toString().length() > 0) {
                    generateBarcode(editText_generator_barcode.getText().toString());
                    if (bitmap == null) {
                        Toast.makeText(takeLoyaltyBarCode.this, "Barcode format not supported", Toast.LENGTH_SHORT).show();
                    } else
                        imageView_captureBarcode.setImageBitmap(bitmap);
                } else
                    Toast.makeText(takeLoyaltyBarCode.this, "Please enter appropriate barcode number", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_next_barcode:
                if (editText_generator_barcode.getText().toString().length() > 0) {
                    //Convert to byte array
                    if (bitmap == null) {
                        generateBarcode(editText_generator_barcode.getText().toString());
                    }
                    if (bitmap == null) {
                        Toast.makeText(takeLoyaltyBarCode.this, "Barcode format not supported", Toast.LENGTH_SHORT).show();
                    } else
                        uploadBarcodeImage();
                } else
                    Toast.makeText(takeLoyaltyBarCode.this, "Please generate barcode", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageView_captureBarcode:
                launchActivity(com.citemenu.mystash.helper.SimpleScannerActivity.class);
                break;
            default:
                break;
        }
    }

    private void uploadBarcodeImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", "loyalty_images", requestFile);

        Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call = WebServicesFactory.getInstance().uploadLoyaltyImage(com.citemenu.mystash.helper.Constant_util.ACTION_UPLOAD_LOYALTY_IMAGE, body);

        call.enqueue(new Callback<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call, Response<UploadLoyaltyImage> response) {
                dialog.dismiss();
                com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage uploadLoyaltyImage = response.body();
                if (uploadLoyaltyImage.getHeader().getSuccess().equals("1")) {
//                                Toast.makeText(takeLoyaltyBarCode.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    String barcodeImage = "http://www.mystash.ca/" + uploadLoyaltyImage.getBody().getFiles().getFilepath();
                    SharedPreferences.Editor editor = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).edit();
                    editor.putString("barcodeImage", barcodeImage);
                    editor.apply();
                    Intent intent = new Intent(takeLoyaltyBarCode.this, com.citemenu.mystash.home.mycards_box.takeLoyaltyNameDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cardNumber", editText_generator_barcode.getText().toString());
                    com.citemenu.mystash.helper.SimpleScannerActivity.barcodeText = "";
                    startActivity(intent);
                } else {
                    Toast.makeText(takeLoyaltyBarCode.this, "" + uploadLoyaltyImage.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call, Throwable t) {
                dialog.dismiss();
//                Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "onFailure: " + t.getMessage());
                Toast.makeText(takeLoyaltyBarCode.this, "Barcode Image upload failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClass = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    public void generateBarcode(String barcode) {
        com.citemenu.mystash.helper.BarcodeGeneratorActivity barcodeGeneratorActivity = new com.citemenu.mystash.helper.BarcodeGeneratorActivity();
        try {
            bitmap = barcodeGeneratorActivity.encodeAsBitmap(barcode, com.citemenu.mystash.helper.SimpleScannerActivity.barcodeFormat, 600, 300);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClass != null) {
                        Intent intent = new Intent(this, mClass);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(takeLoyaltyBarCode.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}