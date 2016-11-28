package com.citemenu.mystash.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.custom_view.DividerItemDecoration;
import com.citemenu.mystash.helper.Log;
import com.citemenu.mystash.pojo.get_bills.Bill;
import com.citemenu.mystash.pojo.get_bills.GetBillsWS;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadedBillsHistory extends AppCompatActivity {

    private List<Bill> billList;
    private BillHistoryAdapter mAdapter;
    private TextView tvAlternateText;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_bills_history);
        billList = new ArrayList<>();
        mAdapter = new BillHistoryAdapter();
        tvAlternateText = (TextView) findViewById(R.id.tv_billsHistory_altText);
        recyclerView = (RecyclerView) findViewById(R.id.bill_history_recycler_view);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        String userId = CustomSharedPref.getUserObject(this).getId();
        getBills("get_bills", userId);
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void getBills(String action, String user_id) {
        dialog.show();
        Call<GetBillsWS> call = WebServicesFactory.getInstance().getBills(action, user_id);
        call.enqueue(new Callback<GetBillsWS>() {
            @Override
            public void onResponse(Call<GetBillsWS> call, Response<GetBillsWS> response) {
                dialog.dismiss();
                GetBillsWS billsWS = response.body();
                if (billsWS == null) {
                    Toast.makeText(UploadedBillsHistory.this, "Found Null in getBills service", Toast.LENGTH_SHORT).show();
                } else if (billsWS.getHeader().getSuccess().equals("1")) {
                    billList = billsWS.getBody().getBills();
                    mAdapter.notifyDataSetChanged();
                    if (tvAlternateText.getVisibility() == View.VISIBLE)
                        tvAlternateText.setVisibility(View.GONE);
                    if (recyclerView.getVisibility() == View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);

                } else {
                    if (tvAlternateText.getVisibility() == View.GONE)
                        tvAlternateText.setVisibility(View.VISIBLE);
                    if (recyclerView.getVisibility() == View.VISIBLE)
                        recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetBillsWS> call, Throwable t) {
                dialog.dismiss();
                Log.d("RETROFIT ON FAILURE: " + t);
                Toast.makeText(UploadedBillsHistory.this,
                        "Something went wrong. Please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class BillHistoryAdapter extends RecyclerView.Adapter<BillHistoryAdapter.MyViewHolder> {

        BillHistoryAdapter() {

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bill_history_list_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (billList.get(position).getAmount() != null)
                holder.amount.setText(billList.get(position).getAmount());
            if (billList.get(position).getStatus() != null)
                holder.status.setText(billList.get(position).getStatus());
            if (billList.get(position).getInvoiceNo() != null)
                holder.invoice.setText(billList.get(position).getInvoiceNo());
            if (billList.get(position).getComment() != null)
                holder.details.setText(billList.get(position).getComment().toString());
            if (billList.get(position).getResName() != null)
                holder.name.setText(billList.get(position).getResName());
        }

        @Override
        public int getItemCount() {
            return billList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, invoice, amount, status, details;

            MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.bill_history_item_name);
                invoice = (TextView) view.findViewById(R.id.bill_history_item_invoice);
                amount = (TextView) view.findViewById(R.id.bill_history_item_amount);
                status = (TextView) view.findViewById(R.id.bill_history_status);
                details = (TextView) view.findViewById(R.id.bill_history_item_description);
            }
        }
    }
}
