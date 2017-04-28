package com.citemenu.mystash.activity.mycards_box;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.getcardslist_pojo.GetCardsList;
import com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_LoyaltyCard extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener {

    private ListView listview;
    //    private ArrayList<String> stTitle;
//    private ArrayList<String> stDetails;
    private ProgressDialog progress;
    private Button createButton;
    private android.widget.SearchView searchView_Addcards;
    private AddLoyaltyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loyaltycard);
        init();
        bindingData();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(Constant.PREFS_NAME, 0).edit();
                editor.putBoolean("updateLoyaltyCard", false).apply();
                Intent createIntent = new Intent(Add_LoyaltyCard.this, com.citemenu.mystash.activity.mycards_box.CreateACard.class);
                TakeLoyaltyNameDetails.is_Created = true;
                startActivity(createIntent);
            }
        });
    }

    private void bindingData() {
        searchView_Addcards.setOnQueryTextListener(Add_LoyaltyCard.this);
        searchView_Addcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView_Addcards.setIconified(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();
        //Retrofit2.0 Call
        getAddLoyaltyList();
    }

    private void getAddLoyaltyList() {
        Users cid = CustomSharedPref.getUserObject(Add_LoyaltyCard.this);
//        Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, cid.getId());
        Call<GetCardsList> call = WebServicesFactory.getInstance().getCardsList(Constant.ACTION_GET_LOYALTY_CARDS_LIST, cid.getId());
        call.enqueue(new Callback<GetCardsList>() {
            @Override
            public void onResponse(Call<GetCardsList> call, Response<GetCardsList> response) {
                progress.dismiss();
                GetCardsList getCardsList = response.body();
                if (getCardsList.getHeader().getSuccess().equals("1")) {
                    adapter = new AddLoyaltyAdapter(Add_LoyaltyCard.this, getCardsList.getBody().getGetloyalty());
                    listview.setAdapter(adapter);
                } else {
                    searchView_Addcards.setVisibility(View.GONE);
                    Toast.makeText(Add_LoyaltyCard.this, "" + getCardsList.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.getcardslist_pojo.GetCardsList> call, Throwable t) {
                progress.dismiss();
                searchView_Addcards.setVisibility(View.GONE);
                Toast.makeText(Add_LoyaltyCard.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
//                if (t.getMessage() != null)
//                    Log.d(com.citemenu.mystash.constant.Constant.LOG_TAG, "" + t.getMessage());
            }
        });
    }

    private void init() {
        listview = (ListView) findViewById(R.id.listView_Add_LoyaltyCard);
        progress = new ProgressDialog(this);
        createButton = (Button) findViewById(R.id.button_create_loyalty);
        searchView_Addcards = (android.widget.SearchView) findViewById(R.id.searchView_Addcards);
        int searchCloseButtonId = searchView_Addcards.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView_Addcards.findViewById(searchCloseButtonId);
// Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesoftkeyboard(v);
                searchView_Addcards.setQuery("", false);
            }
        });
    }

    public void imgBack_MyCards(View view) {
        startActivity(new Intent(Add_LoyaltyCard.this, MyCards.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapter.getFilter().filter(newText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        hidesoftkeyboard(getCurrentFocus());
        return false;
    }

    private void hidesoftkeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Add_LoyaltyCard.this, MyCards.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private class AddLoyaltyAdapter extends BaseAdapter implements Filterable {
        List<Getloyalty> getloyalties;
        List<Getloyalty> getFilteredloyalties;
        private LayoutInflater layoutInflater;
        private Activity context;
        private ValueFilter valueFilter;

        public AddLoyaltyAdapter(Add_LoyaltyCard add_loyaltyCard, List<Getloyalty> getloyalty) {
            context = add_loyaltyCard;
            getloyalties = getloyalty;
            getFilteredloyalties = getloyalty;
        }

        @Override
        public int getCount() {
            return getloyalties.size();
        }

        @Override
        public Object getItem(int position) {
            return getloyalties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.row_addloyaltycard_listview, parent, false);

            ImageView img = (ImageView) convertView.findViewById(R.id.imageView_row_list_myCards);
//            TextView title = (TextView) convertView.findViewById(R.id.textViewTitle_row_list_myCards);
            TextView details = (TextView) convertView.findViewById(R.id.textViewDetails_row_list_myCards);
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_addLoyalty);
//            title.setText(getloyalties.get(position).getCardname());
            if (getloyalties.get(position).getCardname() != null)
                details.setText(getloyalties.get(position).getCardname());
            ImageUtil.setImageWithResource(context,img,getloyalties.get(position).getImageurl());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Add_LoyaltyCard.this, TakeLoyaltyBarCode.class);
//                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Add_LoyaltyCard.this, DetailsLoyalty.class);
                    SharedPreferences.Editor editor = getSharedPreferences(Constant.PREFS_NAME, 0).edit();
                    editor.putString("addLoyaltyObject", new Gson().toJson(getloyalties.get(position)));
                    editor.putString("loyaltyPosition", getloyalties.get(position).getId());
                    editor.putBoolean("updateLoyaltyCard", false).apply();
                    intent.putExtra("addLoyaltyObject", new Gson().toJson(getloyalties.get(position)));
                    TakeLoyaltyNameDetails.is_Created = false;
                    DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty> filterList = new ArrayList<>();
                    for (int i = 0; i < getFilteredloyalties.size(); i++) {
                        if (getFilteredloyalties.get(i).getCardname().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty lCard = new com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty(getFilteredloyalties.get(i).getId(),
                                    getFilteredloyalties.get(i).getCardname(), getFilteredloyalties.get(i).getCarddetail(),
                                    getFilteredloyalties.get(i).getCompanyinfo(), getFilteredloyalties.get(i).getCompanylogo(), getFilteredloyalties.get(i).getImageurl());
                            filterList.add(lCard);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = getFilteredloyalties.size();
                    results.values = getFilteredloyalties;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                getloyalties = (ArrayList<com.citemenu.mystash.pojo.getcardslist_pojo.Getloyalty>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
