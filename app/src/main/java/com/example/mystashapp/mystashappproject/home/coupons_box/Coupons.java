package com.example.mystashapp.mystashappproject.home.coupons_box;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.flyers_box.Flyers_Available;
import com.example.mystashapp.mystashappproject.pojo.categories_coupons_pojo.CategoriesCoupons;
import com.example.mystashapp.mystashappproject.pojo.categories_coupons_pojo.Category;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coupons extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textSaved;
    private TextView textTitleToolbar;
    private ListView listView;
    private boolean isFlyer;
    private ProgressDialog progDialog;
    private CategoriesCoupons resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        isFlyer = getIntent().getBooleanExtra("isFlyer", false);
        init();
        clickEvents();
//        listView.setAdapter(new ListAdapterCategories(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Loading...");
        progDialog.show();
        //Retrofit2.0 call
        getCategories();
    }

    private void getCategories() {
        Call<CategoriesCoupons> call = WebServicesFactory.getInstance().getcategoriesCoupons(Constant_util.ACTION_GET_CATEGORIES_COUPONS);
        call.enqueue(new Callback<CategoriesCoupons>() {
            @Override
            public void onResponse(Call<CategoriesCoupons> call, Response<CategoriesCoupons> response) {
                progDialog.dismiss();
                resp = response.body();
                if (resp.getHeader().getSuccess().equals("1")) {
                    listView.setAdapter(new ListAdapterCategories(Coupons.this, resp.getBody().getCategories()));
                } else {
                    Toast.makeText(Coupons.this, "Found 0", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesCoupons> call, Throwable t) {
                progDialog.dismiss();
                Toast.makeText(Coupons.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        textSaved = (TextView) findViewById(R.id.textView_Saved_Coupons);
        textTitleToolbar = (TextView) findViewById(R.id.textView_TitleToolbar_Coupons);
        imageViewBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        listView = (ListView) findViewById(R.id.listview_Coupons);

        //Setting of Toolbar Title;
        if (isFlyer) {
            textTitleToolbar.setText("Flyers");
        }
    }

    private void clickEvents() {

        //ListView OnClick Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFlyer) {
                    Toast.makeText(Coupons.this, "" + position, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Coupons.this, Flyers_Available.class));
                } else {
                    Toast.makeText(Coupons.this, "" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Coupons.this, Coupons_Categories_Click.class);
                    intent.putExtra("textSavedClick", false);
                    intent.putExtra("catID", resp.getBody().getCategories().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        //Saved textView
        textSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupons.this, Coupons_Categories_Click.class);
                intent.putExtra("textSavedClick", true);
                startActivity(intent);
            }
        });

        //Back imageView in Toolbar
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ListAdapterCategories extends BaseAdapter {
        private final List<Category> categories;
        Context context;
        LayoutInflater layoutInflater;

        public ListAdapterCategories(Context context, List<Category> categories) {
            this.context = context;
            this.categories = categories;

        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.row_categories_listview, parent, false);

            final RelativeLayout layoutBack = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_cat_row_listview);
            TextView titleLabel = (TextView) convertView.findViewById(R.id.textView_row_Categories_Listview);
            Picasso.with(context).load(categories.get(position).getImgurl())
                    .placeholder(R.drawable.placeholder_shadow)
                    .error(R.drawable.placeholder_shadow)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            layoutBack.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d(Constant_util.LOG_TAG, "Prepare Load");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.d(Constant_util.LOG_TAG, "Prepare Load");
                        }
                    });
            titleLabel.setText(categories.get(position).getName());

            return convertView;
        }

    }
}
