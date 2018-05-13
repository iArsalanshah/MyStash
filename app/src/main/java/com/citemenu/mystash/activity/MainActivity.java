package com.citemenu.mystash.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.activity.login_pages.Login_activity;
import com.citemenu.mystash.activity.login_pages.Register;
import com.citemenu.mystash.activity.mycards_box.MyCards;
import com.citemenu.mystash.activity.mystash_box.List_MyStash;
import com.citemenu.mystash.activity.mystash_box.SearchBusiness_MyStash;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.pojo_cite_points.CitePointsTransactions;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.residemenu_util.CustomAdapterNavList;
import com.citemenu.mystash.residemenu_util.ResideMenu;
import com.citemenu.mystash.singleton.MyCitePoints;
import com.citemenu.mystash.singleton.MyLocation;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.utils.SelectShareIntent;
import com.citemenu.mystash.utils.Tracker;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 12345;

    static int[] listImages = {
            R.drawable.home_icon,
            R.drawable.message_icon,
            R.drawable.cite_points_icon,
            R.drawable.my_account_icon,
            R.drawable.upload_bill_icon,
            R.drawable.view_bill_icon,
            R.drawable.share_the_app_icon,
            R.drawable.logout_icon};

    private String[] listName = {};

    ListView lv;
    private ResideMenu resideMenu;
    private Tracker tracker;
    private Users user;
    private TextView tvTotalPoints;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // attach ResideMenu to current activity;
        tvTotalPoints = (TextView) findViewById(R.id.tv_total_Points);

        LinearLayout layoutCitePoints = (LinearLayout) findViewById(R.id.layout_cite_points);
        layoutCitePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // push citemenu
                startActivity(new Intent(MainActivity.this, Cite_Points.class));
            }
        });

        getTotalPoints();
        setUpMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityPermissionsDispatcher.showCameraWithCheck(this);
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
            ImageUtil.setImageWithResource(this,imgProfile,user.getImgurl());
//            if (user.getImgurl() != null && !user.getImgurl().isEmpty()) {
//                Picasso.with(this)
//                        .load(user.getImgurl())
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.placeholder)
//                        .into(imgProfile);
//            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void setUpMenu() {
        for (int i = 0; i < 6; i++){
            switch (i){
                case 0:
                    listName[i] = getString(R.string.home);
                    break;
                case 1:
                    listName[i] = getString(R.string.messages);
                    break;
                case 2:
                    listName[i] = getString(R.string.cite_points);
                    break;
                case 3:
                    listName[i] = getString(R.string.my_account);
                    break;
                case 4:
                    listName[i] = getString(R.string.upload_bill);
                    break;
                case 5:
                    listName[i] = getString(R.string.view_bill_status);
                    break;
                case 6:
                    listName[i] = getString(R.string.share_app);
                    break;
                case 7:
                    listName[i] = getString(R.string.logout);
                    break;
                default:
                    break;
            }
        }

        // attach to current activity;
        user = CustomSharedPref.getUserObject(MainActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, 0);
        resideMenu = new ResideMenu(this, R.layout.residemenu_left_activity, R.layout.residemenu_left_activity);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        lv = (ListView) resideMenu.getLeftMenuView().findViewById(R.id.listview_nav_items);
        lv.setAdapter(new CustomAdapterNavList(this, listName, listImages));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        resideMenu.closeMenu();
                        break;
                    case 1:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, Messages.class));
                        break;
                    case 2:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, Cite_Points.class));
                        break;
                    case 3:
                        resideMenu.closeMenu();
                        Register.isNavigated = true;
                        startActivity(new Intent(MainActivity.this, Register.class));
                        break;
                    case 4:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, AddBillDetails.class));
                        break;
                    case 5:
                        resideMenu.closeMenu();
                        startActivity(new Intent(MainActivity.this, UploadedBillsHistory.class));
                        break;
                    case 6:
                        resideMenu.closeMenu();
                        SelectShareIntent.selectIntent(MainActivity.this, Constant.SHARE_APP_TEXT);
                        break;
                    case 7:
                        resideMenu.closeMenu();
                        if (isLoggedIn())
                            LoginManager.getInstance().logOut();
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Constant.IS_LOGIN);
                        CustomSharedPref.RemoveUserObject(MainActivity.this);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, Login_activity.class));
                        break;
                    default:
                        resideMenu.closeMenu();
                        break;
                }
            }
        });
        //valid scale factor is between 0.0f and 1.0f. left menu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
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
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            });
        } catch (NullPointerException e) {
//            Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "addMenuButtonListener: Menu Btn Not Working");
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
        startActivity(new Intent(this, List_MyStash.class));
    }

    public void checkIn_container_home(View view) {
//        SearchBusiness_MyStash checkinLogin = new SearchBusiness_MyStash();
        SearchBusiness_MyStash.IS_CHECK_IN = true;
        startActivity(new Intent(this, SearchBusiness_MyStash.class));
    }

    public void nearBy_container_home(View view) {
        SearchBusiness_MyStash.IS_CHECK_IN = false;
        startActivity(new Intent(this, SearchBusiness_MyStash.class));
    }

    public void myCards_container_home(View view) {
        startActivity(new Intent(this, MyCards.class));
    }

    public void coupons_container_home(View view) {
        Intent intent = new Intent(MainActivity.this, Coupons.class);
        intent.putExtra("isFlyer", false);
        startActivity(intent);
    }

    public void flyers_container_home(View view) {
        Intent intent = new Intent(MainActivity.this, Coupons.class);
        intent.putExtra("isFlyer", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Exit MyStash")
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                })
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        moveTaskToBack(true);
//                        finish();
//                    }
//                })
//                .show();
        if (mBackPressed + Constant.BACK_BTN_TIME_INTERVAL > System.currentTimeMillis()) {
            moveTaskToBack(true);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.exit_message),Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void enableTracking() {
        tracker = new Tracker(this);
        if (tracker.isCanGetLocation()) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, 0);
            sharedPreferences.edit().putString(Constant.USER_LAT, String.valueOf(tracker.getLat())).apply();
            sharedPreferences.edit().putString(Constant.USER_LONG, String.valueOf(tracker.getLng())).apply();
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
    }

    @NeedsPermission({ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, CAMERA})
    void showCamera() {
        enableTracking();
    }

    @OnShowRationale({ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, CAMERA})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Please grant Location access to MyStash")
                .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, CAMERA})
    void showDeniedForCamera() {
        Toast.makeText(this, "Some features may not work without permissions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void getTotalPoints() {
        String cid = CustomSharedPref.getUserObject(this).getId();
        Call<CitePointsTransactions> call = WebServicesFactory.getInstance().getCitePoints(Constant.ACTION_GET_CITE_POINTS, cid);
        call.enqueue(new Callback<CitePointsTransactions>() {
            @Override
            public void onResponse(Call<CitePointsTransactions> call, Response<CitePointsTransactions> response) {
                CitePointsTransactions transactions = response.body();
                if (transactions == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
                } else if (transactions.getHeader().getSuccess().equals("1")) {
                    if (transactions.getBody().getTotalpoints() != null &&
                            !transactions.getBody().getTotalpoints().isEmpty()) {
                        tvTotalPoints.setText(transactions.getBody().getTotalpoints());
                        MyCitePoints.getInstance().setTotalPoints(transactions.getBody().getTotalpoints());
                        MyCitePoints.getInstance().setCitePointsHistory(transactions.getBody().getHistory());
                    } else {
                        tvTotalPoints.setText("0");
                    }
                } else {
                    Toast.makeText(MainActivity.this, transactions.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CitePointsTransactions> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
