package com.example.mystashapp.mystashappproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.home.coupons_box.Coupons;
import com.example.mystashapp.mystashappproject.home.mycards_box.MyCards;
import com.example.mystashapp.mystashappproject.home.mystash_box.List_MyStash;
import com.example.mystashapp.mystashappproject.home.mystash_box.SearchBusiness_MyStash;
import com.example.mystashapp.mystashappproject.login_pages.Login_activity;
import com.example.mystashapp.mystashappproject.login_pages.Register;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.LoginUser;
import com.example.mystashapp.mystashappproject.residemenu_util.ResideMenu;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.Tracker;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity {
    static int[] listImages = {R.drawable.ic_home, R.drawable.ic_messege, R.drawable.ic_my_cite_point, R.drawable.ic_my_account, R.drawable.ic_sharethe_app, R.drawable.ic_powerbtn};
    static String[] listName = {"Home", "Messages", "Cite Points", "My Account", "Share the App", "Logout"};
    ListView lv;
    private ResideMenu resideMenu;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting toolbar
        setToolbar();

        // attach ResideMenu to current activity;
        setUpMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableTracking();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        TextView Name = (TextView) findViewById(R.id.textViewNameResideMenu);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
        LoginUser user = new LoginUser();
        //String name = user.getBody().getUsers().getCfirstname();
        try {
            Name.setText(user.getBody().getUsers().getCfirstname());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                        Toast.makeText(MainActivity.this, "In Progress", Toast.LENGTH_SHORT).show();
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
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MyStash share.");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case 5:
                        resideMenu.closeMenu();
                        LoginManager.getInstance().logOut();
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Constant_util.IS_LOGIN);
                        CustomSharedPrefLogin.RemoveUserObject(MainActivity.this);
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
            Toast.makeText(MainActivity.this, "Menu Btn Not Working", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    public void myStash_container_home(View view) {
        startActivity(new Intent(this, List_MyStash.class));
    }

    public void checkIn_container_home(View view) {
        SearchBusiness_MyStash checkinLogin = new SearchBusiness_MyStash();
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
        tracker = new Tracker(this);
        if (tracker.isCanGetLocation()) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
            sharedPreferences.edit().putString(Constant_util.USER_LAT, String.valueOf(tracker.getLat())).apply();
            sharedPreferences.edit().putString(Constant_util.USER_LONG, String.valueOf(tracker.getLng())).apply();
        } else {
            tracker.settingGPS();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        tracker.stopGPS();
        Log.d(Constant_util.LOG_TAG, "Tracker Stopped");
    }
}
