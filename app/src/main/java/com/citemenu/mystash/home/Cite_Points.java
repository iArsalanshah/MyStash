package com.citemenu.mystash.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.pojo.pojo_cite_points.CitePointsTransactions;
import com.citemenu.mystash.pojo.pojo_cite_points.History;
import com.citemenu.mystash.singleton.MyCitePoints;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cite_Points extends AppCompatActivity {
    ListView listView;
    TextView tvCPNumber;
    private ImageView img_AddIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cite_point);
        init();
        img_AddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Upload bill section*/
                startActivity(new Intent(Cite_Points.this, AddBillDetails.class));
            }
        });
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView_citePointsHistory);
        tvCPNumber = (TextView) findViewById(R.id.textView_CitePointsNumber);
        img_AddIcon = (ImageView) findViewById(R.id.img_add_icon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<History> histories = MyCitePoints.getInstance().getCitePointsHistory();
        String totalPoints = MyCitePoints.getInstance().getTotalPoints();
        if (histories != null && totalPoints != null) {
            listView.setAdapter(new CitePointsAdapter(Cite_Points.this, histories));
            tvCPNumber.setText(totalPoints);
        } else
            getCitePoints();
    }

    private void getCitePoints() {
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.show();
        String cid = CustomSharedPref.getUserObject(this).getId();
        Call<CitePointsTransactions> call = WebServicesFactory.getInstance().getCitePoints(Constant_util.ACTION_GET_CITE_POINTS, cid);
        call.enqueue(new Callback<CitePointsTransactions>() {
            @Override
            public void onResponse(Call<CitePointsTransactions> call, Response<CitePointsTransactions> response) {
                dlg.dismiss();
                CitePointsTransactions transactions = response.body();
                if (transactions.getHeader().getSuccess().equals("1")) {
                    try {
                        if (!transactions.getBody().getHistory().isEmpty() && transactions.getBody().getHistory().size() != 0) {
                            listView.setAdapter(new CitePointsAdapter(Cite_Points.this, transactions.getBody().getHistory()));
                            tvCPNumber.setText(transactions.getBody().getTotalpoints());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(Cite_Points.this, "" + transactions.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CitePointsTransactions> call, Throwable t) {
                dlg.dismiss();
                Toast.makeText(Cite_Points.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imgBack(View view) {
        finish();
    }

    public void useYourPointClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mystash.ca")));
    }

    private class CitePointsAdapter extends BaseAdapter {
        private final List<History> history;
        Context context;
        private LayoutInflater inflater;

        CitePointsAdapter(Context context, List<History> history) {
            this.context = context;
            this.history = history;
        }

        @Override
        public int getCount() {
            return history.size();
        }

        @Override
        public Object getItem(int position) {
            return history.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = inflater.inflate(R.layout.row_citepoints_listview, parent, false);

            TextView tvPoints = (TextView) convertView.findViewById(R.id.textView_row_citepoint);
            TextView tvBusinessName = (TextView) convertView.findViewById(R.id.textView_row_citepoint_businessName);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_row_citePoints_dateTime);

            tvPoints.setText(history.get(position).getRedeemedpoints());
            tvBusinessName.setText(history.get(position).getBusinessName());
            tvTime.setText(history.get(position).getDate());
            if (history.get(position).getType().equals("Redeemed")) {
                tvPoints.setTextColor(getColor(R.color.colorPrimaryDark));
            } else {
                tvPoints.setTextColor(getColor(R.color.colorCitePointsTypeGreen));
            }
            return convertView;
        }
    }
}
