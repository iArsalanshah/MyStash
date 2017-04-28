package com.citemenu.mystash.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.helper.FromXML;
import com.citemenu.mystash.helper.Log;
import com.citemenu.mystash.pojo.delete_notification.DeleteNotificationWS;
import com.citemenu.mystash.pojo.meesages.Datum;
import com.citemenu.mystash.pojo.meesages.MessagesWebService;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.DateUtils;
import com.citemenu.mystash.utils.LogUtil;
import com.citemenu.mystash.utils.ToastUtil;
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

public class Messages extends BaseActivity {
    private String cid;
    private List<Datum> listData;
    private MessagesAdapter adapter;
    private TextView altText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        cid = CustomSharedPref.getUserObject(this).getId();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ListView mListView = (ListView) findViewById(R.id.listview_Messages);
        altText = (TextView) findViewById(R.id.tvAltText);
        listData = new ArrayList<>();
        adapter = new MessagesAdapter(this);
        mListView.setAdapter(adapter);
        getMessages(cid);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages(cid);
            }
        });
    }

    private void getMessages(String cid) {
        showProgressDialog();
        Call<MessagesWebService> call = WebServicesFactory.getInstance().getMessagesAPI(Constant.ACTION_GET_MESSAGES, cid);
        call.enqueue(new Callback<MessagesWebService>() {
            @Override
            public void onResponse(Call<MessagesWebService> call, Response<MessagesWebService> response) {
                dismissProgressDialog();
                MessagesWebService messagesWebService = response.body();
                if (messagesWebService == null || messagesWebService.getHeader() == null
                        || messagesWebService.getHeader().getSuccess() == null) {
                    ToastUtil.showShortMessage(context, Constant.RESPONSE_NULL);
                } else if (messagesWebService.getHeader().getSuccess().equals("1")) {
                    listData = messagesWebService.getBody().getData();
                    if (listData.size() > 0) {
                        altText.setVisibility(View.GONE);
                    } else {
                        listData = new ArrayList<>();
                        altText.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShortMessage(context, messagesWebService.getHeader().getMessage());
                    listData = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    altText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MessagesWebService> call, Throwable t) {
                dismissProgressDialog();
                LogUtil.e(Constant.LOG_TAG, t.toString());
                ToastUtil.showShortMessage(context, Constant.RESPONSE_ON_FAILURE);
            }
        });
    }

    @FromXML
    public void backImage_mystashReviews(View view) {
        onBackPressed();
    }

    private class MessagesAdapter extends BaseAdapter {
        //        private final ViewBinderHelper binderHelper;
        private Context context;
        private LayoutInflater inflater;

        MessagesAdapter(Context context) {
            this.context = context;
//            binderHelper = new ViewBinderHelper();
//            binderHelper.setOpenOnlyOne(true);
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
//            SwipeRevealLayout swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
//            FrameLayout deleteView = (FrameLayout) convertView.findViewById(R.id.delete_layout);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.textView_Review_Title);
            FrameLayout parentContainer = (FrameLayout) convertView.findViewById(R.id.layout_itemMessage);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.textView_Review_Desc);
            TextView tvTime = (TextView) convertView.findViewById(R.id.textView_Review_dateTime);
            if (listPosition.getBusinessName() != null)
                tvTitle.setText(listPosition.getBusinessName());
            if (listPosition.getMessage() != null)
                tvDesc.setText(listPosition.getMessage().trim());
            if (listPosition.getSentTime() != null)
                if (DateUtils.isToday(listPosition.getSentTime()))
                    tvTime.setText("Today");
                else {
                    tvTime.setText(setTime(listPosition.getSentTime()));
                }
            parentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listPosition.getMessage() != null && listPosition.getBusinessName() != null)
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.message)
                                .setMessage(listPosition.getMessage())
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                }
            });

            parentContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteItem(position, listPosition.getMessageid());
                    return false;
                }
            });

            String item = getItem(position).toString();

//            if (item != null) {
//                binderHelper.bind(swipeLayout, item);
//            }
//            deleteView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteItem(position, listPosition.getMessageid());
//                }
//            });
            return convertView;
        }

        private void deleteItem(final int positionId, final String messageid) {
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
                    deleteNotificationService(Constant.ACTION_DELETE_NOTIFICATION, messageid, positionId);
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
            default_.applyPattern("MMM dd yyyy");
            return default_.format(t1);
        }
    }

    private void deleteNotificationService(String action, String messageid, final int positionId) {
        Call<DeleteNotificationWS> call = WebServicesFactory.getInstance().deleteNotification(action, messageid);
        call.enqueue(new Callback<DeleteNotificationWS>() {
            @Override
            public void onResponse(Call<DeleteNotificationWS> call, Response<DeleteNotificationWS> response) {
                if (response.body() == null || response.body().getHeader() == null
                        || response.body().getHeader().getSuccess() == null) {
                    Toast.makeText(Messages.this, "Found null in web response", Toast.LENGTH_SHORT).show();
                } else if (response.body().getHeader().getSuccess().equals("1")) {
                    listData.remove(positionId);
                    adapter.notifyDataSetChanged();
                } else if (response.body().getHeader().getMessage() != null) {
                    Toast.makeText(Messages.this, response.body().getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Messages.this, "Found null in API response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteNotificationWS> call, Throwable t) {
                Log.e(t.toString());
                Toast.makeText(Messages.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
