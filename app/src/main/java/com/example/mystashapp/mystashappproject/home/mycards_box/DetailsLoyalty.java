package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.addloyalty_pojo.AddLoyalty;
import com.example.mystashapp.mystashappproject.pojo.editloyalty_pojo.EditLoyalty;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.Getloyalty;
import com.example.mystashapp.mystashappproject.pojo.getmycards_pojo.Loyaltycard;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsLoyalty extends AppCompatActivity {
    private static final int CAMERA_PERMISSION = 1;
    public static boolean is_Edit = false;
    ViewPager viewPager;
    TextView etCard, etName, etBusiness, etDetails;
    InkPageIndicator inkPageIndicator;
    Button save;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Class<?> mClass;
    private Getloyalty convertedObjAdd;
    private ImageView img1;
    private ImageView img2;
    private Loyaltycard convertedObjEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_loyalty);

        //
        //Extra Intent from List
        //
        String objectList = getIntent().getStringExtra("addLoyaltyObject");
        convertedObjAdd = new Gson().fromJson(objectList, Getloyalty.class);

        //
        //Extra Intent from Edit
        //
        String objectEdit = getIntent().getStringExtra("editLoyaltyObject");
        convertedObjEdit = new Gson().fromJson(objectEdit, Loyaltycard.class);


        init();

        viewPager.setAdapter(new ViewPagerLayaltyAdapter(this));
        //page Indicator
        if (inkPageIndicator != null) {
            inkPageIndicator.setViewPager(viewPager);
        }
        settingData();
    }

    private void settingDataForEdit() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SimpleScannerActivity.barcodeText.equals("")) {
            etCard.setText(SimpleScannerActivity.barcodeText);
            com.google.zxing.MultiFormatWriter writer = new MultiFormatWriter();

            String finaldata = Uri.encode(SimpleScannerActivity.barcodeText, "utf-8");

            BitMatrix bm = null;
            try {
                bm = writer.encode(finaldata, BarcodeFormat.CODE_128, 150, 150);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            Bitmap ImageBitmap = Bitmap.createBitmap(280, 140, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < 280; i++) {//width
                for (int j = 0; j < 140; j++) {//height
                    ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            if (ImageBitmap != null) {
                img2.setImageBitmap(ImageBitmap);
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        if (is_Edit) {
            etCard.setText(convertedObjEdit.getCardno());
            //etName.setText(convertedObjEdit.get);
        }
    }

    private void settingData() {
        if (is_Edit) {
            etBusiness.setText(convertedObjEdit.getCardname());
            etDetails.setText(convertedObjEdit.getCarddetail());
        } else {
            etBusiness.setText(convertedObjAdd.getCardname());
            etDetails.setText(convertedObjAdd.getCarddetail());
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users cid = CustomSharedPrefLogin.getUserObject(DetailsLoyalty.this);
                if (!etName.getText().toString().equals("") && !etBusiness.getText().toString().equals("") &&
                        !etCard.getText().toString().equals("") && !etDetails.getText().toString().equals("")) {
                    if (is_Edit) {
                        Call<EditLoyalty> calledit = WebServicesFactory.getInstance().getEditLoyalty(Constant_util.ACTION_EDIT_LOYALTY_CARD, cid.getId(),
                                convertedObjEdit.getCardname(), convertedObjEdit.getCarddetail(), "Hyenoon", "Logo", etCard.getText().toString(), "Notes", "", "", "1", convertedObjEdit.getCid());
                        calledit.enqueue(new Callback<EditLoyalty>() {
                            @Override
                            public void onResponse(Call<EditLoyalty> call, Response<EditLoyalty> response) {
                                EditLoyalty editLoyalty = response.body();
                                if (editLoyalty.getHeader().getSuccess().equals("1")) {
                                    Toast.makeText(DetailsLoyalty.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(DetailsLoyalty.this, "Found 0", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<EditLoyalty> call, Throwable t) {
                                Toast.makeText(DetailsLoyalty.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Call<AddLoyalty> call = WebServicesFactory.getInstance().getAddLoyalty(Constant_util.ACTION_ADD_LOYALTY_CARD, cid.getId(),
                                convertedObjAdd.getCardname(), convertedObjAdd.getCarddetail(), "Hyenoon", "Logo", etCard.getText().toString(), "Notes", "", "", "1"
                        );
                        call.enqueue(new Callback<AddLoyalty>() {
                            @Override
                            public void onResponse(Call<AddLoyalty> call, Response<AddLoyalty> response) {
                                AddLoyalty addLoyalty = response.body();
                                if (addLoyalty.getHeader().getSuccess().equals("1")) {
                                    Toast.makeText(DetailsLoyalty.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(DetailsLoyalty.this, "Found 0", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddLoyalty> call, Throwable t) {
                                Toast.makeText(DetailsLoyalty.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(DetailsLoyalty.this, "Please Complete All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_Loyalty);
        etCard = (EditText) findViewById(R.id.edittext_loyalty_cardNo);
        etName = (EditText) findViewById(R.id.edittext_loyalty_name);
        etBusiness = (EditText) findViewById(R.id.edittext_loyalty_business);
        etDetails = (EditText) findViewById(R.id.edittext_loyalty_details);
        save = (Button) findViewById(R.id.button_loyalty_save);
        inkPageIndicator = (InkPageIndicator) findViewById(R.id.ink_indicator_loyalty);
    }

    public void imgBack_LoyaltyDetails(View view) {
        finish();
    }

    public void loyalty_details_img(View view) {
        Toast.makeText(DetailsLoyalty.this, "Clicked", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        SimpleScannerActivity.barcodeText = "";
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsLoyalty.this);
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

        img1.setImageBitmap(thumbnail);
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

        img1.setImageBitmap(bm);
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

    private class ViewPagerLayaltyAdapter extends PagerAdapter {
        Context context;
        private int[] image_resources = {R.drawable.ic_loyalty_card_front, R.drawable.ic_loyalty_card_bk};
        private LayoutInflater layoutInflater;

        public ViewPagerLayaltyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item_view = layoutInflater.inflate
                    (R.layout.swipe_layout_loyalty_details, container, false);

            ImageView imageView = (ImageView) item_view.findViewById(R.id.swipe_view_Images_loyalty);

            imageView.setImageResource(image_resources[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            img1 = (ImageView) v;
                            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                            selectImage();
                            break;
                        case 1:
                            img2 = (ImageView) v;
                            launchActivity(SimpleScannerActivity.class);
                            break;
                        default:
                            Toast.makeText(context, "default", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            container.addView(item_view);
            return item_view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public int getCount() {
            return image_resources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}
