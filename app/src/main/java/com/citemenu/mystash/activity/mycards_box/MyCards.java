package com.citemenu.mystash.activity.mycards_box;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.helper.Log;
import com.citemenu.mystash.activity.MainActivity;
import com.citemenu.mystash.pojo.delete_loyalty_card.DeleteLoyaltyCard;
import com.citemenu.mystash.pojo.getmycards_pojo.GetMycards;
import com.citemenu.mystash.pojo.getmycards_pojo.Loyaltycard;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
MyCards extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listView;
    private ProgressDialog progress;
    private ListViewMyCards adapterListview;
    private TextView alternateText;
    private SearchView searchView_cards;
    private List<Loyaltycard> loyaltycards;
    private List<Loyaltycard> mFilterloyaltycards;
    private Users cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        init();
        bindingData();
    }

    private void bindingData() {
        searchView_cards.setOnQueryTextListener(this);
        // Catch event on [x] button inside search view
        int searchCloseButtonId = searchView_cards.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView_cards.findViewById(searchCloseButtonId);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesoftkeyboard(v);
                searchView_cards.setQuery("", false);
            }
        });
        searchView_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView_cards.setIconified(false);
            }
        });
    }

//    private void swipeMenuCreator() {
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(MyCards.this);
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                deleteItem.setWidth(dp2px(45));
//                // set item title
//                deleteItem.setTitle("Delete");
//                deleteItem.setTitleColor(android.R.color.white);
//                // set item title fontsize
//                deleteItem.setTitleSize(14);
//                // set item title font color
//                deleteItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(deleteItem);
//            }
//
//            private int dp2px(int dp) {
//                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                        getResources().getDisplayMetrics());
//            }
//        };
//        listView.setMenuCreator(creator);
//// Left swipe direction
//        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        // delete item webService
//                        Toast.makeText(MyCards.this, "Deleted", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });
//    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView_MyCards);

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();

        alternateText = (TextView) findViewById(R.id.alternateText);
        searchView_cards = (SearchView) findViewById(R.id.searchView_cards);
        loyaltycards = new ArrayList<>();
        mFilterloyaltycards = new ArrayList<>();
        adapterListview = new ListViewMyCards(MyCards.this);
        listView.setAdapter(adapterListview);
        cid = CustomSharedPref.getUserObject(MyCards.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Retrofit2 Call
        getCards();
    }

    private void getCards() {
//        Users cid = CustomSharedPref.getUserObject(MyCards.this);
        Log.d(cid.getId());
        Call<GetMycards> call = WebServicesFactory.getInstance().getMyCards(Constant.ACTION_GET_MY_LOYALTY_CARDS, cid.getId());
        call.enqueue(new Callback<GetMycards>() {
            @Override
            public void onResponse(Call<GetMycards> call, Response<GetMycards> response) {
                progress.dismiss();
                GetMycards getloyalty = response.body();
                if (getloyalty.getHeader().getSuccess().equals("1")) {
                    loyaltycards = getloyalty.getBody().getLoyaltycards();
                    mFilterloyaltycards = getloyalty.getBody().getLoyaltycards();
                    adapterListview.notifyDataSetChanged();
                } else {
                    alternateText.setVisibility(View.VISIBLE);
                    searchView_cards.setVisibility(View.GONE);
//                    Toast.makeText(MyCards.this, "" + getloyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMycards> call, Throwable t) {
                progress.dismiss();
                searchView_cards.setVisibility(View.GONE);
                Toast.makeText(MyCards.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imgBack_MyCards(View view) {
        startActivity(new Intent(MyCards.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void imgAdd_MyCards(View view) {
        startActivity(new Intent(this, Add_LoyaltyCard.class));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapterListview.getFilter().filter(newText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void hidesoftkeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyCards.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /*
        *
        *List Adapter Custom
        *
        * */
    private class ListViewMyCards extends BaseAdapter implements Filterable {
        private final ViewBinderHelper binderHelper;
        Context context;
        private LayoutInflater layoutInflater;
        private ValueFilter valueFilter;

        ListViewMyCards(Context context) {
            this.context = context;
            binderHelper = new ViewBinderHelper();
            binderHelper.setOpenOnlyOne(true);
        }

        @Override
        public int getCount() {
            return loyaltycards.size();
        }

        @Override
        public Object getItem(int position) {
            return loyaltycards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.row_mycards_listview, parent, false);
            ImageView img = (ImageView) convertView.findViewById(R.id.imageView_row_list_myCards);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle_row_list_myCards);
            TextView tvDetails = (TextView) convertView.findViewById(R.id.textViewDetails_row_list_myCards);
            final FrameLayout deleteView = (FrameLayout) convertView.findViewById(R.id.delete_layout);
            SwipeRevealLayout swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_addLoyalty);

            if (!TextUtils.isEmpty(loyaltycards.get(position).getFrontimage())) {
                Picasso.with(context)
                        .load(loyaltycards.get(position).getFrontimage())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img);
            }
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyCards.this, DetailsLoyalty.class);
                    intent.putExtra("editLoyaltyObject", new Gson().toJson(loyaltycards.get(position)));
                    DetailsLoyalty.is_Edit = true;
                    startActivity(intent);
                }
            });
            /*Card Name*/
            if (!TextUtils.isEmpty(loyaltycards.get(position).getCarddetail())) {
                tvTitle.setText(loyaltycards.get(position).getCarddetail());
            }
            /*Your Name*/
            if (!TextUtils.isEmpty(loyaltycards.get(position).getCardname())) {
                tvDetails.setText(loyaltycards.get(position).getCardname());
            }

            final String item = getItem(position).toString();
            if (item != null) {
                binderHelper.bind(swipeLayout, item);
            }
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    deleteItem(loyaltycards.get(position).getId());
                }
            });
            Log.d(new Gson().toJson(loyaltycards.get(position)));
            return convertView;
        }

        private void onDeleteLoyaltyCard(final String position) {

            // webservice should be called here.
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting...");
            dialog.show();
            Call<com.citemenu.mystash.pojo.delete_loyalty_card.DeleteLoyaltyCard> call = WebServicesFactory.getInstance()
                    .deleteLoyaltyCard(Constant.ACTION_DELETE_LOYALTY_CARD,
                            position);
            call.enqueue(new Callback<DeleteLoyaltyCard>() {
                @Override
                public void onResponse(Call<DeleteLoyaltyCard> call, Response<DeleteLoyaltyCard> response) {
                    dialog.dismiss();
                    DeleteLoyaltyCard deleteLoyaltyCard = response.body();

                    if (deleteLoyaltyCard.getHeader().getSuccess().equals("1")) {
                        for (int i = 0; i < loyaltycards.size(); i++) {
                            if (loyaltycards.get(i).getId().equals(position)) {
                                loyaltycards.remove(i);
                            }
                        }
                        for (int i = 0; i < mFilterloyaltycards.size(); i++) {
                            if (mFilterloyaltycards.get(i).getId().equals(position)) {
                                mFilterloyaltycards.remove(i);
                            }
                        }
                        Toast.makeText(context, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        adapterListview.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeleteLoyaltyCard> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteItem(final String position) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Are you sure you want to remove this loyalty card");
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onDeleteLoyaltyCard(position);
                }
            });
            dialog.show();
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
                    ArrayList<Loyaltycard> filterList = new ArrayList<>();
                    for (int i = 0; i < mFilterloyaltycards.size(); i++) {
                        if (mFilterloyaltycards.get(i).getCarddetail().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            Loyaltycard lCard = new com.citemenu.mystash.pojo.getmycards_pojo.Loyaltycard(mFilterloyaltycards.get(i).getId(), mFilterloyaltycards.get(i).getLoyaltyId(),
                                    mFilterloyaltycards.get(i).getCid(), mFilterloyaltycards.get(i).getCardno(), mFilterloyaltycards.get(i).getNotes(),
                                    mFilterloyaltycards.get(i).getIsRegisterdCompany(), mFilterloyaltycards.get(i).getCardname(), mFilterloyaltycards.get(i).getCarddetail(),
                                    mFilterloyaltycards.get(i).getCompanyinfo(), mFilterloyaltycards.get(i).getCompanylogo(), mFilterloyaltycards.get(i).getImageurl(),
                                    mFilterloyaltycards.get(i).getList(), mFilterloyaltycards.get(i).getFrontimage(), mFilterloyaltycards.get(i).getBackimage());
                            filterList.add(lCard);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mFilterloyaltycards.size();
                    results.values = mFilterloyaltycards;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                loyaltycards = (ArrayList<Loyaltycard>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}