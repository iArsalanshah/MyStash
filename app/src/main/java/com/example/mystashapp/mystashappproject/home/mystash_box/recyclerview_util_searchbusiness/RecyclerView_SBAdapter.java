package com.example.mystashapp.mystashappproject.home.mystash_box.recyclerview_util_searchbusiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.mystash_box.ListDetails_MyStash;
import com.example.mystashapp.mystashappproject.home.mystash_box.SearchBusiness_MyStash;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Searchnearby;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_SBAdapter extends RecyclerView.Adapter<RecyclerView_SBAdapter.RecyclerView_SBCustomViewHolder> implements Filterable {
    SearchBusiness_MyStash latlong;
    private List<Searchnearby> searchNearbyList;
    private List<Searchnearby> mStringFilterList;
    private Context mContext;
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            context.startActivity(new Intent(context, ListDetails_MyStash.class));
            if (!SearchBusiness_MyStash.IS_CHECK_IN) {
                RecyclerView_SBCustomViewHolder holder =
                        (RecyclerView_SBCustomViewHolder) view.getTag();
                int position = holder.getAdapterPosition();
                Searchnearby s = searchNearbyList.get(position);
                String jsonBusiness = (new Gson()).toJson(s);
                Intent intent = new Intent(mContext, ListDetails_MyStash.class);
                intent.putExtra("id", jsonBusiness);
//                Log.d(Constant_util.LOG_TAG, "" + s.getId());
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Thanks for visiting", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private ValueFilter valueFilter;

    public RecyclerView_SBAdapter(Context context, List<Searchnearby> searchnearbies) {
        searchNearbyList = searchnearbies;
        mStringFilterList = searchnearbies;
        latlong = new SearchBusiness_MyStash();
        this.mContext = context;
    }

    @Override
    public RecyclerView_SBCustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recyclerview_sb, viewGroup, false);
        return new RecyclerView_SBCustomViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView_SBCustomViewHolder customViewHolder, int i) {
        Searchnearby sbNearBy = searchNearbyList.get(i);
        //setting image using picasso library
        Picasso.with(mContext).load(sbNearBy.getLogourl())
                .error(R.drawable.placeholder_shadow) //optional
                .placeholder(R.drawable.placeholder_shadow) //optional
                .into(customViewHolder.thumbnail);

        //Setting text view title,address,rating,distance
        customViewHolder.tvTileAddress.setText(sbNearBy.getName());
        customViewHolder.tvAreaAddress.setText(sbNearBy.getAddress());
        customViewHolder.tvMeterAddress.setText(String.valueOf(sbNearBy.getDistance().intValue()) + "m");
        customViewHolder.rattingBar.setRating(sbNearBy.getRatingvalue());
        customViewHolder.thumbnail.setOnClickListener(clickListener);
        customViewHolder.thumbnail.setTag(customViewHolder);
    }

    @Override
    public int getItemCount() {
        return searchNearbyList.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class RecyclerView_SBCustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView tvTileAddress;
        protected TextView tvAreaAddress;
        protected TextView tvMeterAddress;
        protected RatingBar rattingBar;

        public RecyclerView_SBCustomViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.item_location_sb_thumbnail);
            tvTileAddress = (TextView) view.findViewById(R.id.item_title_sb_location_address);
            tvAreaAddress = (TextView) view.findViewById(R.id.item_tvaddress_sb_location);
            tvAreaAddress.setMovementMethod(new ScrollingMovementMethod());
            tvMeterAddress = (TextView) view.findViewById(R.id.item_sb_location_meter);
            rattingBar = (RatingBar) view.findViewById(R.id.item_ratingbar_sb);
        }
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Searchnearby> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (mStringFilterList.get(i).getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        Searchnearby sb = new Searchnearby(mStringFilterList.get(i).getSelected(), mStringFilterList.get(i).getId(),
                                mStringFilterList.get(i).getName(), mStringFilterList.get(i).getCompanyname(), mStringFilterList.get(i).getAddress(),
                                mStringFilterList.get(i).getContact(), mStringFilterList.get(i).getEmail(), mStringFilterList.get(i).getLogourl(),
                                mStringFilterList.get(i).getUid(), mStringFilterList.get(i).getCity(), mStringFilterList.get(i).getPostalcode(),
                                mStringFilterList.get(i).getProvince(), mStringFilterList.get(i).getCountry(), mStringFilterList.get(i).getContactname(),
                                mStringFilterList.get(i).getLat(), mStringFilterList.get(i).getLongt(), mStringFilterList.get(i).getRatingCount(),
                                mStringFilterList.get(i).getRating(), mStringFilterList.get(i).getIsCitepoint(), mStringFilterList.get(i).getStatus(),
                                mStringFilterList.get(i).getIsStamp(), mStringFilterList.get(i).getImages(), mStringFilterList.get(i).getRatingvalue(),
                                mStringFilterList.get(i).getStashid(), mStringFilterList.get(i).getIsstash(), mStringFilterList.get(i).getDistance(),
                                mStringFilterList.get(i).getReviews());
                        filterList.add(sb);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchNearbyList = (ArrayList<Searchnearby>) results.values;
            notifyDataSetChanged();
        }
    }
}
