package com.citemenu.mystash.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.citemenu.mystash.R;
import com.squareup.picasso.Picasso;

import static com.citemenu.mystash.utils.Utils.isNotNullEmpty;


public class ImageUtil {
    /**
     * @param context   required context to set
     * @param imageView widget in which image would be set
     * @param rsc       image Resource to be set in imageView
     */
    public static void setImageWithResource(@NonNull Context context, @NonNull ImageView imageView, @Nullable String rsc) {
        if (context == null || imageView == null) {
            return;
        }
        if (isNotNullEmpty(rsc))
            Picasso.with(context)
                    .load(rsc)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_img_not_found)
                    .into(imageView);
        else imageView.setImageResource(R.drawable.placeholder_img_not_found);
    }
}
