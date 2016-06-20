package com.example.mystashapp.mystashappproject.home.mystash_box.recyclerview_util_mystashlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.mystash_box.ListDetails_MyStash;
import com.example.mystashapp.mystashappproject.pojo.get_my_stash_list.Stashlist;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter_MyStashList extends RecyclerView.Adapter<RecyclerViewHolder_MyStashList> {
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
            Log.d(Constant_util.LOG_TAG, " " + s.getId());
            //   ListDetails_MyStash.isDirect = true;
            context.startActivity(intent);
        }
    };

    public RecyclerAdapter_MyStashList(Context context, ArrayList<Stashlist> searchnearbies) {
        this.context = context;
        this.searchNearbyList = searchnearbies;
    }

    @Override
    public RecyclerViewHolder_MyStashList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mystash_recyclerview_selected_items, parent, false);
        return new RecyclerViewHolder_MyStashList(v);
    }

    public void onBindViewHolder(RecyclerViewHolder_MyStashList holder, int position) {
        Stashlist sbNearBy = searchNearbyList.get(position);

        //setting image using picasso library
        Picasso.with(context).load(sbNearBy.getLogourl())
                .error(R.drawable.placeholder) //optional
                .placeholder(R.drawable.placeholder) //optional
                .into(holder.imageViewAvatar);

        holder.tvRecyclerTitle.setText(sbNearBy.getName());
        holder.tvRecyclerDesc.setText(sbNearBy.getAddress());
        holder.layout.setOnClickListener(clickListener);
        holder.layout.setTag(holder);
    }

    public int getItemCount() {
        return searchNearbyList.size();
    }
}
