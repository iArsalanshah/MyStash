package com.example.mystashapp.mystashappproject.home.mystash_box.viewpager_adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.Searchnearby;

public class ViewPagerAdapter extends PagerAdapter {
    Searchnearby snb = new Searchnearby();
    private int[] image_resources = {R.drawable.food_1, R.drawable.food_2,
            R.drawable.food_3
    };
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate
                (R.layout.swipe_layout_mystash_details, container, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.swipe_view_Images);
        imageView.setImageResource(image_resources[position]);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }
}
