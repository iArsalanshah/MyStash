package com.citemenu.mystash.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.pojo.meesages.Datum;
import com.citemenu.mystash.utils.CustomSharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Messages extends AppCompatActivity {
    ListView msgsList;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        cid = CustomSharedPref.getUserObject(this).getId();
        msgsList = (ListView) findViewById(R.id.listview_Messages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessages(cid);
    }

    private void getMessages(String cid) {
        Call<com.citemenu.mystash.pojo.meesages.MessagesWebService> call = com.citemenu.mystash.webservicefactory.WebServicesFactory.getInstance().getMessages(com.citemenu.mystash.helper.Constant_util.ACTION_GET_MESSAGES, cid);
        call.enqueue(new Callback<com.citemenu.mystash.pojo.meesages.MessagesWebService>() {
            @Override
            public void onResponse(Call<com.citemenu.mystash.pojo.meesages.MessagesWebService> call, Response<com.citemenu.mystash.pojo.meesages.MessagesWebService> response) {
                com.citemenu.mystash.pojo.meesages.MessagesWebService messagesWebService = response.body();
                if (messagesWebService.getHeader().getSuccess().equals("1")) {
                    if (!messagesWebService.getBody().getData().isEmpty()
                            && !messagesWebService.getBody().getData().equals("")) {
                        msgsList.setAdapter(new MessagesAdapter(Messages.this, messagesWebService.getBody().getData()));
                    }
                } else {
                    Toast.makeText(Messages.this, messagesWebService.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.citemenu.mystash.pojo.meesages.MessagesWebService> call, Throwable t) {
                Toast.makeText(Messages.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backImage_mystashReviews(View view) {
        finish();
    }

    private class MessagesAdapter extends BaseAdapter {
        List<Datum> listData;
        Context context;
        private LayoutInflater inflater;

        public MessagesAdapter(Context context, List<Datum> data) {
            this.context = context;
            listData = data;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
                convertView = inflater.inflate(R.layout.row_review_details_listview, parent, false);

            final Datum listPosition = listData.get(position);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.textView_Review_Title);
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layout_itemMessage);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.textView_Review_Desc);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_Review_dateTime);
            if (listPosition.getBusinessName() != null)
                tvTitle.setText(listPosition.getBusinessName());
            if (listPosition.getMessage() != null)
                tvDesc.setText(listPosition.getMessage().trim());
            tvTime.setText(setTime(listPosition.getSentTime()));
            if (listPosition.getSentTime() != null)
                setTime(listPosition.getSentTime());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listPosition.getMessage() != null && listPosition.getBusinessName() != null)
                        new AlertDialog.Builder(context)
                                .setMessage(listPosition.getMessage())
                                .setTitle(listPosition.getBusinessName())
                                .show();
                }
            });
            return convertView;
        }

        private String setTime(String sentTime) {
            SimpleDateFormat default_ = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.US);
            Date t1 = null;
            try {
                t1 = default_.parse(sentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            default_.applyPattern("MMM dd");
            return default_.format(t1);
        }
    }
}