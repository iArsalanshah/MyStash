package com.citemenu.mystash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.categories_coupons_pojo.CategoriesCoupons;
import com.citemenu.mystash.pojo.categories_coupons_pojo.Category;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coupons extends AppCompatActivity {

    private ImageView imageViewBack;
    private ImageView img_Saved_Coupons;
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
        progDialog.setMessage(getString(R.string.loading));
        progDialog.show();
        //Retrofit2.0 call
        if (isFlyer) {
            getCategories("flyers");
        } else
            getCategories("coupons");
    }

    private void getCategories(String type) {
        Call<CategoriesCoupons> call = WebServicesFactory.getInstance().getcategoriesCoupons(Constant.ACTION_GET_CATEGORIES_COUPONS, type);
        call.enqueue(new Callback<CategoriesCoupons>() {
            @Override
            public void onResponse(Call<CategoriesCoupons> call, Response<CategoriesCoupons> response) {
                progDialog.dismiss();
                resp = response.body();
                if (resp.getHeader().getSuccess().equals("1")) {
                    listView.setAdapter(new ListAdapterCategories(Coupons.this, resp.getBody().getCategories()));
                } else {
                    Toast.makeText(Coupons.this, "" + resp.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesCoupons> call, Throwable t) {
                progDialog.dismiss();
                Toast.makeText(Coupons.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        img_Saved_Coupons = (ImageView) findViewById(R.id.img_Saved_Coupons);
        textTitleToolbar = (TextView) findViewById(R.id.textView_TitleToolbar_Coupons);
        imageViewBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        listView = (ListView) findViewById(R.id.listview_Coupons);

        //Setting of Toolbar Title;
        if (isFlyer) {
            textTitleToolbar.setText(getString(R.string.flyers));
            img_Saved_Coupons.setVisibility(View.GONE);
        }
    }

    private void clickEvents() {

        //ListView OnClick Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFlyer) {
                    Intent intent = new Intent(Coupons.this, Flyers_Available.class);
                    intent.putExtra("catID", resp.getBody().getCategories().get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Coupons.this, Coupons_Categories_Click.class);
                    intent.putExtra("textSavedClick", false);
                    intent.putExtra("catID", resp.getBody().getCategories().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        //Saved textView
        img_Saved_Coupons.setOnClickListener(new View.OnClickListener() {
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
            if (categories.get(position).getImgurl() != null && !categories.get(position).getImgurl().isEmpty())
                Picasso.with(context).load(categories.get(position).getImgurl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder_img_not_found)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                layoutBack.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
//                            Log.d(Constant.LOG_TAG, "Prepare Load");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                            Log.d(Constant.LOG_TAG, "Prepare Load");
                            }
                        });
            titleLabel.setText(categories.get(position).getName());

            return convertView;
        }

    }
}
