package com.example.mystashapp.mystashappproject.home.mystash_box.recyclerview_util_mystashlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.mystashapp.mystashappproject.R;


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
