package com.citemenu.mystash.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.singleton.MyLocation;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 12345;
    static int[] listImages = {R.drawable.ic_home, R.drawable.ic_messege, R.drawable.ic_my_cite_point, R.drawable.ic_my_account, R.drawable.ic_sharethe_app, R.drawable.ic_powerbtn};
    static String[] listName = {"Home", "Messages", "Cite Points", "My Account", "Share the App", "Logout"};
    ListView lv;
    private com.citemenu.mystash.residemenu_util.ResideMenu resideMenu;
    private com.citemenu.mystash.webservicefactory.Tracker tracker;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // attach ResideMenu to current activity;
        setUpMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPermisions();
        settingUpUserData();
    }

    private void settingUpUserData() {
        TextView Name = (TextView) resideMenu.getLeftMenuView().findViewById(R.id.textViewNameResideMenu);
        ImageView imgProfile = (ImageView) resideMenu.getLeftMenuView().findViewById(R.id.imageViewResideMenuProfile);
        //String name = user.getBody().getUsers().getCfirstname();
        try {
            if (user.getCfirstname() != null) {
                Name.setText(user.getCfirstname());
                if (user.getClastname() != null) {
                    Name.append(" " + user.getClastname());
                }
            }
            if (user.getImgurl() != null && !user.getImgurl().isEmpty()) {
                Picasso.with(this)
                        .load(user.getImgurl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imgProfile);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        user = com.citemenu.mystash.webservicefactory.CustomSharedPref.getUserObject(MainActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0);
        resideMenu = new com.citemenu.mystash.residemenu_util.ResideMenu(this, R.layout.residemenu_left_activity, R.layout.residemenu_left_activity);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        lv = (ListView) resideMenu.getLeftMenuView().findViewById(R.id.listview_nav_items);
        lv.setAdapter(new com.citemenu.mystash.residemenu_util.CustomAdapterNavList(this, listName, listImages));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        resideMenu.closeMenu();
                        break;
                    case 1:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, com.citemenu.mystash.home.Messages.class));
                        break;
                    case 2:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, com.citemenu.mystash.home.Cite_Points.class));
                        break;
                    case 3:
                        resideMenu.closeMenu();
                        com.citemenu.mystash.login_pages.Register.isNavigated = true;
                        startActivity(new Intent(MainActivity.this, com.citemenu.mystash.login_pages.Register.class));
                        break;
                    case 4:
                        resideMenu.closeMenu();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, com.citemenu.mystash.helper.Constant_util.SHARE_SUBJECT);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, com.citemenu.mystash.helper.Constant_util.SHARE_TEXT);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case 5:
                        resideMenu.closeMenu();
                        if (isLoggedIn())
                            LoginManager.getInstance().logOut();
                        SharedPreferences sharedPreferences = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(com.citemenu.mystash.helper.Constant_util.IS_LOGIN);
                        com.citemenu.mystash.webservicefactory.CustomSharedPref.RemoveUserObject(MainActivity.this);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, com.citemenu.mystash.login_pages.Login_activity.class));
                        break;
                    default:
                        resideMenu.closeMenu();
                        break;
                }
            }
        });
        //valid scale factor is between 0.0f and 1.0f. left menu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(com.citemenu.mystash.residemenu_util.ResideMenu.DIRECTION_RIGHT);
//        TextView Name = (TextView) resideMenu.getLeftMenuView().findViewById(R.id.textViewNameResideMenu);
//        ImageView imgProfile = (ImageView) resideMenu.getLeftMenuView().findViewById(R.id.imageViewResideMenuProfile);
//        //String name = user.getBody().getUsers().getCfirstname();
//        try {
//            if (user.getCfirstname() != null) {
//                Name.setText(user.getCfirstname());
//                if (user.getClastname() != null) {
//                    Name.append(" " + user.getClastname());
//                }
//            }
//            if (user.getImgurl() != null) {
//                Picasso.with(this)
//                        .load(user.getImgurl())
//                        .placeholder(R.drawable.img_profile)
//                        .error(R.drawable.img_profile)
//                        .into(imgProfile);
//            }
//        } catch (NullPointerException ex) {
//            ex.printStackTrace();
//        }
        addMenuButtonListener();
    }

    private void addMenuButtonListener() {
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        try {
            findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resideMenu.openMenu(com.citemenu.mystash.residemenu_util.ResideMenu.DIRECTION_LEFT);
                }
            });
        } catch (NullPointerException e) {
//            Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "addMenuButtonListener: Menu Btn Not Working");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    public boolean isLoggedIn() {
        return FacebookSdk.isInitialized();
    }

    public void myStash_container_home(View view) {
        startActivity(new Intent(this, com.citemenu.mystash.home.mystash_box.List_MyStash.class));
    }

    public void checkIn_container_home(View view) {
//        SearchBusiness_MyStash checkinLogin = new SearchBusiness_MyStash();
        com.citemenu.mystash.home.mystash_box.SearchBusiness_MyStash.IS_CHECK_IN = true;
        startActivity(new Intent(this, com.citemenu.mystash.home.mystash_box.SearchBusiness_MyStash.class));
    }

    public void nearBy_container_home(View view) {
        com.citemenu.mystash.home.mystash_box.SearchBusiness_MyStash.IS_CHECK_IN = false;
        startActivity(new Intent(this, com.citemenu.mystash.home.mystash_box.SearchBusiness_MyStash.class));
    }

    public void myCards_container_home(View view) {
        startActivity(new Intent(this, com.citemenu.mystash.home.mycards_box.MyCards.class));
    }

    public void coupons_container_home(View view) {
        Intent intent = new Intent(MainActivity.this, com.citemenu.mystash.home.coupons_box.Coupons.class);
        intent.putExtra("isFlyer", false);
        startActivity(intent);
    }

    public void flyers_container_home(View view) {
        Intent intent = new Intent(MainActivity.this, com.citemenu.mystash.home.coupons_box.Coupons.class);
        intent.putExtra("isFlyer", true);
        startActivity(intent);
    }

    @Override
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

    private void enableTracking() {
        tracker = new com.citemenu.mystash.webservicefactory.Tracker(this);
        if (tracker.isCanGetLocation()) {
            SharedPreferences sharedPreferences = getSharedPreferences(com.citemenu.mystash.helper.Constant_util.PREFS_NAME, 0);
            sharedPreferences.edit().putString(com.citemenu.mystash.helper.Constant_util.USER_LAT, String.valueOf(tracker.getLat())).apply();
            sharedPreferences.edit().putString(com.citemenu.mystash.helper.Constant_util.USER_LONG, String.valueOf(tracker.getLng())).apply();
            com.citemenu.mystash.singleton.MyLocation myLocation = MyLocation.getInstance();
            myLocation.setLat(tracker.getLat());
            myLocation.setLng(tracker.getLng());
        } else {
            tracker.settingGPS();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tracker != null)
            tracker.stopGPS();
//        Log.d(com.citemenu.mystash.helper.Constant_util.LOG_TAG, "Tracker Stopped");
    }

    private void getPermisions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            List<String> permissionsNeeded = new ArrayList<>();
            final List<String> permissionsList = new ArrayList<>();
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Wifi Location");
//            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//                permissionsNeeded.add("Write Storage");
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
                                    ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            enableTracking();
        } else enableTracking();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        MainActivity.this.startActivity(i);
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
                    enableTracking();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
