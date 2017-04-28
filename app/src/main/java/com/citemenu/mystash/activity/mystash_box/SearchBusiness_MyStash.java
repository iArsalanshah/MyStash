package com.citemenu.mystash.activity.mystash_box;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.helper.Log;
import com.citemenu.mystash.pojo.add_stash.AddStash;
import com.citemenu.mystash.pojo.customer_check_in.CustomerCheckIn;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.pojo_searchbusiness.SearchBusiness;
import com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby;
import com.citemenu.mystash.singleton.MyLocation;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBusiness_MyStash extends AppCompatActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener {
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static boolean IS_CHECK_IN = false;
    public RecyclerView_SBAdapter2 mAdapter;
    SupportMapFragment mapFragment;
    TextView altText;
    ImageView search;
    Marker userMarker;
    private ArrayList<Marker> oldMarkers;
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;
    private SearchView search_view;
    private int attempts = 1;
    private double lat;
    private double lng;
    private GoogleMap mGoogleMap;
    private List<Searchnearby> mainSB_List;
    private Users userObj;
    private TextView tvFilter;
    private List<Searchnearby> filtered_SB_List;
    private HashMap<Marker, Searchnearby> infoWindowMarkers;
    private Map<Marker, Integer> markerPositions;
    private boolean isPlusClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_business__mystash);
        //initialization of views
        init();

        search_view.setOnQueryTextListener(this);
        isPlusClicked = getIntent().getBooleanExtra("plusClicked", false);
        //Filter Dialog
        filterDialog();
        if (IS_CHECK_IN) {
            TextView titleSearchBusiness = (TextView) findViewById(R.id.titleSearchBusiness);
            titleSearchBusiness.setText("Check In");
        }
    }

    private void filterDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Range");
        String[] filterItems = {"5km", "10km", "20km", "50km"};
        alertDialog.setItems(filterItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        getRequest(5000);
                        break;
                    case 1:
                        getRequest(10000);
                        break;
                    case 2:
                        getRequest(20000);
                        break;
                    case 3:
                        getRequest(50000);
                        break;
                    default:
                        getRequest(Constant.DEFAULT_RADIUS);
                        break;
                }
            }
        });
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }

    private void init() {
        //for latlng
        MyLocation myLocation = MyLocation.getInstance();
        lat = myLocation.getLat();
        lng = myLocation.getLng();
        altText = (TextView) findViewById(R.id.list_searchBusiness_altText);
        search_view = (SearchView) findViewById(R.id.search_view);

        search = (ImageView) findViewById(R.id.img_search_business);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_SearchBusiness);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mainSB_List = new ArrayList<>();
        filtered_SB_List = new ArrayList<>();
        mAdapter = new RecyclerView_SBAdapter2(this);
        mRecyclerView.setAdapter(mAdapter);


        EditText et = (EditText) search_view.findViewById(search_view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        et.setTextColor(Color.BLACK);
        //Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
//        btnMapSB.setClickable(false);

        //SupportMapFragment
        mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.mapSearchBusiness);
        mapFragment.getMapAsync(this);

        tvFilter = (TextView) findViewById(R.id.tvFilter);
        oldMarkers = new ArrayList<>();
        markerPositions = new HashMap<>();
        //Retrofit Callback
        getRequest(Constant.DEFAULT_RADIUS);
    }

    void getRequest(final float defaultRadius) {
        //for cid
        userObj = CustomSharedPref.getUserObject(SearchBusiness_MyStash.this);
        progressDialog.show();
        Call<SearchBusiness> call = WebServicesFactory.getInstance().getSearchBusinessCall(
                Constant.ACTION_GET_RESTAURANT_LIST_FOR_CHECKIN, userObj.getId(), String.valueOf(lat), String.valueOf(lng),
                defaultRadius
        );
        call.enqueue(new Callback<SearchBusiness>() {

            @Override
            public void onResponse(Call<SearchBusiness> call, Response<SearchBusiness> response) {
                progressDialog.dismiss();
                SearchBusiness webResponse = response.body();
                if (webResponse == null) {
                    Toast.makeText(SearchBusiness_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    search.setClickable(false);
                } else if (webResponse.getHeader().getSuccess().equals("1")) {
                    if (!search.isClickable()) {
                        search.setClickable(true);
                    }
                    mainSB_List.clear();
                    filtered_SB_List.clear();
                    List<Searchnearby> mList = webResponse.getBody().getSearchnearby();
                    if (isPlusClicked) {
                        List<Searchnearby> updatedList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getIsstash() != null &&
                                    !mList.get(i).getIsstash().isEmpty() &&
                                    mList.get(i).getIsstash().equals("0")) {
                                updatedList.add(mList.get(i));
                            }
                        }
                        mainSB_List = updatedList;
                        filtered_SB_List = updatedList;
                    } else {
                        mainSB_List = webResponse.getBody().getSearchnearby();
                        filtered_SB_List = webResponse.getBody().getSearchnearby();
                    }

                    if (filtered_SB_List.size() == 0) {
                        if (isPlusClicked) {
                            altText.setText("You already have added all available businesses");
                            altText.setVisibility(View.VISIBLE);
                            search.setClickable(false);
                        } else {
                            altText.setText("There are currently no business nearby. Send us an email from the contact us section with some suggestions of your favorite places and we will try to add them ASAP!");
                            altText.setVisibility(View.VISIBLE);
                            search.setClickable(false);
                        }
                    }else altText.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                } else {
                    search.setClickable(false);
                    Toast.makeText(SearchBusiness_MyStash.this, "" + webResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchBusiness> call, Throwable t) {
                progressDialog.dismiss();
                search.setClickable(false);
                Log.d("ERROR ON FAILURE: " + t.toString());
                Toast.makeText(SearchBusiness_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backMyStashRecyclerViewImageBtn(View view) {
        onBackPressed();
    }

    public void BtnListSearchBusiness(View view) {
        if (mapFragment.getView() != null) {
            tvFilter.setVisibility(View.VISIBLE);
            mapFragment.getView().setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void BtnMapSearchBusiness(View view) {
//        if (mapFragment.getView() != null) {
//            tvFilter.setVisibility(View.GONE);
//            mapFragment.getView().setVisibility(View.VISIBLE);
//            search.setVisibility(View.GONE);
//        }
//        mRecyclerView.setVisibility(View.GONE);
        if (servicesOK()) {
            mRecyclerView.setVisibility(View.GONE);
            if (mapFragment.getView() != null) {
                mapFragment.getView().setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                tvFilter.setVisibility(View.GONE);
                try {
                    if (oldMarkers != null) {
                        for (Marker marker : oldMarkers) {
                            marker.remove();
                        }
                    }
                    MarkerOptions options = new MarkerOptions();
                    infoWindowMarkers = new HashMap<>();
                    for (int i = 0; i < filtered_SB_List.size(); i++) {
                        LatLng latLng = new LatLng(Double.valueOf(filtered_SB_List.get(i).getLat()),
                                Double.valueOf(filtered_SB_List.get(i).getLongt()));
                        options.position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_icon));
                        Marker m = mGoogleMap.addMarker(options);
                        oldMarkers.add(i, m);
                        infoWindowMarkers.put(oldMarkers.get(i), filtered_SB_List.get(i));
                        markerPositions.put(m, i);
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        if (userMarker.equals(marker)) {
                            return null;
                        } else {
                            final Searchnearby obj = infoWindowMarkers.get(marker);
                            if (obj == null) {
                                return null;
                            }
                            View v = getLayoutInflater().inflate(R.layout.layout_info_window, null);
                            ImageView img = (ImageView) v.findViewById(R.id.info_img);
                            TextView title = (TextView) v.findViewById(R.id.info_title);
                            TextView address = (TextView) v.findViewById(R.id.info_address);
                            if (obj.getName() != null)
                                title.setText(obj.getName());
                            if (obj.getAddress() != null)
                                address.setText(obj.getAddress());

                            ImageUtil.setImageWithResource(SearchBusiness_MyStash.this, img, obj.getLogourl());
//                            if (obj.getLogourl() != null && !obj.getLogourl().isEmpty())
//                                Picasso.with(SearchBusiness_MyStash.this)
//                                        .load(obj.getLogourl())
//                                        .placeholder(R.drawable.placeholder)
//                                        .into(img);
                            return v;
                        }
                    }
                });
                mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for (int i = 0; i < markerPositions.size(); i++) {
                            if (markerPositions.get(marker).equals(i))
                                startActivity(new Intent(SearchBusiness_MyStash.this, com.citemenu.mystash.activity.mystash_box.ListDetails_MyStash.class)
                                        .putExtra("id", new Gson().toJson(filtered_SB_List.get(i))));
                        }
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
        userMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .snippet("Your location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home_location)));//fromResource(R.drawable.home_location)
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12);
        googleMap.moveCamera(update);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (mapFragment.getView() != null) {
            mapFragment.getView().setVisibility(View.GONE);
        }
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

    public class RecyclerView_SBAdapter2 extends RecyclerView.Adapter<RecyclerView_SBAdapter2.RecyclerView_SBCustomViewHolder2> implements Filterable {
        private Context mContext;
        View.OnClickListener clickListener = new View.OnClickListener() {
            int position;

            @Override
            public void onClick(View view) {
                RecyclerView_SBCustomViewHolder2 holder =
                        (RecyclerView_SBCustomViewHolder2) view.getTag();
                position = holder.getAdapterPosition();
                if (!SearchBusiness_MyStash.IS_CHECK_IN) {
                    Searchnearby s = filtered_SB_List.get(position);
                    String jsonBusiness = (new Gson()).toJson(s);
                    Intent intent = new Intent(mContext, com.citemenu.mystash.activity.mystash_box.ListDetails_MyStash.class);
                    intent.putExtra("id", jsonBusiness);
                    mContext.startActivity(intent);
                } else {
                    getCheckingService(position);
                }
            }
        };

        private ValueFilter valueFilter;

        RecyclerView_SBAdapter2(Context context) {
            this.mContext = context;
        }

        @Override
        public RecyclerView_SBCustomViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_recyclerview_sb, viewGroup, false);
            return new RecyclerView_SBCustomViewHolder2(view);
        }

        public void onBindViewHolder(RecyclerView_SBCustomViewHolder2 customViewHolder, int i) {
            if (isPlusClicked) {
                if (filtered_SB_List.get(i).getIsstash() != null &&
                        !filtered_SB_List.get(i).getIsstash().isEmpty() &&
                        filtered_SB_List.get(i).getIsstash().equals("0")) {
                    ImageUtil.setImageWithResource(mContext, customViewHolder.thumbnail, filtered_SB_List.get(i).getLogourl());
//                    if (filtered_SB_List.get(i).getLogourl() != null
//                            && !filtered_SB_List.get(i).getLogourl().isEmpty()) {
//                        Picasso.with(mContext).load(filtered_SB_List.get(i).getLogourl())
//                                .error(R.drawable.placeholder_img_not_found) //optional
//                                .placeholder(R.drawable.placeholder_img_not_found) //optional
//                                .into(customViewHolder.thumbnail);
//                    } else {
//                        customViewHolder.thumbnail.setImageResource(R.drawable.placeholder_img_not_found);
//                    }

                    //Setting text view title,address,rating,distance
                    if (filtered_SB_List.get(i).getName() != null)
                        customViewHolder.tvTileAddress.setText(filtered_SB_List.get(i).getName());
                    if (filtered_SB_List.get(i).getAddress() != null)
                        customViewHolder.tvAreaAddress.setText(filtered_SB_List.get(i).getAddress());
                    if (filtered_SB_List.get(i).getDistance() != null) {
                        double dist = Double.valueOf(filtered_SB_List.get(i).getDistance() / 1000);
                        customViewHolder.tvMeterAddress.setText(String.format("%.1f", dist) + "km");
                    }
                    if (filtered_SB_List.get(i).getRatingvalue() != null) {
                        int starRating = Math.round(filtered_SB_List.get(i).getRatingvalue());
                        customViewHolder.rattingBar.setRating(starRating);
                    }
                }
            } else {
                ImageUtil.setImageWithResource(mContext, customViewHolder.thumbnail, filtered_SB_List.get(i).getLogourl());
//                if (filtered_SB_List.get(i).getLogourl() != null
//                        && !filtered_SB_List.get(i).getLogourl().isEmpty()) {
//                    Picasso.with(mContext).load(filtered_SB_List.get(i).getLogourl())
//                            .error(R.drawable.placeholder_img_not_found) //optional
//                            .placeholder(R.drawable.placeholder_img_not_found) //optional
//                            .into(customViewHolder.thumbnail);
//                } else {
//                    customViewHolder.thumbnail.setImageResource(R.drawable.placeholder_img_not_found);
//                }

                //Setting text view title,address,rating,distance
                if (filtered_SB_List.get(i).getName() != null)
                    customViewHolder.tvTileAddress.setText(filtered_SB_List.get(i).getName());
                if (filtered_SB_List.get(i).getAddress() != null)
                    customViewHolder.tvAreaAddress.setText(filtered_SB_List.get(i).getAddress());
                if (filtered_SB_List.get(i).getDistance() != null) {
                    //String dist = String.valueOf(filtered_SB_List.get(i).getDistance().intValue() / 1000);
                    double dist = Double.valueOf(filtered_SB_List.get(i).getDistance() / 1000);
                    customViewHolder.tvMeterAddress.setText(String.format("%.1f", dist) + "km");
                }
                if (filtered_SB_List.get(i).getRatingvalue() != null) {
                    int starRating = Math.round(filtered_SB_List.get(i).getRatingvalue());
                    customViewHolder.rattingBar.setRating(starRating);
                }
            }
            customViewHolder.thumbnail.setOnClickListener(clickListener);
            customViewHolder.thumbnail.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            return filtered_SB_List.size();
        }

        private int filteredSize() {
            if (isPlusClicked) {
                int count = 0;
                for (int i = 0; i < filtered_SB_List.size(); i++) {
//                    if (filtered_SB_List.get(i).getIsstash().equals("0")) {
                    count++;
//                    }
                }
                if (count == 0) {
                    altText.setVisibility(View.VISIBLE);
                } else {
                    altText.setVisibility(View.GONE);
                }
                return count;
            } else
                return filtered_SB_List.size();
        }

        private void getCheckingService(final int position) {
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            dialog.show();
            Call<CustomerCheckIn> checkInCall = WebServicesFactory.getInstance()
                    .checkInCustomer(Constant.ACTION_CUSTOMER_CHECKIN,
                            userObj.getId(), filtered_SB_List.get(position).getId());

            checkInCall.enqueue(new Callback<CustomerCheckIn>() {
                @Override
                public void onResponse(Call<CustomerCheckIn> call, Response<CustomerCheckIn> response) {
                    dialog.dismiss();
                    CustomerCheckIn checkIn = response.body();
                    if (checkIn == null) {
                        Toast.makeText(mContext, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } else if (checkIn.getHeader().getSuccess().equals("1")) {
                        if (filtered_SB_List.get(position).getIsstash().equals("1")) {
                            new AlertDialog.Builder(SearchBusiness_MyStash.this)
                                    .setCancelable(false)
                                    .setTitle("Thanks for visiting")
                                    .setMessage("Present yourself at the cash counter " +
                                            "and mention your name when making " +
                                            "your purchase. See you again soon")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        } else {
                            new AlertDialog.Builder(SearchBusiness_MyStash.this)
                                    .setCancelable(false)
                                    .setTitle("Thanks for visiting")
                                    .setMessage("To start earning rewards please add us to your stash")
                                    .setPositiveButton("Add to stash", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            addStash(filtered_SB_List.get(position), position);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        Toast.makeText(mContext,
                                checkIn.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerCheckIn> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void addStash(Searchnearby searchnearby, final int position) {
            Call<AddStash> call = WebServicesFactory.getInstance().getAddStash(Constant.ACTION_ADD_STASH, searchnearby.getId(), userObj.getId());
            call.enqueue(new Callback<AddStash>() {
                @Override
                public void onResponse(Call<AddStash> call, Response<AddStash> response) {
                    AddStash stash = response.body();
                    if (stash == null) {
                        Toast.makeText(SearchBusiness_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } else if (stash.getHeader().getSuccess().equals("1")) {
                        filtered_SB_List.get(position).setIsstash("1");
                        mAdapter.notifyDataSetChanged();
                        new android.support.v7.app.AlertDialog.Builder(SearchBusiness_MyStash.this)
                                .setMessage("Add my stash successful")
                                .setTitle("Message")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(SearchBusiness_MyStash.this, stash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddStash> call, Throwable t) {
                    Toast.makeText(SearchBusiness_MyStash.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        class RecyclerView_SBCustomViewHolder2 extends RecyclerView.ViewHolder {
            ImageView thumbnail;
            TextView tvTileAddress;
            TextView tvAreaAddress;
            TextView tvMeterAddress;
            RatingBar rattingBar;

            RecyclerView_SBCustomViewHolder2(View view) {
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
                    for (int i = 0; i < mainSB_List.size(); i++) {
                        if (mainSB_List.get(i).getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            Searchnearby sb = new com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby(mainSB_List.get(i).getSelected(), mainSB_List.get(i).getId(),
                                    mainSB_List.get(i).getName(), mainSB_List.get(i).getCompanyname(), mainSB_List.get(i).getAddress(),
                                    mainSB_List.get(i).getContact(), mainSB_List.get(i).getEmail(), mainSB_List.get(i).getLogourl(),
                                    mainSB_List.get(i).getUid(), mainSB_List.get(i).getCity(), mainSB_List.get(i).getPostalcode(),
                                    mainSB_List.get(i).getProvince(), mainSB_List.get(i).getCountry(), mainSB_List.get(i).getContactname(),
                                    mainSB_List.get(i).getLat(), mainSB_List.get(i).getLongt(), mainSB_List.get(i).getRatingCount(),
                                    mainSB_List.get(i).getRating(), mainSB_List.get(i).getIsCitepoint(), mainSB_List.get(i).getStatus(),
                                    mainSB_List.get(i).getIsStamp(), mainSB_List.get(i).getImages(), mainSB_List.get(i).getRatingvalue(),
                                    mainSB_List.get(i).getStashid(), mainSB_List.get(i).getIsstash(), mainSB_List.get(i).getDistance(),
                                    mainSB_List.get(i).getReviews());
                            filterList.add(sb);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mainSB_List.size();
                    results.values = mainSB_List;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered_SB_List = (List<Searchnearby>) results.values;
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}