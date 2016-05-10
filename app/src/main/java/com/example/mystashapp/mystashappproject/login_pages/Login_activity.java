package com.example.mystashapp.mystashappproject.login_pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.MainActivity;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.LoginUser;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_activity extends AppCompatActivity {
    EditText etEmail, etPwd;
    String email, pwd;
    CallbackManager callbackManager;
    LoginButton loginButton;
    private RelativeLayout rootLayout;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        //btnFB = (Button)findViewById(R.id.fbLoginBtn);

        //initializing Views
        init();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                //Log.i(Constant_util.LOG_TAG, accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i(Constant_util.LOG_TAG, response.toString());
                                // Get facebook data from login
                                Bundle bFb = getFacebookData(object);

                                //WebService
                                Call<LoginUser> call = WebServicesFactory.getInstance().getFblogin(Constant_util.ACTION_FB_LOGIN,
                                        bFb.getString("email"), bFb.getString("name"),
                                        bFb.getString("idFacebook"), bFb.getString("gender"));
                                call.enqueue(new Callback<LoginUser>() {
                                    @Override
                                    public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                                        Users Webresponse = response.body().getBody().getUsers();
                                        CustomSharedPrefLogin.setUserObject(Login_activity.this, Webresponse);
                                        Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_activity.this, MainActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Call<LoginUser> call, Throwable t) {
                                        Snackbar snackbar = Snackbar.make(rootLayout, "Facebook Login Failure", Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(Login_activity.this, R.color.colorPrimary));
                                        snackbar.show();
                                    }
                                });
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(Constant_util.LOG_TAG, "on Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Constant_util.LOG_TAG, "on Error");
            }
        });
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.input_email_login);
        etPwd = (EditText) findViewById(R.id.input_pwd_login);
        loginButton = (LoginButton) findViewById(R.id.login_fb_btn);
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle rbundle = null;
        try {
            Bundle bundle = new Bundle();
            rbundle = bundle;
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("user_birthday"))
                bundle.putString("user_birthday", object.getString("user_birthday"));
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rbundle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
        if (sharedPreferences.getString(Constant_util.IS_LOGIN, "").equals(Constant_util.IS_LOGIN)) {
            startActivity(new Intent(this, MainActivity.class));
        }
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public void btnMainLogin(View view) {
        getData();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.equals("") && !pwd.equals("") && email.matches(emailPattern)) {
            prog = new ProgressDialog(this);
            prog.show();
            Call<LoginUser> call = WebServicesFactory.getInstance().getLoginUsers(Constant_util.ACTION_LOGIN_CUSTOMER, email, pwd);
            call.enqueue(new Callback<LoginUser>() {
                @Override
                public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                    LoginUser users = response.body();

                    users.getBody().getUsers();
                    if (users.getHeader().getSuccess().equals("1")) {
                        prog.dismiss();
                        Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        CustomSharedPrefLogin.setUserObject(Login_activity.this, users.getBody().getUsers());
                        startActivity(new Intent(Login_activity.this, MainActivity.class));
                    } else {
                        prog.dismiss();
                        Snackbar snackbar = Snackbar.make(rootLayout, users.getHeader().getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(Login_activity.this, R.color.colorPrimary));
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<LoginUser> call, Throwable t) {
                    prog.dismiss();
                    Snackbar snackbar = Snackbar.make(rootLayout, "Login Failure", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(Login_activity.this, R.color.colorPrimary));
                    snackbar.show();
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(rootLayout, "Please enter valid email and pwd", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }
    }

    //saving Edittext values
    private void getData() {
        email = etEmail.getText().toString();
        pwd = etPwd.getText().toString();
        rootLayout = (RelativeLayout) findViewById(R.id.rootContainerLogin);
    }

    public void btnRegLogin(View view) {
        Register.isNavigated = false;
        startActivity(new Intent(this, Register.class));
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit MyStash")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }
                })
                .show();
    }

    public void TvForgotPwd(View view) {
        startActivity(new Intent(this, ActivityForgotPwd.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppEventsLogger.deactivateApp(this);
    }
}