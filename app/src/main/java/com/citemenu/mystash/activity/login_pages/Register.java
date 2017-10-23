package com.citemenu.mystash.activity.login_pages;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.activity.MainActivity;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.pojo_register.RegisterUser;
import com.citemenu.mystash.pojo.update_registeration.UpdateRegisteration;
import com.citemenu.mystash.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener, com.citemenu.mystash.activity.login_pages.MultiSpinner.MultiSpinnerListener {
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1234;
    public static boolean isNavigated = false;
    private static String imgURL;
    EditText etName, etEmail, etPwd, etCPwd, etPhone, etBday, etSex, etCateg, etInterest;
    ArrayAdapter<CharSequence> adapterSex;
    RelativeLayout rootLayout;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Spinner spinnerGender;
    private Button btnRegisterID;
    private boolean userIsInteracting = false;
    private String name, email, pwd, cpwd, phone, bday, gender, category, areaOfInterest;
    private ProgressDialog progressDialog;
    private com.citemenu.mystash.activity.login_pages.MultiSpinner multiSpinnerCat;
    private ArrayList<String> listCat, listInterest;
    private com.citemenu.mystash.activity.login_pages.MultiSpinner multiSpinnerInterest;
    private ImageView imageProfileRegister;
    private Button btnId_updateRegister, btnId_updatePwd;
    private String newPassword = "";
    private ImageView imageProfileRegisterThumb;
    private String TAG = "Register Data check";
    private String gcmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();

        settingAdapter();

        settingSpinnerOnClicks();

        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setCursorVisible(true);
            }
        });
        imageProfileRegisterThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermisions();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gcmID = sharedPreferences.getString("gcmID", "");
//        Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "onCreate: " + gcmID);
    }

    private void initialization() {
        //RootLayout
        rootLayout = (RelativeLayout) findViewById(R.id.rootContainerRegister);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        //Button
        btnRegisterID = (Button) findViewById(R.id.btnId_Register);
        btnId_updateRegister = (Button) findViewById(R.id.btnId_updateRegister);
        btnId_updatePwd = (Button) findViewById(R.id.btnId_updatePwd);

        //edittext
        etName = (EditText) findViewById(R.id.etNameRegister);
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPwd = (EditText) findViewById(R.id.etPwdRegister);
        etCPwd = (EditText) findViewById(R.id.etCnfPwdRegister);
        etPhone = (EditText) findViewById(R.id.etPhoneRegister);
        etBday = (EditText) findViewById(R.id.etBirthdayRegister);
        etSex = (EditText) findViewById(R.id.etSexRegister);
        etCateg = (EditText) findViewById(R.id.etCategoriesRegister);
        etInterest = (EditText) findViewById(R.id.etAreaOfInterestRegister);

        //Spinner
        multiSpinnerCat = (com.citemenu.mystash.activity.login_pages.MultiSpinner) findViewById(R.id.multi_spinnerCat);
        multiSpinnerInterest = (com.citemenu.mystash.activity.login_pages.MultiSpinner) findViewById(R.id.multi_spinnerInterest);
        spinnerGender = (Spinner) findViewById(R.id.spinnerSexRegister);
        imageProfileRegister = (ImageView) findViewById(R.id.imageProfileRegister);
        imageProfileRegisterThumb = (ImageView) findViewById(R.id.imageProfileRegisterThumb);
        btnId_updateRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRegister();
            }
        });
        btnId_updatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword(v);
            }
        });
    }

    private void updatePassword(final View v) {
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText oldPwd = new EditText(this);
        oldPwd.setHint(" old password");
        oldPwd.setSingleLine(true);
        layout.addView(oldPwd);

        final EditText newPwd = new EditText(this);
        newPwd.setHint(" new password");
        newPwd.setSingleLine(true);
        layout.addView(newPwd);

        final EditText newcPwd = new EditText(this);
        newcPwd.setHint(" confirm password");
        newcPwd.setSingleLine(true);
        layout.addView(newcPwd);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(lp);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        builder.setView(layout);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String old = oldPwd.getText().toString();
                String newp = newPwd.getText().toString();
                String newc = newcPwd.getText().toString();
                if (!old.equals("") || !newp.equals("") || !newc.equals("")) {
                    if (old.equals(CustomSharedPref.getUserObject(Register.this).getPassword())) {
                        if (newp.equals(newc)) {
                            hidesoftkeyboard(v);
                            newPassword = newp;
                            Toast.makeText(Register.this, "Please update account to effect changes", Toast.LENGTH_SHORT).show();
                        } else {
                            hidesoftkeyboard(v);
                            Toast.makeText(Register.this, "new password not matched", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hidesoftkeyboard(v);
                        Toast.makeText(Register.this, "old password not matched", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hidesoftkeyboard(getCurrentFocus());
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateRegister() {
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //TODO better option for email validation
        gettingEdittextData();
        if (!name.equals("") && !email.equals("") &&
                !gender.equals("") && email.matches(emailPattern)) {
            progressDialog.show();
            gettingEdittextData();
            if (!CustomSharedPref.getUserObject(this).getPassword().equals("")) {
                if (!newPassword.equals("")) {
                    pwd = newPassword;
                } else {
                    pwd = CustomSharedPref.getUserObject(this).getPassword();
                }
            }
            final String loginType = CustomSharedPref.getUserObject(this).getLogintype();
            if (loginType.equals("1")) {
                final String fbid = CustomSharedPref.getUserObject(this).getFbid();
                //Updating Fb User the user
                Call<UpdateRegisteration> call = WebServicesFactory.getInstance().postUpdateRegisterFbUser(Constant.ACTION_UPDATE_REGISTER_CUSTOMER,
                        fbid, name, email, pwd, phone, imgURL, bday, gender, category, areaOfInterest, loginType);
                Log.d(TAG, "onResponse: " + name + " " + email + " " + pwd + " " + phone + " " + imgURL + " " + bday + " " + gender + " " + category + " " + areaOfInterest);
                call.enqueue(new Callback<UpdateRegisteration>() {
                    @Override
                    public void onResponse(Call<UpdateRegisteration> call, Response<UpdateRegisteration> response) {
                        UpdateRegisteration registerResponse = response.body();
                        if (registerResponse.getHeader().getSuccess().equals("1")) {
                            progressDialog.dismiss();
                            String id = CustomSharedPref.getUserObject(Register.this).getId();
                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            CustomSharedPref.RemoveUserObject(Register.this);
                            Users obj = new Users();
                            obj.setId(id);
                            obj.setCfirstname(name);
                            obj.setEmail(email);
                            obj.setPassword(pwd);
                            obj.setContactnumber(phone);
                            obj.setImgurl(imgURL);
                            obj.setBirthday(bday);
                            obj.setSex(gender);
                            obj.setCategories(category);
                            obj.setAreaOfInterest(areaOfInterest);
                            obj.setLogintype(loginType);
                            CustomSharedPref.setUserObject(Register.this, obj);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRegisteration> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //Updating Normal User user
                Call<UpdateRegisteration> call = WebServicesFactory.getInstance().postUpdateRegisterUser(Constant.ACTION_UPDATE_REGISTER_CUSTOMER,
                        name, email, pwd, phone, imgURL, bday, gender, category, areaOfInterest, loginType);
                Log.d(TAG, "onResponse: " + name + " " + email + " " + pwd + " " + phone + " " + imgURL + " " + bday + " " + gender + " " + category + " " + areaOfInterest);
                call.enqueue(new Callback<UpdateRegisteration>() {
                    @Override
                    public void onResponse(Call<UpdateRegisteration> call, Response<UpdateRegisteration> response) {
                        UpdateRegisteration registerResponse = response.body();
                        if (registerResponse.getHeader().getSuccess().equals("1")) {
                            progressDialog.dismiss();
                            String id = CustomSharedPref.getUserObject(Register.this).getId();
                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            CustomSharedPref.RemoveUserObject(Register.this);
                            Users obj = new Users();
                            obj.setId(id);
                            obj.setCfirstname(name);
                            obj.setEmail(email);
                            obj.setPassword(pwd);
                            obj.setContactnumber(phone);
                            obj.setImgurl(imgURL);
                            obj.setBirthday(bday);
                            obj.setSex(gender);
                            obj.setCategories(category);
                            obj.setAreaOfInterest(areaOfInterest);
                            obj.setLogintype(loginType);
                            CustomSharedPref.setUserObject(Register.this, obj);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRegisteration> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(Register.this, "Please enter valid fields", Toast.LENGTH_SHORT).show();
        }
    }

    //Spinner OnItemSelectedListeners
    private void settingSpinnerOnClicks() {
        spinnerGender.setOnItemSelectedListener(this);
    }

    //Spinner Adapter
    private void settingAdapter() {
        adapterSex = ArrayAdapter.createFromResource(this,
                R.array.genderRegisterArray, android.R.layout.select_dialog_item);
        spinnerGender.setAdapter(adapterSex);

        // MultiSpinners
        listCat = new ArrayList<>();
        listCat.add("Restaurants");
        listCat.add("Clothing Men's");
        listCat.add("Clothing Women's");
        listCat.add("Electronics");
        listCat.add("Services");
        listCat.add("Other");

        listInterest = new ArrayList<>();
        listInterest.add("Montreal");
        listInterest.add("Laval");
        listInterest.add("South Shore/Rive Sud");
        listInterest.add("Other/Autre");

        multiSpinnerCat.setItems(listCat, getString(R.string.reg_login), this);

        com.citemenu.mystash.activity.login_pages.MultiSpinner.MultiSpinnerListener listener = new com.citemenu.mystash.activity.login_pages.MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                ArrayList<String> value = new ArrayList<>();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        value.add(listInterest.get(i));
                    }
                }
                String interest = TextUtils.join(",", value);
                etInterest.setText(interest);
            }
        };
        multiSpinnerInterest.setItems(listInterest, getString(R.string.reg_login), listener);
    }

    public void backRegisterImageBtn(View view) {
        finish();
    }

    public void BirthdayRegister(View view) {
        hidesoftkeyboard(view);
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(this,
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    public void GenderRegister(View view) {
        hidesoftkeyboard(view);
        spinnerGender.performClick();
    }

    public void CategoriesRegister(View view) {
        hidesoftkeyboard(view);
        multiSpinnerCat.performClick();
    }

    public void AreaOfInterestRegister(View view) {
        hidesoftkeyboard(view);
        multiSpinnerInterest.performClick();
    }

    private void hidesoftkeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerSexRegister:
                if (position != 0) {
                    etSex.setText(parent.getItemAtPosition(position).toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        switch (parent.getId()){
//            case R.id.spinnerSexRegister:
//                String text = etSex.getText().toString();
//                etSex.setText();
//                break;
//            default:
//                break;
//        }
    }

    private void gettingEdittextData() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        pwd = etPwd.getText().toString();
        cpwd = etCPwd.getText().toString();
        phone = etPhone.getText().toString();
        bday = etBday.getText().toString();
        gender = etSex.getText().toString();
        category = etCateg.getText().toString();
        areaOfInterest = etInterest.getText().toString();
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

        ArrayList<String> value = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                value.add(listCat.get(i));
            }
        }
        String categories = TextUtils.join(",", value);
        etCateg.setText(categories);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNavigated) {
            Users objUser = CustomSharedPref.getUserObject(this);
            imgURL = objUser.getImgurl();
            if (!objUser.getEmail().equals("")
                    || !objUser.getEmail().isEmpty()) {
                etEmail.setFocusable(false);
                etEmail.setCursorVisible(false);
                etEmail.setClickable(false);
            }
            btnRegisterID.setVisibility(View.GONE);
            findViewById(R.id.pwdContainer).setVisibility(View.GONE);
            findViewById(R.id.confPwdContainer).setVisibility(View.GONE);
            btnId_updateRegister.setVisibility(View.VISIBLE);
            if (objUser.getPassword().equals("")
                    && objUser.getLogintype().equals("1")) {
                ImageUtil.setImageWithResource(this, imageProfileRegister, imgURL);
//                if (imgURL != null && !imgURL.isEmpty()) {
//                    Picasso.with(this)
//                            .load(imgURL)
//                            .error(R.drawable.profile_image)
//                            .placeholder(R.drawable.profile_image)
//                            .into(imageProfileRegister);
//                }
                etName.setText(objUser.getCfirstname());
                etEmail.setText(objUser.getEmail());
                etSex.setText(objUser.getSex());
                if (!objUser.getContactnumber().equals(null))
                    etPhone.setText(objUser.getContactnumber());
                try {
                    if (!objUser.getCategories().toString().equals(null)) {
                        etCateg.setText(objUser.getCategories().toString());
                    }
                    if (!objUser.getAreaOfInterest().toString().equals(null)) {
                        etInterest.setText(objUser.getAreaOfInterest().toString());
                    }
                    if (!objUser.getBirthday().toString().equals(null)) {
                        etBday.setText(objUser.getBirthday().toString());
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }

                btnId_updatePwd.setVisibility(View.GONE);
            } else {
                try {
                    etName.setText(objUser.getCfirstname());
                    etEmail.setText(objUser.getEmail());
                    etSex.setText(objUser.getSex());
                    etBday.setText(objUser.getBirthday().toString());
                    etCateg.setText(objUser.getCategories().toString());
                    etInterest.setText(objUser.getAreaOfInterest().toString());
                    etPhone.setText(objUser.getContactnumber());
//                    Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "onResume: " + com.citemenu.mystash.Utils.CustomSharedPref.getUserObject(this).getBirthday());
                    btnId_updatePwd.setVisibility(View.VISIBLE);
                    ImageUtil.setImageWithResource(this, imageProfileRegister, imgURL);
//                    if (!imgURL.equals("")) {
//                        Picasso.with(this)
//                                .load(imgURL)
//                                .error(R.drawable.profile_image)
//                                .placeholder(R.drawable.profile_image)
//                                .into(imageProfileRegister);
//                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            btnRegisterID.setVisibility(View.VISIBLE);
            findViewById(R.id.pwdContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.confPwdContainer).setVisibility(View.VISIBLE);
            btnId_updateRegister.setVisibility(View.GONE);
            btnId_updatePwd.setVisibility(View.GONE);
        }
    }

    public void btnRegister(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        gettingEdittextData();
        if (!name.equals("") && !email.equals("") && !pwd.equals("") &&
                !cpwd.equals("") && !phone.equals("") && email.matches(emailPattern)) {
            if (etPwd.getText().toString().equals(etCPwd.getText().toString())) {
                progressDialog.show();
//                uploadImage(); //first webservice for uploading image
                gettingEdittextData();
                //Registering the user
                Call<RegisterUser> call = WebServicesFactory.getInstance().postRegisterUser(Constant.ACTION_REGISTER_CUSTOMER,
                        name, email, pwd, phone, imgURL, bday, gender, category, areaOfInterest, gcmID, "0");
                call.enqueue(new Callback<RegisterUser>() {
                    @Override
                    public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                        progressDialog.dismiss();
                        RegisterUser registerResponse = response.body();
                        if (registerResponse.getHeader().getSuccess().equals("1")) {
//                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            com.citemenu.mystash.pojo.pojo_register.Users vals = registerResponse.getBody().getUsers();
                            Users obj = new Users();
                            obj.setId(vals.getId());
                            obj.setCfirstname(vals.getCfirstname());
                            obj.setEmail(vals.getEmail());
                            obj.setPassword(vals.getPassword());
                            obj.setContactnumber(vals.getContactnumber());
                            obj.setImgurl(vals.getImgurl());
                            obj.setBirthday(vals.getBirthday());
                            obj.setSex(vals.getSex());
                            obj.setCategories(vals.getCategories());
                            obj.setAreaOfInterest(vals.getAreaOfInterest());
                            CustomSharedPref.setUserObject(Register.this, obj);
                            startActivity(new Intent(Register.this, MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "" + registerResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterUser> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(Register.this, "Password not matched", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(Register.this, "Please enter valid fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        }

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
        //webservice for uploading image
        uploadImage(thumbnail);
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
//        imageProfileRegister.setImageBitmap(bm);
        uploadImage(bm); //first webservice for uploading image
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

    private void uploadImage(final Bitmap thumbnail) {
        if (thumbnail != null) {
            final ProgressDialog dlg = new ProgressDialog(this);
            dlg.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            //Create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());

            //MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_file", "upload_image", requestFile);
            Call<UploadLoyaltyImage> call = WebServicesFactory.getInstance().uploadProfileImage(Constant.ACTION_UPLOAD_PROFILE_IMAGE, body);
            call.enqueue(new Callback<UploadLoyaltyImage>() {
                @Override
                public void onResponse(Call<UploadLoyaltyImage> call, Response<UploadLoyaltyImage> response) {
                    UploadLoyaltyImage uploadImage = response.body();
                    dlg.dismiss();
                    if (uploadImage.getHeader().getSuccess().equals("1")) {
                        imgURL = "http://www.mystash.ca/" + uploadImage.getBody().getFiles().getFilepath();
                        Log.d(TAG, "onResponse: " + "http://www.mystash.ca/" + uploadImage.getBody().getFiles().getFilepath());
                        imageProfileRegister.setImageBitmap(thumbnail);
                    } else {
//                        Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "Upload Profile Image onResponse: " + uploadImage + uploadImage.getHeader().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UploadLoyaltyImage> call, Throwable t) {
                    dlg.dismiss();
//                    Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "onFailure: " + t.getMessage());
//                        Toast.makeText(Register.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                }

            });
        }

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
                                    ActivityCompat.requestPermissions(Register.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(Register.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            selectImage();
        } else selectImage();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(Register.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(Register.this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Register.this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + Register.this.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        Register.this.startActivity(i);
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
                    Toast.makeText(Register.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String mMonth = String.valueOf(monthOfYear + 1);
            String mDay = String.valueOf(dayOfMonth);
            if (mMonth.length() < 2) {
                mMonth = "0" + mMonth;
            }
            if (mDay.length() < 2) {
                mDay = "0" + mDay;
            }
            String formatedString = "" + year + "-" + mMonth + "-" + mDay;
            etBday.setText(formatedString);
        }

    }
}