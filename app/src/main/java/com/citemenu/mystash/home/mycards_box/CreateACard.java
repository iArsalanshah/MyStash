package com.citemenu.mystash.home.mycards_box;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.helper.FromXML;
import com.citemenu.mystash.helper.ImageCropActivity;
import com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateACard extends AppCompatActivity implements View.OnClickListener {
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
    private static final int REQ_CAMERA_IMAGE = 122;
    Button next;
    TextView textview_front_of_card;
    ImageView frontCard, backArrow;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String url = "";
    ProgressDialog progressDialog;
    boolean isCaptured = false;
    private boolean comesFromDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acard);
        initialization();
        clickListeners();

        comesFromDetailActivity = getIntent().getBooleanExtra("comesFromDetail", false);
        if (comesFromDetailActivity) {
            url = getIntent().getStringExtra("frontCard");
            if (url != null && !url.equals("")) {
                Picasso.with(this).load(url)
                        .placeholder(R.drawable.placeholder_shadow)
                        .error(R.drawable.placeholder_shadow)
                        .into(frontCard);
            }
        }
    }

    private void initialization() {
        next = (Button) findViewById(R.id.button_loyaltyDetails_next);
        frontCard = (ImageView) findViewById(R.id.imageView_captureFrontCard);
        backArrow = (ImageView) findViewById(R.id.imageview_backToolbar);
        textview_front_of_card = (TextView) findViewById(R.id.textview_front_of_card);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    private void clickListeners() {
        next.setOnClickListener(this);
        frontCard.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        textview_front_of_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_loyaltyDetails_next:
                if (comesFromDetailActivity && url != null && !url.isEmpty()) {
                    updateSharedPref(url);
                    Intent intent = new Intent(CreateACard.this, TakeLoyaltyBarCode.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (isCaptured || !url.equals("")) {
                        uploadImageView();
                    } else
                        Toast.makeText(CreateACard.this, "Please add front card", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView_captureFrontCard:
                getPermisions();
                break;
            case R.id.imageview_backToolbar:
                startActivity(new Intent(CreateACard.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.textview_front_of_card:
                break;
            default:
                break;
        }
    }

    private void uploadImageView() {
        progressDialog.show();
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
            Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call = WebServicesFactory.getInstance().uploadLoyaltyImage(Constant_util.ACTION_UPLOAD_LOYALTY_IMAGE, body);

            call.enqueue(new Callback<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage>() {
                @Override
                public void onResponse(Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call, Response<UploadLoyaltyImage> response) {
                    progressDialog.dismiss();
                    com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage uploadLoyaltyImage = response.body();
                    if (uploadLoyaltyImage.getHeader().getSuccess().equals("1")) {
                        updateSharedPref("http://www.mystash.ca/" + uploadLoyaltyImage.getBody().getFiles().getFilepath());
//                        String frontImage = "http://www.mystash.ca/" + uploadLoyaltyImage.getBody().getFiles().getFilepath();
//                        SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
//                        editor.putString("frontImage", frontImage);
//                        editor.apply();
                        Intent intent = new Intent(CreateACard.this, TakeLoyaltyBarCode.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CreateACard.this, "" + uploadLoyaltyImage.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage> call, Throwable t) {
//                    Log.d(Constant_util.LOG_TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(CreateACard.this, "Image uploading failed, Please try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else
            Toast.makeText(CreateACard.this, "Error on Bitmap conversion", Toast.LENGTH_SHORT).show();
    }

    private void updateSharedPref(String imgPath) {
        SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
        editor.putString("frontImage", imgPath);
        editor.apply();
    }

    private void selectImage() {
        final String[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateACard.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Intent intent = new Intent(CreateACard.this, ImageCropActivity.class);
//                    intent.putExtra("ACTION", Constant_util.ACTION_CAMERA);
//                    startActivityForResult(intent, REQUEST_CAMERA);
                    onUseCameraClick();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(CreateACard.this, ImageCropActivity.class);
                    intent.putExtra("ACTION", Constant_util.ACTION_GALLERY);
                    startActivityForResult(intent, SELECT_FILE);
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(String imagePath) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        if (thumbnail != null) {
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        }
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (imagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            frontCard.setImageBitmap(myBitmap);
            isCaptured = true;
        }
//        retake.setVisibility(View.VISIBLE);
//        textview_front_of_card.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(String imagePath) {
//        Uri selectedImageUri = data.getData();
//        String[] projection = {MediaStore.MediaColumns.DATA};
//        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
//                null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        cursor.moveToFirst();
//
//        String selectedImagePath = cursor.getString(column_index);
//
//        Bitmap bm;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(selectedImagePath, options);
//        final int REQUIRED_SIZE = 200;
//        int scale = 1;
//        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//            scale *= 2;
//        options.inSampleSize = scale;
//        options.inJustDecodeBounds = false;
//        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        if (imagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            frontCard.setImageBitmap(myBitmap);
            isCaptured = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String imagePath = data.getStringExtra(Constant_util.IMAGE_PATH);
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(imagePath);
            } else if (requestCode == REQ_CAMERA_IMAGE) {
                imagePath = data.getStringExtra(Constant_util.IMAGE_PATH);
                onCaptureImageResult(imagePath);
            }
        } else if (resultCode == RESULT_CANCELED) {
            //TODO : Handle case
        } else {
            String errorMsg = data.getStringExtra(ImageCropActivity.ERROR_MSG);
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateACard.this, com.citemenu.mystash.home.mycards_box.Add_LoyaltyCard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void getPermisions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            List<String> permissionsNeeded = new ArrayList<>();
            final List<String> permissionsList = new ArrayList<>();
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Read Storage");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write Storage");
            if (permissionsList.size() > 0) {

                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(CreateACard.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(CreateACard.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            selectImage();
        } else selectImage();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(CreateACard.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(CreateACard.this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CreateACard.this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + CreateACard.this.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        CreateACard.this.startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    selectImage();
                } else {
                    // Permission Denied
                    Toast.makeText(CreateACard.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @FromXML
    public void onUseCameraClick() {
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", Constant_util.ACTION_CAMERA);
        startActivityForResult(intent, REQ_CAMERA_IMAGE);
    }

    public List<int[]> getThem() {
        List<int[]> theList = new ArrayList<int[]>();
        List<int[]> list1 = new ArrayList<int[]>();
        for (int[] x : theList)
            if (x[0] == 4)
                list1.add(x);
        return list1;
    }
}
