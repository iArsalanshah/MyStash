package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.MainActivity;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.delete_loyalty_card.DeleteLoyaltyCard;
import com.example.mystashapp.mystashappproject.pojo.getmycards_pojo.GetMycards;
import com.example.mystashapp.mystashappproject.pojo.getmycards_pojo.Loyaltycard;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
MyCards extends AppCompatActivity {
    ListView listView;
    private ProgressDialog progress;
    private ListViewMyCards adapterListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        init();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setMessage("Loading...");
        progress.show();
        //Retrofit2 Call
        getCards();
//        swipeMenuCreator();
    }

    private void getCards() {
        Users cid = CustomSharedPrefLogin.getUserObject(MyCards.this);
        Call<GetMycards> call = WebServicesFactory.getInstance().getMyCards(Constant_util.ACTION_GET_MY_LOYALTY_CARDS, cid.getId());
        call.enqueue(new Callback<GetMycards>() {
            @Override
            public void onResponse(Call<GetMycards> call, Response<GetMycards> response) {
                progress.dismiss();
                GetMycards getloyalty = response.body();
                if (getloyalty.getHeader().getSuccess().equals("1")) {
                    adapterListview = new ListViewMyCards(MyCards.this, getloyalty.getBody().getLoyaltycards());
                    listView.setAdapter(adapterListview);
                    adapterListview.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyCards.this, "" + getloyalty.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMycards> call, Throwable t) {
                progress.dismiss();
                Log.d(Constant_util.LOG_TAG, t.getMessage());
                Toast.makeText(MyCards.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imgBack_MyCards(View view) {
        startActivity(new Intent(MyCards.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void imgAdd_MyCards(View view) {
        startActivity(new Intent(this, Add_LoyaltyCard.class));
    }

    /*
    *
    *List Adapter Custom
    *
    * */
    private class ListViewMyCards extends BaseAdapter {
        private final ViewBinderHelper binderHelper;
        Activity context;
        List<Loyaltycard> loyaltycards;
        private LayoutInflater layoutInflater;

        public ListViewMyCards(MyCards context, List<Loyaltycard> loyaltycards) {
            this.context = context;
            this.loyaltycards = loyaltycards;
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

            if (loyaltycards.get(position).getFrontimage() != null && !loyaltycards.get(position).getFrontimage().isEmpty()) {
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
            final String item = getItem(position).toString();
            tvTitle.setText(loyaltycards.get(position).getCardname());
            tvDetails.setText(loyaltycards.get(position).getCardno());
            if (item != null) {
                binderHelper.bind(swipeLayout, getItem(position).toString());
            }
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    deleteItem(loyaltycards.get(position).getId());
                }
            });

            return convertView;
        }

        private void onDeleteLoyaltyCard(final String position) {

            // webservice should be called here.
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting...");
            dialog.show();
            Call<DeleteLoyaltyCard> call = WebServicesFactory.getInstance()
                    .deleteLoyaltyCard(Constant_util.ACTION_DELETE_LOYALTY_CARD,
                            position);
            call.enqueue(new Callback<DeleteLoyaltyCard>() {
                @Override
                public void onResponse(Call<DeleteLoyaltyCard> call, Response<DeleteLoyaltyCard> response) {
                    dialog.dismiss();
                    DeleteLoyaltyCard deleteLoyaltyCard = response.body();

                    if (deleteLoyaltyCard.getHeader().getSuccess().equals("1")) {
                        Toast.makeText(context, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                getCards();
                            }
                        });
                    } else {
                        Toast.makeText(context, "" + deleteLoyaltyCard.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeleteLoyaltyCard> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void deleteItem(final String position) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Confirmation");
            dialog.setMessage("delete loyalty card?");
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onDeleteLoyaltyCard(position);
                }
            });
            dialog.show();
        }
    }
}