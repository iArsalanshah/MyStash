package com.citemenu.mystash.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.helper.FromXML;
import com.citemenu.mystash.pojo.meesages.Datum;
import com.citemenu.mystash.pojo.meesages.MessagesWebService;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Messages extends AppCompatActivity {
    private ListView msgsList;
    private String cid;
    private List<Datum> listData;
    private MessagesAdapter adapter;
    private TextView altText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        cid = CustomSharedPref.getUserObject(this).getId();
        msgsList = (ListView) findViewById(R.id.listview_Messages);
        altText = (TextView) findViewById(R.id.tvAltText);
        listData = new ArrayList<>();
        adapter = new MessagesAdapter(this);
        msgsList.setAdapter(adapter);
        getMessages(cid);
    }

    private void getMessages(String cid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        Call<MessagesWebService> call = WebServicesFactory.getInstance().getMessages(Constant_util.ACTION_GET_MESSAGES, cid);
        call.enqueue(new Callback<MessagesWebService>() {
            @Override
            public void onResponse(Call<MessagesWebService> call, Response<MessagesWebService> response) {
                dialog.dismiss();
                MessagesWebService messagesWebService = response.body();
                if (messagesWebService.getHeader().getSuccess().equals("1")) {
                    if (!messagesWebService.getBody().getData().isEmpty()) {
                        listData = messagesWebService.getBody().getData();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(Messages.this, messagesWebService.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessagesWebService> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Messages.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @FromXML
    public void backImage_mystashReviews(View view) {
        onBackPressed();
    }

    private class MessagesAdapter extends BaseAdapter {
        private final ViewBinderHelper binderHelper;
        private Context context;
        private LayoutInflater inflater;

        MessagesAdapter(Context context) {
            this.context = context;
            binderHelper = new ViewBinderHelper();
            binderHelper.setOpenOnlyOne(true);
        }

        @Override
        public int getCount() {
            if (listData.size() > 0) {
                altText.setVisibility(View.GONE);
            } else {
                altText.setVisibility(View.VISIBLE);
            }
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
            SwipeRevealLayout swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            FrameLayout deleteView = (FrameLayout) convertView.findViewById(R.id.delete_layout);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.textView_Review_Title);
            FrameLayout parentContainer = (FrameLayout) convertView.findViewById(R.id.layout_itemMessage);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.textView_Review_Desc);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_Review_dateTime);
            if (listPosition.getBusinessName() != null)
                tvTitle.setText(listPosition.getBusinessName());
            if (listPosition.getMessage() != null)
                tvDesc.setText(listPosition.getMessage().trim());
            tvTime.setText(setTime(listPosition.getSentTime()));
            if (listPosition.getSentTime() != null)
                setTime(listPosition.getSentTime());
            parentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listPosition.getMessage() != null && listPosition.getBusinessName() != null)
                        new AlertDialog.Builder(context)
                                .setMessage(listPosition.getMessage())
                                .setTitle(listPosition.getBusinessName())
                                .show();
                }
            });

            String item = getItem(position).toString();
            if (item != null) {
                binderHelper.bind(swipeLayout, item);
            }
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    deleteItem(position);
                }
            });
            return convertView;
        }

        private void deleteItem(final int positionId) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Are you sure you want to remove this Message");
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listData.remove(positionId);
                    adapter.notifyDataSetChanged();
                }
            });
            dialog.show();
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
