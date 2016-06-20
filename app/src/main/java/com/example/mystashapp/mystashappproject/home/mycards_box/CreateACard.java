package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateACard extends AppCompatActivity implements View.OnClickListener {
    Button next, retake;
    TextView textview_front_of_card;
    ImageView frontCard, backArrow;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acard);
        initialization();
        clickListeners();
//        textview_front_of_card.setVisibility(View.VISIBLE);
//        retake.setVisibility(View.GONE);
        boolean comesFromDetailActivity = getIntent().getBooleanExtra("comesFromDetail", false);
        if (comesFromDetailActivity) {
            url = getIntent().getStringExtra("frontCard");
            if (!url.equals("")) {
//                textview_front_of_card.setVisibility(View.GONE);
//                retake.setVisibility(View.VISIBLE);
                Picasso.with(this).load(url)
                        .placeholder(R.drawable.placeholder_shadow)
                        .error(R.drawable.placeholder_shadow)
                        .into(frontCard);
            }
        }
    }

    private void initialization() {
        next = (Button) findViewById(R.id.button_loyaltyDetails_next);
//        retake = (Button) findViewById(R.id.button_loyaltyDetails_retake);
        frontCard = (ImageView) findViewById(R.id.imageView_captureFrontCard);
        backArrow = (ImageView) findViewById(R.id.imageview_backToolbar);
        textview_front_of_card = (TextView) findViewById(R.id.textview_front_of_card);
    }

    private void clickListeners() {
        next.setOnClickListener(this);
//        retake.setOnClickListener(this);
        frontCard.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        textview_front_of_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.button_loyaltyDetails_retake:
//                selectImage();
//                break;
            case R.id.button_loyaltyDetails_next:
                if (frontCard.isPressed() || !url.equals("")) { //TODO need new idea
                    uploadImageView();
                    takeLoyaltyNameDetails.is_Created = true;
                    Intent intent = new Intent(CreateACard.this, takeLoyaltyBarCode.class);
                    startActivity(intent);
                } else
                    Toast.makeText(CreateACard.this, "Please add front card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageView_captureFrontCard:
                selectImage();
                break;
            case R.id.imageview_backToolbar:
                startActivity(new Intent(CreateACard.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.textview_front_of_card:
                break;
            default:
                break;
        }
    }

    private void uploadImageView() {
        frontCard.buildDrawingCache();
        Bitmap bitmap = frontCard.getDrawingCache();
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_file", "loyalty_images", requestFile);
            Call<UploadLoyaltyImage> call = WebServicesFactory.getInstance().uploadLoyaltyImage(Constant_util.ACTION_UPLOAD_LOYALTY_IMAGE, body);

            call.enqueue(new Callback<UploadLoyaltyImage>() {
                @Override
                public void onResponse(Call<UploadLoyaltyImage> call, Response<UploadLoyaltyImage> response) {
                    UploadLoyaltyImage uploadLoyaltyImage = response.body();
                    if (uploadLoyaltyImage.getHeader().getSuccess().equals("1")) {
                        Toast.makeText(CreateACard.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        String frontImage = "http://pioneerfoodclub.com/mystash/" + uploadLoyaltyImage.getBody().getFiles().getFilepath();
                        SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
                        editor.putString("frontImage", frontImage);
                        editor.apply();
//                        Intent intent = new Intent(takeLoyaltyBarCode.this, takeLoyaltyNameDetails.class);
//                        intent.putExtra("cardNumber", editText_generator_barcode.getText());
//                        startActivity(intent);
                    } else {
                        Toast.makeText(CreateACard.this, "" + uploadLoyaltyImage.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadLoyaltyImage> call, Throwable t) {
                    Log.d(Constant_util.LOG_TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(CreateACard.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(CreateACard.this, "Error on Bitmap conversion", Toast.LENGTH_SHORT).show();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateACard.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frontCard.setImageBitmap(thumbnail);
//        retake.setVisibility(View.VISIBLE);
//        textview_front_of_card.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        frontCard.setImageBitmap(bm);
//        retake.setVisibility(View.VISIBLE);
//        textview_front_of_card.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateACard.this, Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
