package com.citemenu.mystash.login_pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.citemenu.mystash.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPwd extends AppCompatActivity {
    EditText etEmail;
    ProgressDialog dialog;
    private String email = "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        dialog = new ProgressDialog(this);
        context = this;
        etEmail = (EditText) findViewById(R.id.edittext_forgotPwd);
    }

    public void BackBtnForgotPwd(View view) {
        finish();
    }

    public void BtnSendForgotPwd(View view) {
        dialog.setMessage("Loading ... ");
        email = etEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.equals("") && email.matches(emailPattern)) {
            dialog.show();
            Call<com.citemenu.mystash.pojo.pojo_register.RegisterUser> call = com.citemenu.mystash.webservicefactory.WebServicesFactory.getInstance().getForgotPwd(com.citemenu.mystash.helper.Constant_util.ACTION_FORGOT_PWD, email);
            call.enqueue(new Callback<com.citemenu.mystash.pojo.pojo_register.RegisterUser>() {
                @Override
                public void onResponse(Call<com.citemenu.mystash.pojo.pojo_register.RegisterUser> call, Response<com.citemenu.mystash.pojo.pojo_register.RegisterUser> response) {
                    dialog.dismiss();
                    if (response.body().getHeader().getSuccess().equals("1")) {
                        Toast.makeText(context, "" + response.body().getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, "" + response.body().getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.citemenu.mystash.pojo.pojo_register.RegisterUser> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ActivityForgotPwd.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
    }
}
