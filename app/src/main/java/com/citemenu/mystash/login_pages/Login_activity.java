package com.citemenu.mystash.login_pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_activity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9111;
    EditText etEmail, etPwd;
    String email, pwd;
    CallbackManager callbackManager;
    LoginButton loginButton;
    private RelativeLayout rootLayout;
    private ProgressDialog prog;
    private boolean isMarshmallow;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String gcmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//            //do something about the Android version being too new
//            isMarshmallow = true;
//        }
        setContentView(R.layout.activity_login);
        //btnFB = (Button)findViewById(R.id.fbLoginBtn);
        if (!getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).getString(com.citemenu.mystash.helper.Constant_util.IS_LOGIN, "").equals("")) {
            startActivity(new Intent(Login_activity.this, com.citemenu.mystash.home.MainActivity.class));
            finish();
        }
        initNotify();

        //initializing Views
        init();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                String accessToken = loginResult.getAccessToken().getToken();
                //Log.i(Constant_util.LOG_TAG, accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.i(com.citemenu.mystash.helper.Constant_util.LOG_TAG, response.toString());
                                // Get facebook data from login
                                Bundle bFb = getFacebookData(object);

                                //WebService
                                Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call = WebServicesFactory.getInstance().getFblogin(com.citemenu.mystash.helper.Constant_util.ACTION_FB_LOGIN,
                                        bFb.getString("email"), bFb.getString("name"),
                                        bFb.getString("idFacebook"), bFb.getString("gender"), bFb.getString("profile_pic"), gcmID, "1");
                                call.enqueue(new Callback<com.citemenu.mystash.pojo.pojo_login.LoginUser>() {
                                    @Override
                                    public void onResponse(Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call, Response<com.citemenu.mystash.pojo.pojo_login.LoginUser> response) {
                                        Users webResponse = response.body().getBody().getUsers();
//                                        Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "Checks " + webResponse);
//                                        Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "onResponse: " + webResponse.getId() + " " + webResponse.getImgurl() + " " + webResponse.getCfirstname() + " " + webResponse.getFbid());
                                        com.citemenu.mystash.webservicefactory.CustomSharedPref.setUserObject(Login_activity.this, webResponse);
                                        Toast.makeText(Login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_activity.this, com.citemenu.mystash.home.MainActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call, Throwable t) {
                                        Toast.makeText(Login_activity.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
//                Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "on Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login_activity.this, error.toString(), Toast.LENGTH_SHORT).show(); //todo Facebook login Error issue need to solve
//                Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "on Error" + error.toString());
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gcmID = sharedPreferences.getString("gcmID", "");
//        Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "onCreate: " + gcmID);

    }

    private void initNotify() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(com.citemenu.mystash.helper.Constant_util.SENT_TOKEN_TO_SERVER, false);
//                if (sentToken) {
//                    Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "gcm works");
//                } else {
//                    Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "gcm ERROR");
//                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, com.citemenu.mystash.gcm.RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
//                Log.i(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=150&height=150");
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
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rbundle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }

    public void btnMainLogin(View view) {
        getData();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.equals("") && !pwd.equals("") && email.matches(emailPattern)) {
            prog = new ProgressDialog(this);
            prog.show();
            Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call = WebServicesFactory.getInstance().getLoginUsers(com.citemenu.mystash.helper.Constant_util.ACTION_LOGIN_CUSTOMER, email, pwd);
            call.enqueue(new Callback<com.citemenu.mystash.pojo.pojo_login.LoginUser>() {
                @Override
                public void onResponse(Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call, Response<com.citemenu.mystash.pojo.pojo_login.LoginUser> response) {
                    com.citemenu.mystash.pojo.pojo_login.LoginUser users = response.body();

//                    users.getBody().getUsers();
                    if (users.getHeader().getSuccess().equals("1")) {
                        prog.dismiss();
//                        Toast.makeText(Login_activity.this, "" + users.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0).edit().putString(com.citemenu.mystash.helper.Constant_util.IS_LOGIN, com.citemenu.mystash.helper.Constant_util.IS_LOGIN).apply();
                        com.citemenu.mystash.webservicefactory.CustomSharedPref.setUserObject(Login_activity.this, users.getBody().getUsers());
                        startActivity(new Intent(Login_activity.this, com.citemenu.mystash.home.MainActivity.class));
                        finish();
                    } else {
                        prog.dismiss();
                        Toast.makeText(Login_activity.this, users.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.citemenu.mystash.pojo.pojo_login.LoginUser> call, Throwable t) {
                    prog.dismiss();
                    Toast.makeText(Login_activity.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Login_activity.this, "Please enter valid fields", Toast.LENGTH_SHORT).show();
        }
    }

    //saving Edittext values
    private void getData() {
        email = etEmail.getText().toString();
        pwd = etPwd.getText().toString();
        rootLayout = (RelativeLayout) findViewById(R.id.rootContainerLogin);
    }

    public void btnRegLogin(View view) {
        com.citemenu.mystash.login_pages.Register.isNavigated = false;
        startActivity(new Intent(this, com.citemenu.mystash.login_pages.Register.class));
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
        startActivity(new Intent(this, com.citemenu.mystash.login_pages.ActivityForgotPwd.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppEventsLogger.deactivateApp(this);
    }
}