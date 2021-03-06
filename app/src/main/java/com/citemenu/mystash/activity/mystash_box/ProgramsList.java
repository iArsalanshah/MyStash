package com.citemenu.mystash.activity.mystash_box;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby;
import com.citemenu.mystash.pojo.program_stamps.Datum;
import com.citemenu.mystash.pojo.program_stamps.ProgramsStamps;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramsList extends AppCompatActivity {

    private ListView listview;
    private String programID;
    private TextView altText;
    private ProgramsStamps stampsObj;
    private ProgressDialog progress;
    private ImageView imgBack;
    private Searchnearby sbID;
    private String isStampReward;
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_list);

        //Getting Programs
        programID = getIntent().getExtras().getString("programID");
        sbID = new Gson().fromJson(programID, Searchnearby.class);
//        isStampReward = getIntent().getStringExtra("");
        //initialization of objects
        init();

        //click events
        clickEvents();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.loading));
        progress.show();
        getStamps();
    }

    private void getStamps() {
        Users users_obj = CustomSharedPref.getUserObject(ProgramsList.this);

        Call<ProgramsStamps> call = WebServicesFactory.getInstance().getStamps(Constant.ACTION_GET_STAMPS, users_obj.getId(), sbID.getId());
//        Log.d(Constant.LOG_TAG, "getStamps: id:" + sbID.getId() + " uid" + sbID.getUid() + " cid" + users_obj.getId());
        call.enqueue(new Callback<ProgramsStamps>() {
            @Override
            public void onResponse(Call<ProgramsStamps> call, Response<ProgramsStamps> response) {
                stampsObj = response.body();
                progress.dismiss();
                if (stampsObj.getHeader().getSuccess().equals("1")) {
                    if (stampsObj.getBody().getData().size() == 0) {
                        listview.setVisibility(View.GONE);
                        altText.setVisibility(View.VISIBLE);
                    } else {
                        listview.setVisibility(View.VISIBLE);
                        altText.setVisibility(View.GONE);
                        listview.setAdapter(new CustomAdapterPrograms(ProgramsList.this, stampsObj.getBody().getData()));
                    }
                } else {
                    listview.setVisibility(View.GONE);
                    altText.setVisibility(View.VISIBLE);
//                    Log.d(Constant.LOG_TAG, stampsObj.getHeader().getMessage());
                    Toast.makeText(ProgramsList.this, "" + stampsObj.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProgramsStamps> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(ProgramsList.this, getString(R.string.message_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEvents() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ProgramsList.this, Program_Details.class);
                Datum s = stampsObj.getBody().getData().get(position);
//              String jsonObj = new Gson().toJson(s);
                i.putExtra("pdata", new Gson().toJson(s));
                i.putExtra("programOtherDetails", programID);
                startActivity(i);
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
        listview = (ListView) findViewById(R.id.listview_Programs);
        altText = (TextView) findViewById(R.id.list_programs_altText);
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class CustomAdapterPrograms extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        List<Datum> data;

        CustomAdapterPrograms(Context context, List<Datum> data) {
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
                convertView = layoutInflater.inflate(R.layout.row_programs_listview, parent, false);

            // we would use this only data
            TextView listTitle = (TextView) convertView.findViewById(R.id.textViewTitle_row_programslist);

            //no need for now bcs we dont have data for desc and image
            TextView listDesc = (TextView) convertView.findViewById(R.id.textViewDesc_row_programslist);
            ImageView listImage = (ImageView) convertView.findViewById(R.id.imageview_row_programslist);

            //setting Program Title
            if (data.get(position).getProgramname() != null)
                listTitle.setText(data.get(position).getProgramname());

            ImageUtil.setImageWithResource(context, listImage, sbID.getLogourl());
//            if (sbID.getLogourl() != null && !sbID.getLogourl().isEmpty())
//                Picasso.with(context).load(sbID.getLogourl())
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.placeholder)
//                        .into(listImage);
            if (data.get(position).getDesc() != null && !data.get(position).getDesc().equals("")) {
                listDesc.setText(data.get(position).getDesc());
            }
            return convertView;
        }
    }
}
