package com.citemenu.mystash.activity.mystash_box;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.activity.BaseActivity;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.helper.FromXML;
import com.citemenu.mystash.pojo.get_my_stash_list.GetMyStash;
import com.citemenu.mystash.pojo.get_my_stash_list.Stashlist;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.remove_stash.RemoveStash;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.ImageUtil;
import com.citemenu.mystash.utils.LogUtil;
import com.citemenu.mystash.utils.TextUtil;
import com.citemenu.mystash.utils.ToastUtil;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_MyStash extends BaseActivity {
    RecyclerView mRecyclerView;
    TextView altText;
    private AdapterStashList adapter;
    private List<Stashlist> searchNearbyList;
    private Users userObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__mystash);
        userObject = CustomSharedPref.getUserObject(context);
        altText = (TextView) findViewById(R.id.list_mystash_altText);
        //Progress Dialog
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_MyStashList);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_myStashList);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        adapter = new AdapterStashList(context);
        searchNearbyList = new ArrayList<>();
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyStash();
            }
        });
    }

    @Override
    protected void onResume() {
        getMyStash();
        super.onResume();
    }

    private void getMyStash() {
        showProgressDialog();
        Users cid = CustomSharedPref.getUserObject(List_MyStash.this);
        Call<GetMyStash> call = WebServicesFactory.getInstance().getMyStashList(Constant.ACTION_GET_MYSTASH_LIST, cid.getId());
        call.enqueue(new Callback<GetMyStash>() {
            @Override
            public void onResponse(Call<GetMyStash> call, Response<GetMyStash> response) {
                dismissProgressDialog();
                if (response.body() == null || response.body().getHeader() == null
                        || response.body().getHeader().getSuccess() == null) {
                    ToastUtil.showShortMessage(context, Constant.RESPONSE_NULL);
                } else if (response.body().getHeader().getSuccess().equals("1")) {
                    if (response.body().getBody().getStashlist().size() > 0) {
                        if (altText.getVisibility() == View.VISIBLE)
                            altText.setVisibility(View.GONE);
                        searchNearbyList = response.body().getBody().getStashlist();
                        adapter.notifyDataSetChanged();
                    } else altText.setVisibility(View.VISIBLE);
                } else {
                    searchNearbyList = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    altText.setVisibility(View.VISIBLE);
                    ToastUtil.showShortMessage(context, response.body().getHeader().getMessage());
                }
//                try {
//                    GetMyStash businessResponse = response.body();
//                    switch (businessResponse.getHeader().getSuccess()) {
//                        case "1":
//                            if (businessResponse.getBody().getStashlist().isEmpty() && businessResponse.getBody().getStashlist().size() == 0) {
//                                mRecyclerView.setVisibility(View.GONE);
//                                altText.setVisibility(View.VISIBLE);
//                                Toast.makeText(List_MyStash.this, "" + businessResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
//                            } else {
//                                searchNearbyList = businessResponse.getBody().getStashlist();
//                                AdapterStashList mAdapter = new AdapterStashList(context);
//                                mRecyclerView.setAdapter(mAdapter);
//                            }
//                            break;
//                        case "0":
//                            mRecyclerView.setVisibility(View.GONE);
//                            altText.setVisibility(View.VISIBLE);
//                            break;
//                        default:
//                            Toast.makeText(List_MyStash.this, "" + businessResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
//                            mRecyclerView.setVisibility(View.GONE);
//                            altText.setVisibility(View.VISIBLE);
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<GetMyStash> call, Throwable t) {
                dismissProgressDialog();
                LogUtil.e(Constant.LOG_TAG, t.toString());
                ToastUtil.showShortMessage(context, Constant.RESPONSE_ON_FAILURE);
            }
        });
    }

    @FromXML
    public void backMyStashRecyclerViewImageBtn(View view) {
        finish();
    }

    @FromXML
    public void PlusMyStashRecyclerImageView(View view) {
        SearchBusiness_MyStash.IS_CHECK_IN = false;
        startActivity(new Intent(this, SearchBusiness_MyStash.class).putExtra("plusClicked", true));
    }

    class AdapterStashList extends RecyclerView.Adapter<AdapterStashList.VH> {
        //        private final ViewBinderHelper binderHelper;
        Context context;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, ListDetails_MyStash.class));
                VH vholder = (VH) v.getTag();
                int position = vholder.getAdapterPosition();
                Stashlist s = searchNearbyList.get(position);
                String jsonlist = (new Gson()).toJson(s);
                Intent intent = new Intent(context, ListDetails_MyStash.class);
                intent.putExtra("id", jsonlist);
                context.startActivity(intent);
            }
        };

        AdapterStashList(Context context) {
            this.context = context;
//            binderHelper = new ViewBinderHelper();
//            binderHelper.setOpenOnlyOne(true);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mystash_recyclerview_selected_items, parent, false);
            return new VH(v);
        }

        public void onBindViewHolder(final VH holder, int position) {
            Stashlist sbNearBy = searchNearbyList.get(position);
            //setting image using picasso library
            ImageUtil.setImageWithResource(context, holder.imageView, sbNearBy.getLogourl());

            TextUtil.setText(holder.tvRecyclerTitle, sbNearBy.getName());

            TextUtil.setText(holder.tvRecyclerDesc, sbNearBy.getAddress());

            holder.layout.setOnClickListener(clickListener);
            holder.layout.setTag(holder);
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    removeDialog(searchNearbyList.get(holder.getAdapterPosition()).getId(), holder.getAdapterPosition());
                    return false;
                }
            });
//            binderHelper.bind(holder.swipeLayout, String.valueOf(getItemId(position)));
//            holder.deleteView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    removeDialog(searchNearbyList.get(holder.getAdapterPosition()).getId(), holder.getAdapterPosition());
//                }
//            });
        }

        private void removeDialog(final String id, final int adapterPosition) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Are you sure you want to remove this business " +
                    "from your stash, doing so will prevent  you from  receiving " +
                    "specials and VIP offers from this merchant.");
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onRemoveStashItem(id, adapterPosition);
                }
            });
            dialog.show();
        }

        private void onRemoveStashItem(String id, final int adapterPosition) {
            // webservice should be called here.
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting...");
            dialog.show();
            Call<RemoveStash> call = WebServicesFactory.getInstance()
                    .getRemoveStash(Constant.ACTION_REMOVE_STASH, id
                            , userObject.getId());
            call.enqueue(new Callback<RemoveStash>() {
                @Override
                public void onResponse(Call<RemoveStash> call, Response<RemoveStash> response) {
                    dialog.dismiss();
                    RemoveStash removeStash = response.body();
                    if (removeStash == null) {

                    } else if (removeStash.getHeader().getSuccess().equals("1")) {
                        Toast.makeText(context, "" + removeStash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                        searchNearbyList.remove(adapterPosition);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "" + removeStash.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RemoveStash> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public int getItemCount() {
            return searchNearbyList.size();
        }

        class VH extends RecyclerView.ViewHolder {
            //            final FrameLayout deleteView;
//            final SwipeRevealLayout swipeLayout;
            TextView tvRecyclerTitle, tvRecyclerDesc;
            ImageView imageView;
            RelativeLayout layout;

            VH(View itemView) {
                super(itemView);
//                deleteView = (FrameLayout) itemView.findViewById(R.id.delete_layoutStash);
//                swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layoutStash);

                tvRecyclerTitle = (TextView) itemView.findViewById(R.id.row_recyclerview_title_mystash);
                tvRecyclerDesc = (TextView) itemView.findViewById(R.id.row_recyclerview_desc_mystash);
                imageView = (ImageView) itemView.findViewById(R.id.row_recyclerview_avatar_mystash);
                layout = (RelativeLayout) itemView.findViewById(R.id.container_item_mystash_list);
            }
        }
    }
}
