package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setMessage("Loading...");
        progress.show();
        //Retrofit2 Call
        getCards();
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
                    listView.setAdapter(new ListViewMyCards(MyCards.this, getloyalty.getBody().getLoyaltycards()));
                } else {
                    Toast.makeText(MyCards.this, "Found 0", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMycards> call, Throwable t) {
                progress.dismiss();
                Log.d(Constant_util.LOG_TAG, t.getMessage());
            }
        });
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView_MyCards);
        progress = new ProgressDialog(this);
    }

    public void imgBack_MyCards(View view) {
        finish();
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
        Activity context;
        List<Loyaltycard> loyaltycards;
        private LayoutInflater layoutInflater;

        public ListViewMyCards(MyCards context, List<Loyaltycard> loyaltycards) {
            this.context = context;
            this.loyaltycards = loyaltycards;
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
            TextView title = (TextView) convertView.findViewById(R.id.textViewTitle_row_list_myCards);
            TextView details = (TextView) convertView.findViewById(R.id.textViewDetails_row_list_myCards);
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_addLoyalty);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyCards.this, DetailsLoyalty.class);
                    intent.putExtra("editLoyaltyObject", new Gson().toJson(loyaltycards.get(position)));
                    DetailsLoyalty.is_Edit = true;
                    startActivity(intent);
                }
            });
            title.setText(loyaltycards.get(position).getCardname());
            details.setText(loyaltycards.get(position).getCarddetail());
            if (loyaltycards.get(position).getImageurl() != null && !loyaltycards.get(position).getImageurl().isEmpty())
                Picasso.with(context)
                        .load(loyaltycards.get(position).getImageurl())
                        .placeholder(R.drawable.placeholder_shadow)
                        .error(R.drawable.img_profile)
                        .into(img);

            return convertView;
        }
    }
}