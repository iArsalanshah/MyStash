package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.mystash_box.recyclerview_util_searchbusiness.RecyclerView_SBAdapter;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.SearchBusiness;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Searchnearby;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.mingle.widget.LoadingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBusiness_MyStash extends AppCompatActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener {
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static boolean IS_CHECK_IN = false;
    public RecyclerView_SBAdapter mAdapter;
    public ArrayList<LatLng> latLngs;
    SupportMapFragment mapFragment;
    TextView altText;
    ImageView search;
    private RecyclerView mRecyclerView;
    private LoadingView lv;
    private SearchView search_view;
    private int attempts = 1;
    private SharedPreferences sharedPreferences;
    private String lat;
    private String lng;
    private GoogleMap mGoogleMap;
    private ArrayList<Searchnearby> arrSearchBusiness;
    private Button btnMapSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_business__mystash);

        altText = (TextView) findViewById(R.id.list_searchBusiness_altText);
        search_view = (SearchView) findViewById(R.id.search_view);


        search = (ImageView) findViewById(R.id.img_search_business);
        btnMapSB = (Button) findViewById(R.id.btnMapSB);
        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_SearchBusiness);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //LoadingView
        lv = (LoadingView) findViewById(R.id.loadView);
        if (lv != null) {
            lv.setVisibility(View.VISIBLE);
            btnMapSB.setClickable(false);
        }

        //SupportMapFragment
        mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.mapSearchBusiness);
        mapFragment.getMapAsync(this);
        if (mapFragment.getView() != null)
            mapFragment.getView().setVisibility(View.GONE);
        search_view.setOnQueryTextListener(this);
        EditText et = (EditText) search_view.findViewById(search_view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        et.setTextColor(Color.BLACK);
    }

    void getRequest() {
        //for cid
        Users objective = CustomSharedPrefLogin.getUserObject(SearchBusiness_MyStash.this);

        //for latlng
        sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
        lat = sharedPreferences.getString(Constant_util.USER_LAT, "0");
        lng = sharedPreferences.getString(Constant_util.USER_LONG, "0");
        Call<SearchBusiness> call = WebServicesFactory.getInstance().getSearchBusinessCall(
                Constant_util.ACTION_GET_RESTAURANT_LIST_FOR_CHECKIN, objective.getId(),
                sharedPreferences.getString(Constant_util.USER_LAT, "0"),
                sharedPreferences.getString(Constant_util.USER_LONG, "0")
        );
        call.enqueue(new Callback<SearchBusiness>() {

            @Override
            public void onResponse(Call<SearchBusiness> call, Response<SearchBusiness> response) {
                try {
                    SearchBusiness businessResponse = response.body();
                    if (businessResponse.getHeader().getSuccess().equals("1")) {
                        arrSearchBusiness = new
                                ArrayList<>(businessResponse.getBody().getSearchnearby());
                        mAdapter = new RecyclerView_SBAdapter(SearchBusiness_MyStash.this, arrSearchBusiness);
                        mRecyclerView.setAdapter(mAdapter);
                        lv.setVisibility(View.GONE);
                        btnMapSB.setClickable(true);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        altText.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SearchBusiness> call, Throwable t) {
                lv.setVisibility(View.GONE);
                btnMapSB.setClickable(true);
                altText.setVisibility(View.VISIBLE);
            }
        });
    }

    public void backMyStashRecyclerViewImageBtn(View view) {
        finish();
    }

    public void BtnListSearchBusiness(View view) {
        if (mapFragment.getView() != null) {
            mapFragment.getView().setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void BtnMapSearchBusiness(View view) {
        if (servicesOK()) {
            mRecyclerView.setVisibility(View.GONE);
            if (mapFragment.getView() != null) {
                mapFragment.getView().setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                latLngs = new ArrayList<>();
                try {
                    for (int i = 0; i < arrSearchBusiness.size(); i++) {
                        latLngs.add(i, new LatLng(Double.valueOf(arrSearchBusiness.get(i).getLat()), Double.valueOf(arrSearchBusiness.get(i).getLongt())));
                    }

                    //for Business Pointers
                    MarkerOptions options = new MarkerOptions();
                    if (latLngs.size() > 0)
                        for (LatLng point : latLngs) {
                            options.position(point)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_icon));
                            mGoogleMap.addMarker(options);
                        }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            if (mGoogleMap != null) {
                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.layout_info_window, null);
                        ImageView img = (ImageView) v.findViewById(R.id.info_img);
                        TextView title = (TextView) v.findViewById(R.id.info_title);
                        TextView address = (TextView) v.findViewById(R.id.info_address);
                        try {
                            for (int i = 0; i < arrSearchBusiness.size(); i++) {
                                title.setText(arrSearchBusiness.get(i).getCompanyname());
                                address.setText(arrSearchBusiness.get(i).getAddress());
                                Picasso.with(SearchBusiness_MyStash.this)
                                        .load(arrSearchBusiness.get(i).getLogourl())
                                        .placeholder(R.drawable.placeholder_shadow)
                                        .into(img);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return v;
                    }
                });
            }
        }
    }

    private boolean servicesOK() {
        GoogleApiAvailability googleAPI =
                GoogleApiAvailability.getInstance();
        int isAvailable = googleAPI
                .isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else {
            if (googleAPI.isUserResolvableError(isAvailable)) {
                Dialog dialog = googleAPI.
                        getErrorDialog(this, ERROR_DIALOG_REQUEST, isAvailable);
                dialog.show();
            } else {
                Toast.makeText(SearchBusiness_MyStash.this,
                        "Can't connect to mapping service",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //User Pointer
        mGoogleMap = googleMap;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 12);
        googleMap.moveCamera(update);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(SearchBusiness_MyStash.this, ListDetails_MyStash.class);
                String intVal = marker.getId().replaceAll("[^0-9]", "");
                Log.d(Constant_util.LOG_TAG, intVal);
                String json = (new Gson().toJson(arrSearchBusiness.get(Integer.valueOf(intVal))));
                intent.putExtra("id", json);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {

            mAdapter.getFilter().filter(newText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void searchButton(View view) {
        attempts++;
        if ((attempts % 2) == 0) {
            search_view.setVisibility(View.VISIBLE);
            search_view.setIconified(false);
        } else {
            search_view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Retrofit Callback
        getRequest();
    }
}
