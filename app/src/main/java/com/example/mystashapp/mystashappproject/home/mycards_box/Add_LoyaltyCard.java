package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.GetCardsList;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.Getloyalty;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_LoyaltyCard extends AppCompatActivity {

    private ListView listview;
    private ArrayList<String> stTitle;
    private ArrayList<String> stDetails;
    private ProgressDialog progress;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loyaltycard);
        init();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(Add_LoyaltyCard.this, CreateACard.class);
                startActivity(createIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setMessage("Loading...");
        progress.show();
        //Retrofit2.0 Call
        getAddLoyaltyList();
    }

    private void getAddLoyaltyList() {
        Users cid = CustomSharedPrefLogin.getUserObject(Add_LoyaltyCard.this);
        Log.d(Constant_util.LOG_TAG, cid.getId());
        Call<GetCardsList> call = WebServicesFactory.getInstance().getCardsList(Constant_util.ACTION_GET_LOYALTY_CARDS_LIST, cid.getId());
        call.enqueue(new Callback<GetCardsList>() {
            @Override
            public void onResponse(Call<GetCardsList> call, Response<GetCardsList> response) {
                progress.dismiss();
                GetCardsList getCardsList = response.body();
                if (getCardsList.getHeader().getSuccess().equals("1")) {
                    listview.setAdapter(new AddLoyaltyAdapter(Add_LoyaltyCard.this, getCardsList.getBody().getGetloyalty()));
                } else {
                    Toast.makeText(Add_LoyaltyCard.this, "Something went wrong. try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCardsList> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(Add_LoyaltyCard.this, "Something went wrong with internet connection", Toast.LENGTH_SHORT).show();
                if (t.getMessage() != null)
                    Log.d(Constant_util.LOG_TAG, "" + t.getMessage());
            }
        });
    }

    private void init() {
        listview = (ListView) findViewById(R.id.listView_Add_LoyaltyCard);
        progress = new ProgressDialog(this);
        createButton = (Button) findViewById(R.id.button_create_loyalty);
    }

    public void imgBack_MyCards(View view) {
        finish();
    }

    private class AddLoyaltyAdapter extends BaseAdapter {
        List<Getloyalty> getloyalties;
        private LayoutInflater layoutInflater;
        private Activity context;

        public AddLoyaltyAdapter(Add_LoyaltyCard add_loyaltyCard, List<Getloyalty> getloyalty) {
            context = add_loyaltyCard;
            getloyalties = getloyalty;
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
            TextView title = (TextView) convertView.findViewById(R.id.textViewTitle_row_list_myCards);
            TextView details = (TextView) convertView.findViewById(R.id.textViewDetails_row_list_myCards);
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_addLoyalty);
            title.setText(getloyalties.get(position).getCardname());
            details.setText(getloyalties.get(position).getCarddetail());
            if (getloyalties.get(position).getImageurl() != null && !getloyalties.get(position).getImageurl().isEmpty())
                Picasso.with(context).load(getloyalties.get(position).getImageurl())
                        .error(R.drawable.placeholder_shadow)
                        .placeholder(R.drawable.placeholder_shadow)
                        .into(img);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Add_LoyaltyCard.this, takeLoyaltyBarCode.class);
//                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Add_LoyaltyCard.this, DetailsLoyalty.class);
                    SharedPreferences.Editor editor = getSharedPreferences(Constant_util.PREFS_NAME, 0).edit();
                    editor.putString("addLoyaltyObject", new Gson().toJson(getloyalties.get(position)));
                    editor.putString("loyaltyPosition", getloyalties.get(position).getId()).apply();
                    intent.putExtra("addLoyaltyObject", new Gson().toJson(getloyalties.get(position)));
                    DetailsLoyalty.is_Edit = false;
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
