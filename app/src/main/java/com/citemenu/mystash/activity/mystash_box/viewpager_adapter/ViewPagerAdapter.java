package com.citemenu.mystash.activity.mystash_box.viewpager_adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.citemenu.mystash.R;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby snb;
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context, com.citemenu.mystash.pojo.pojo_searchbusiness.Searchnearby gsonBusiness) {
        this.context = context;
        snb = gsonBusiness;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate
                (R.layout.swipe_layout_mystash_details, container, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.swipe_view_Images);
//        imageView.setImageResource(image_resources[position]);
        if (snb.getImages().get(position).toString() != null
                && !snb.getImages().get(position).toString().isEmpty()) {
            Picasso.with(context)
                    .load(snb.getImages().get(position).toString())
                    .placeholder(R.drawable.placeholder_shadow)
                    .error(R.drawable.placeholder_shadow)
                    .into(imageView);
        }

        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return snb.getImages().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
