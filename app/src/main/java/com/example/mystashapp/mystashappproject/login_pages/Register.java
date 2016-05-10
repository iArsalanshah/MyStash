package com.example.mystashapp.mystashappproject.login_pages;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.MainActivity;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.pojo_register.RegisterUser;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MultiSpinner.MultiSpinnerListener {
    public static boolean isNavigated = false;
    EditText etName, etEmail, etPwd, etCPwd, etPhone, etBday, etSex, etCateg, etInterest;
    ArrayAdapter<CharSequence> adapterSex;
    RelativeLayout rootLayout;
    private Spinner spinnerSex;
    private Button btnRegisterID;
    private boolean userIsInteracting = false;
    private String name, email, pwd, cpwd, phone, bday, gender, category, areaOfInterest;
    private ProgressDialog progressDialog;
    private MultiSpinner multiSpinnerCat;
    private ArrayList<String> listCat, listInterest;
    private MultiSpinner multiSpinnerInterest;

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
    }

    private void initialization() {
        //RootLayout
        rootLayout = (RelativeLayout) findViewById(R.id.rootContainerRegister);
        progressDialog = new ProgressDialog(this);

        //Button
        btnRegisterID = (Button) findViewById(R.id.btnId_Register);

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
        multiSpinnerCat = (MultiSpinner) findViewById(R.id.multi_spinnerCat);
        multiSpinnerInterest = (MultiSpinner) findViewById(R.id.multi_spinnerInterest);
        spinnerSex = (Spinner) findViewById(R.id.spinnerSexRegister);
    }

    //Spinner OnItemSelectedListeners
    private void settingSpinnerOnClicks() {
        spinnerSex.setOnItemSelectedListener(this);
    }

    //Spinner Adapter
    private void settingAdapter() {
        adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sexRegisterArray, android.R.layout.select_dialog_item);
        spinnerSex.setAdapter(adapterSex);

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

        multiSpinnerCat.setItems(listCat, getString(R.string.btn_reg_login), this);

        MultiSpinner.MultiSpinnerListener listener = new MultiSpinner.MultiSpinnerListener() {
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
        multiSpinnerInterest.setItems(listInterest, getString(R.string.btn_reg_login), listener);
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

    public void SexRegister(View view) {
        hidesoftkeyboard(view);
        spinnerSex.performClick();
    }

    public void CategoriesRegister(View view) {
        hidesoftkeyboard(view);
        multiSpinnerCat.performClick();
    }

    public void AreaOfInterestRegoster(View view) {
        hidesoftkeyboard(view);
        multiSpinnerInterest.performClick();
    }

    private void hidesoftkeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (userIsInteracting) {
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
            CustomSharedPrefLogin.getUserObject(this);
            Log.d(Constant_util.LOG_TAG, "" + CustomSharedPrefLogin.getUserObject(this).getCfirstname());
            Log.d(Constant_util.LOG_TAG, "" + CustomSharedPrefLogin.getUserObject(this).getSex());
            etName.setText(CustomSharedPrefLogin.getUserObject(this).getCfirstname());
            etEmail.setText(CustomSharedPrefLogin.getUserObject(this).getEmail());
            etSex.setText(CustomSharedPrefLogin.getUserObject(this).getSex());
            btnRegisterID.setText("Update");
        }
    }

    public void btnRegister(View view) {
        progressDialog.setMessage("Loading...");
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        gettingEdittextData();
        if (!name.equals("") && !email.equals("") && !pwd.equals("") &&
                !cpwd.equals("") && !phone.equals("") && !bday.equals("") &&
                !gender.equals("") && !category.equals("") && !areaOfInterest.equals("") && email.matches(emailPattern)) {
            if (etPwd.getText().toString().equals(etCPwd.getText().toString())) {
                progressDialog.show();
                gettingEdittextData();

                //Registering the user
                Call<RegisterUser> call = WebServicesFactory.getInstance().postRegisterUser(Constant_util.ACTION_REGISTER_CUSTOMER,
                        name, email, pwd, phone, bday, gender, category, areaOfInterest);
                call.enqueue(new Callback<RegisterUser>() {
                    @Override
                    public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                        RegisterUser registerResponse = response.body();
                        if (registerResponse.getHeader().getSuccess().equals("1")) {
                            progressDialog.dismiss();

                            Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            //SharePref
                            SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
                            editor.putString(Constant_util.IS_LOGIN, Constant_util.IS_LOGIN);
                            editor.putString(Constant_util.USER_NAME, registerResponse.getBody().getUsers().getCfirstname());
                            editor.putString(Constant_util.USER_ID, registerResponse.getBody().getUsers().getCfirstname());
                            editor.apply();
                            startActivity(new Intent(Register.this, MainActivity.class));
                        } else {
                            Snackbar.make(rootLayout, "" + registerResponse.getHeader().getSuccess(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterUser> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar.make(rootLayout, "Register Failure", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(Register.this, R.color.colorPrimary));
                        snackbar.show();
                    }
                });

            } else {
                Snackbar snackbar = Snackbar.make(rootLayout, "Password not matched!", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                snackbar.show();
            }

        } else {
            Snackbar snackbar = Snackbar.make(rootLayout, "Please enter valid fields!", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }
    }

    private class mDateSetListener
            implements DatePickerDialog.OnDateSetListener {

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