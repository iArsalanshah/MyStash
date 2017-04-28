package com.citemenu.mystash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.all_flyers_by_categ.Datum;
import com.citemenu.mystash.pojo.all_flyers_by_categ.GetAllFlyersWebService;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

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
    private List<Datum> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyers__available);
        catId = getIntent().getStringExtra("catID");
//        Log.d(Constant.LOG_TAG, "onCreate: " + catId);
        init();
//        gridView.setAdapter(new GridCustomAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Flyers_Available.this, Flyers_pdfview.class);
                intent.putExtra("flyerTile", data.get(position).getTitle());
                intent.putExtra("pdfFile", flyersWebService.getBody().getData().get(position).getFilepath());
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        Call<GetAllFlyersWebService> call = WebServicesFactory.getInstance().getAllFlyers(Constant.ACTION_GET_ALL_FLYERS, catId);
        call.enqueue(new Callback<GetAllFlyersWebService>() {
            @Override
            public void onResponse(Call<GetAllFlyersWebService> call, Response<GetAllFlyersWebService> response) {
                progDialog.dismiss();
                flyersWebService = response.body();
                if (flyersWebService.getHeader().getSuccess().equals("1")) {
                    data = flyersWebService.getBody().getData();
                    gridView.setAdapter(new GridCustomAdapter(Flyers_Available.this));
                } else {
                    Toast toast = Toast.makeText(Flyers_Available.this, "" + flyersWebService.getHeader().getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<GetAllFlyersWebService> call, Throwable t) {
                progDialog.dismiss();
                Toast toast = Toast.makeText(Flyers_Available.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private class GridCustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;

        GridCustomAdapter(Context context) {
            this.context = context;
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
            if (data.get(position).getExpirydate() != null &&
                    !data.get(position).getExpirydate().isEmpty()) {
                if (!data.get(position).getExpirydate().startsWith("00")) {
                    Label1.setText(data.get(position).getExpirydate());
                } else {
                    Label1.setText("No Expiry");
                }
            }
            if (data.get(position).getTitle() != null &&
                    !data.get(position).getTitle().isEmpty())
                Label2.setText(data.get(position).getTitle());
            ImageUtil.setImageWithResource(context,listImage,data.get(position).getImgpath());
//            if (data.get(position).getImg_path() != null &&
//                    !data.get(position).getImg_path().isEmpty())
//                Picasso.with(context).load(data.get(position).getImgpath())
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.placeholder)
//                        .into(listImage);
            return convertView;
        }
    }
}
