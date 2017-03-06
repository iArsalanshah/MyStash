package com.citemenu.mystash.activity.mystash_box.recyclerview_util_mystashlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.citemenu.mystash.R;
import com.citemenu.mystash.activity.mystash_box.ListDetails_MyStash;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.pojo.get_my_stash_list.Stashlist;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.remove_stash.RemoveStash;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAdapter_MyStashList extends RecyclerView.Adapter<RecyclerAdapter_MyStashList.RecyclerViewHolder_MyStashList> {
    private final ViewBinderHelper binderHelper;
    private final Users userObject;
    Context context;
    private ArrayList<Stashlist> searchNearbyList;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //context.startActivity(new Intent(context, ListDetails_MyStash.class));
            Log.i("RecyclerTEST", " OnClick Working");
            RecyclerViewHolder_MyStashList vholder =
                    (RecyclerViewHolder_MyStashList) v.getTag();
            int position = vholder.getAdapterPosition();
            Stashlist s = searchNearbyList.get(position);
            String jsonlist = (new Gson()).toJson(s);
            Intent intent = new Intent(context, ListDetails_MyStash.class);
            intent.putExtra("id", jsonlist);
            context.startActivity(intent);
        }
    };

    public RecyclerAdapter_MyStashList(Context context, ArrayList<Stashlist> searchnearbies) {
        this.context = context;
        this.searchNearbyList = searchnearbies;
        binderHelper = new ViewBinderHelper();
        binderHelper.setOpenOnlyOne(true);
        userObject = CustomSharedPref.getUserObject(context);
    }

    @Override
    public RecyclerViewHolder_MyStashList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mystash_recyclerview_selected_items, parent, false);
        return new RecyclerViewHolder_MyStashList(v);
    }

    public void onBindViewHolder(final RecyclerViewHolder_MyStashList holder, int position) {
        Stashlist sbNearBy = searchNearbyList.get(position);

        //setting image using picasso library
        if (sbNearBy.getLogourl() != null
                && !sbNearBy.getLogourl().isEmpty())
            Picasso.with(context).load(sbNearBy.getLogourl())
                    .error(R.drawable.placeholder) //optional
                    .placeholder(R.drawable.placeholder) //optional
                    .into(holder.imageViewAvatar);
        if (sbNearBy.getName() != null)
            holder.tvRecyclerTitle.setText(sbNearBy.getName());
        if (sbNearBy.getAddress() != null)
            holder.tvRecyclerDesc.setText(sbNearBy.getAddress());
        holder.layout.setOnClickListener(clickListener);
        holder.layout.setTag(holder);
        binderHelper.bind(holder.swipeLayout, String.valueOf(getItemId(position)));
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDialog(searchNearbyList.get(holder.getAdapterPosition()).getId(), holder.getAdapterPosition());
            }
        });
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

    public class RecyclerViewHolder_MyStashList extends RecyclerView.ViewHolder {

        final FrameLayout deleteView;
        final SwipeRevealLayout swipeLayout;
        TextView tvRecyclerTitle, tvRecyclerDesc;
        ImageView imageViewAvatar;
        RelativeLayout layout;

        public RecyclerViewHolder_MyStashList(View itemView) {
            super(itemView);
            deleteView = (FrameLayout) itemView.findViewById(R.id.delete_layoutStash);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layoutStash);

            tvRecyclerTitle = (TextView) itemView.findViewById(R.id.row_recyclerview_title_mystash);
            tvRecyclerDesc = (TextView) itemView.findViewById(R.id.row_recyclerview_desc_mystash);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.row_recyclerview_avatar_mystash);
            layout = (RelativeLayout) itemView.findViewById(R.id.container_item_mystash_list);
        }
    }
}
