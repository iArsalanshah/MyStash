package com.citemenu.mystash.activity.login_pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.pojo_register.RegisterUser;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

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
        dialog.setMessage(getString(R.string.loading));
        email = etEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.isEmpty() && email.matches(emailPattern)) {
            dialog.show();
            Call<RegisterUser> call = WebServicesFactory.getInstance().getForgotPwd(Constant.ACTION_FORGOT_PWD, email);
            call.enqueue(new Callback<com.citemenu.mystash.pojo.pojo_register.RegisterUser>() {
                @Override
                public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                    dialog.dismiss();
                    if (response.body().getHeader().getSuccess().equals("1")) {
                        Toast.makeText(context, response.body().getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, response.body().getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterUser> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ActivityForgotPwd.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
        }
    }
}
