package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.helper.BarcodeGeneratorActivity;
import com.example.mystashapp.mystashappproject.helper.SimpleScannerActivity;
import com.example.mystashapp.mystashappproject.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_loyalty_bar_code);


        //initialization of views
        init();
        clickListenerBind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SimpleScannerActivity.barcodeText.equals("")) {
            editText_generator_barcode.setText(SimpleScannerActivity.barcodeText);
            com.google.zxing.MultiFormatWriter writer = new MultiFormatWriter();

            String finaldata = Uri.encode(SimpleScannerActivity.barcodeText, "utf-8");

            BitMatrix bm = null;
            try {
                bm = writer.encode(finaldata, BarcodeFormat.CODE_128, 150, 150);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            Bitmap ImageBitmap = Bitmap.createBitmap(220, 100, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < 220; i++) {//width
                for (int j = 0; j < 100; j++) {//height
                    if (bm != null) {
                        ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.TRANSPARENT);
                    }
                }
            }

            imageView_captureBarcode.setImageBitmap(ImageBitmap);
        }
    }

    private void init() {
        imageview_backTopbar = (ImageView) findViewById(R.id.imagview_backTopbar);
        imageView_captureBarcode = (ImageView) findViewById(R.id.imageView_captureBarcode);
        editText_generator_barcode = (EditText) findViewById(R.id.editText_generator_barcode);
        button_generate_barcode = (TextView) findViewById(R.id.button_generate_barcode);
        button_next_barcode = (Button) findViewById(R.id.button_next_barcode);
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
                finish();
                break;
            case R.id.button_generate_barcode:
                if (editText_generator_barcode.getText().toString().length() > 5) {
                    generateBarcode();
                    imageView_captureBarcode.setImageBitmap(bitmap);
                } else
                    Toast.makeText(takeLoyaltyBarCode.this, "Please enter appropriate barcode number", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_next_barcode:
                if (editText_generator_barcode.getText().toString().length() > 5) {

                    //Convert to byte array
                    if (bitmap == null) {
                        generateBarcode();
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                    // create RequestBody instance from file
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("uploaded_file", "loyalty_images", requestFile);

                    //TODO need to implement Webservice for upload loyalty image

                    Call<UploadLoyaltyImage> call = WebServicesFactory.getInstance().uploadLoyaltyImage(Constant_util.ACTION_UPLOAD_LOYALTY_IMAGE, body);

                    call.enqueue(new Callback<UploadLoyaltyImage>() {
                        @Override
                        public void onResponse(Call<UploadLoyaltyImage> call, Response<UploadLoyaltyImage> response) {
                            UploadLoyaltyImage uploadLoyaltyImage = response.body();
                            if (uploadLoyaltyImage.getHeader().getSuccess().equals("1")) {
                                Toast.makeText(takeLoyaltyBarCode.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                String barcodeImage = "http://pioneerfoodclub.com/mystash/" + uploadLoyaltyImage.getBody().getFiles().getFilepath();
                                SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
                                editor.putString("barcodeImage", barcodeImage);
                                editor.apply();
                                Intent intent = new Intent(takeLoyaltyBarCode.this, takeLoyaltyNameDetails.class);
                                intent.putExtra("cardNumber", editText_generator_barcode.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(takeLoyaltyBarCode.this, "" + uploadLoyaltyImage.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadLoyaltyImage> call, Throwable t) {
                            Log.d(Constant_util.LOG_TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(takeLoyaltyBarCode.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(takeLoyaltyBarCode.this, "Please generate barcode", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageView_captureBarcode:
                launchActivity(SimpleScannerActivity.class);
                break;
            default:
                break;
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

    public void generateBarcode() {
        BarcodeGeneratorActivity barcodeGeneratorActivity = new BarcodeGeneratorActivity();
        try {
            bitmap = barcodeGeneratorActivity.encodeAsBitmap(editText_generator_barcode.getText().toString(), BarcodeFormat.CODE_128, 600, 300);
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

    }

    //    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
//        String contentsToEncode = contents;
//        if (contentsToEncode == null) {
//            return null;
//        }
//        Map<EncodeHintType, Object> hints = null;
//        String encoding = guessAppropriateEncoding(contentsToEncode);
//        if (encoding != null) {
//            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//            hints.put(EncodeHintType.CHARACTER_SET, encoding);
//        }
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix result;
//        try {
//            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
//        } catch (IllegalArgumentException iae) {
//            // Unsupported format
//            return null;
//        }
//        int width = result.getWidth();
//        int height = result.getHeight();
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            int offset = y * width;
//            for (int x = 0; x < width; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }
//
//    private static String guessAppropriateEncoding(CharSequence contents) {
//        // Very crude at the moment
//        for (int i = 0; i < contents.length(); i++) {
//            if (contents.charAt(i) > 0xFF) {
//                return "UTF-8";
//            }
//        }
//        return null;
//    }
}
