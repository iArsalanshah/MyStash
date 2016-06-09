package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.customer_check_in.CustomerCheckIn;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBusiness_MyStash extends AppCompatActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener {
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static boolean IS_CHECK_IN = false;
    public RecyclerView_SBAdapter2 mAdapter;
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
    private Users objective;

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
        objective = CustomSharedPrefLogin.getUserObject(SearchBusiness_MyStash.this);

        //for latlng
        sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
        lat = sharedPreferences.getString(Constant_util.USER_LAT, "0");
        lng = sharedPreferences.getString(Constant_util.USER_LONG, "0");
        Call<SearchBusiness> call = WebServicesFactory.getInstance().getSearchBusinessCall(
                Constant_util.ACTION_GET_RESTAURANT_LIST_FOR_CHECKIN, objective.getId(),
                sharedPreferences.getString(Constant_util.USER_LAT, "0"),
                sharedPreferences.getString(Constant_util.USER_LONG, "0"),
                Constant_util.DEFAULT_RADIUS
        );
        call.enqueue(new Callback<SearchBusiness>() {

            @Override
            public void onResponse(Call<SearchBusiness> call, Response<SearchBusiness> response) {
                lv.setVisibility(View.GONE);
                try {
                    SearchBusiness businessResponse = response.body();
                    if (businessResponse.getHeader().getSuccess().equals("1")) {
                        if (businessResponse.getBody().getSearchnearby().isEmpty() && businessResponse.getBody().getSearchnearby().size() == 0) {
                            btnMapSB.setClickable(false);
                            altText.setVisibility(View.VISIBLE);
                        } else {
                            arrSearchBusiness = new
                                    ArrayList<>(businessResponse.getBody().getSearchnearby());
                            mAdapter = new RecyclerView_SBAdapter2(SearchBusiness_MyStash.this, arrSearchBusiness);
                            mRecyclerView.setAdapter(mAdapter);
                            btnMapSB.setClickable(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else if (businessResponse.getHeader().getSuccess().equals("0")) {
                        btnMapSB.setClickable(false);
                        Toast.makeText(SearchBusiness_MyStash.this, "" + businessResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SearchBusiness> call, Throwable t) {
                lv.setVisibility(View.GONE);
                btnMapSB.setClickable(false);
                Toast.makeText(SearchBusiness_MyStash.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
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


    public class RecyclerView_SBAdapter2 extends RecyclerView.Adapter<RecyclerView_SBAdapter2.RecyclerView_SBCustomViewHolder2> implements Filterable {
        private List<Searchnearby> searchNearbyList;
        private List<Searchnearby> mStringFilterList;
        private Context mContext;
        View.OnClickListener clickListener = new View.OnClickListener() {
            public int position;

            @Override
            public void onClick(View view) {

                if (!SearchBusiness_MyStash.IS_CHECK_IN) {
                    RecyclerView_SBCustomViewHolder2 holder =
                            (RecyclerView_SBCustomViewHolder2) view.getTag();
                    position = holder.getAdapterPosition();
                    Searchnearby s = searchNearbyList.get(position);
                    String jsonBusiness = (new Gson()).toJson(s);
                    Intent intent = new Intent(mContext, ListDetails_MyStash.class);
                    intent.putExtra("id", jsonBusiness);
                    mContext.startActivity(intent);
                } else {
                    getCheckingService(position);
                }
            }
        };
        private ValueFilter valueFilter;

        public RecyclerView_SBAdapter2(Context context, List<Searchnearby> searchnearbies) {
            searchNearbyList = searchnearbies;
            mStringFilterList = searchnearbies;
            this.mContext = context;
        }

        private void getCheckingService(int position) {
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            dialog.show();
            Call<CustomerCheckIn> checkInCall = WebServicesFactory.getInstance()
                    .checkInCustomer(Constant_util.ACTION_CUSTOMER_CHECKIN,
                            objective.getId(), searchNearbyList.get(position).getId());

            checkInCall.enqueue(new Callback<CustomerCheckIn>() {
                @Override
                public void onResponse(Call<CustomerCheckIn> call, Response<CustomerCheckIn> response) {
                    dialog.dismiss();
                    CustomerCheckIn checkIn = response.body();
                    switch (checkIn.getHeader().getSuccess()) {
                        case "1":
                            Toast.makeText(mContext, "Thanks for visiting", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(mContext, "Checkin Unsuccessful", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }

                @Override
                public void onFailure(Call<CustomerCheckIn> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView_SBCustomViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recyclerview_sb, viewGroup, false);
            return new RecyclerView_SBCustomViewHolder2(view);
        }

        public void onBindViewHolder(RecyclerView_SBCustomViewHolder2 customViewHolder, int i) {
            Searchnearby sbNearBy = searchNearbyList.get(i);
            //setting image using picasso library
            Picasso.with(mContext).load(sbNearBy.getLogourl())
                    .error(R.drawable.placeholder_shadow) //optional
                    .placeholder(R.drawable.placeholder_shadow) //optional
                    .into(customViewHolder.thumbnail);

            //Setting text view title,address,rating,distance
            customViewHolder.tvTileAddress.setText(sbNearBy.getName());
            customViewHolder.tvAreaAddress.setText(sbNearBy.getAddress());
            customViewHolder.tvMeterAddress.setText(String.valueOf(sbNearBy.getDistance().intValue()) + "m");
            customViewHolder.rattingBar.setRating(sbNearBy.getRatingvalue());
            customViewHolder.thumbnail.setOnClickListener(clickListener);
            customViewHolder.thumbnail.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            return searchNearbyList.size();
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        public class RecyclerView_SBCustomViewHolder2 extends RecyclerView.ViewHolder {
            protected ImageView thumbnail;
            protected TextView tvTileAddress;
            protected TextView tvAreaAddress;
            protected TextView tvMeterAddress;
            protected RatingBar rattingBar;

            public RecyclerView_SBCustomViewHolder2(View view) {
                super(view);
                thumbnail = (ImageView) view.findViewById(R.id.item_location_sb_thumbnail);
                tvTileAddress = (TextView) view.findViewById(R.id.item_title_sb_location_address);
                tvAreaAddress = (TextView) view.findViewById(R.id.item_tvaddress_sb_location);
                tvAreaAddress.setMovementMethod(new ScrollingMovementMethod());
                tvMeterAddress = (TextView) view.findViewById(R.id.item_sb_location_meter);
                rattingBar = (RatingBar) view.findViewById(R.id.item_ratingbar_sb);
            }
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<Searchnearby> filterList = new ArrayList<>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if (mStringFilterList.get(i).getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            Searchnearby sb = new Searchnearby(mStringFilterList.get(i).getSelected(), mStringFilterList.get(i).getId(),
                                    mStringFilterList.get(i).getName(), mStringFilterList.get(i).getCompanyname(), mStringFilterList.get(i).getAddress(),
                                    mStringFilterList.get(i).getContact(), mStringFilterList.get(i).getEmail(), mStringFilterList.get(i).getLogourl(),
                                    mStringFilterList.get(i).getUid(), mStringFilterList.get(i).getCity(), mStringFilterList.get(i).getPostalcode(),
                                    mStringFilterList.get(i).getProvince(), mStringFilterList.get(i).getCountry(), mStringFilterList.get(i).getContactname(),
                                    mStringFilterList.get(i).getLat(), mStringFilterList.get(i).getLongt(), mStringFilterList.get(i).getRatingCount(),
                                    mStringFilterList.get(i).getRating(), mStringFilterList.get(i).getIsCitepoint(), mStringFilterList.get(i).getStatus(),
                                    mStringFilterList.get(i).getIsStamp(), mStringFilterList.get(i).getImages(), mStringFilterList.get(i).getRatingvalue(),
                                    mStringFilterList.get(i).getStashid(), mStringFilterList.get(i).getIsstash(), mStringFilterList.get(i).getDistance(),
                                    mStringFilterList.get(i).getReviews());
                            filterList.add(sb);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchNearbyList = (ArrayList<Searchnearby>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
