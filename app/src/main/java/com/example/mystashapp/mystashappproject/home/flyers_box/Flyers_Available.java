package com.example.mystashapp.mystashappproject.home.flyers_box;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.all_flyers_by_categ.Datum;
import com.example.mystashapp.mystashappproject.pojo.all_flyers_by_categ.GetAllFlyersWebService;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Flyers_Available extends AppCompatActivity {

    private GridView gridView;
    private ImageView imgBack;
    private ProgressDialog progDialog;
    private String catId;
    private GetAllFlyersWebService flyersWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyers__available);
        catId = getIntent().getStringExtra("catID");
        Log.d(Constant_util.LOG_TAG, "onCreate: " + catId);
        init();
//        gridView.setAdapter(new GridCustomAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Flyers_Available.this, Flyers_pdfview.class);
                intent.putExtra("pdfFile", flyersWebService.getBody().getData().get(position).getFilepath());
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.gridView);
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Loading...");
        progDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFlyers();
    }

    private void getAllFlyers() {
        progDialog.show();
        Call<GetAllFlyersWebService> call = WebServicesFactory.getInstance().getAllFlyers(Constant_util.ACTION_GET_ALL_FLYERS, catId);
        call.enqueue(new Callback<GetAllFlyersWebService>() {
            @Override
            public void onResponse(Call<GetAllFlyersWebService> call, Response<GetAllFlyersWebService> response) {
                progDialog.dismiss();
                flyersWebService = response.body();
                if (flyersWebService.getHeader().getSuccess().equals("1")) {
                    gridView.setAdapter(new GridCustomAdapter(Flyers_Available.this, flyersWebService.getBody().getData()));
                } else {
                    Toast toast = Toast.makeText(Flyers_Available.this, "" + flyersWebService.getHeader().getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<GetAllFlyersWebService> call, Throwable t) {
                progDialog.dismiss();
                Toast toast = Toast.makeText(Flyers_Available.this, "Something went wrong please try again later", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private class GridCustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        List<Datum> data;

        public GridCustomAdapter(Context context, List<Datum> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
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
                convertView = layoutInflater.inflate(R.layout.row_flyers_gridview, parent, false);
            TextView Label1 = (TextView) convertView.findViewById(R.id.testView_Flyer_row1);
            TextView Label2 = (TextView) convertView.findViewById(R.id.testView_Flyer_row2);
            ImageView listImage = (ImageView) convertView.findViewById(R.id.image1_flyer_row);
            if (!data.get(position).getExpirydate().isEmpty() && data.get(position).getExpirydate() != null)
                Label1.setText(data.get(position).getExpirydate());
            if (!data.get(position).getTitle().isEmpty() && data.get(position).getTitle() != null)
                Label2.setText(data.get(position).getTitle());
            if (!data.get(position).getImgpath().isEmpty() && data.get(position).getImgpath() != null)
                Picasso.with(context).load(data.get(position).getImgpath())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(listImage);
            return convertView;
        }
    }
}
