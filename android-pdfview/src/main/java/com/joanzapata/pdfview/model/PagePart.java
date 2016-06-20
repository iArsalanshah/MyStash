/**
 * Copyright 2014 Joan Zapata
 * <p>
 * This file is part of Android-pdfview.
 * <p>
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.joanzapata.pdfview.model;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class PagePart {

    private int userPage;

    private int page;

    private Bitmap renderedBitmap;

    private float width, height;

    private RectF pageRelativeBounds;

    private boolean thumbnail;

    private int cacheOrder;

    public PagePart(int userPage, int page, Bitmap renderedBitmap, float width, float height, RectF pageRelativeBounds, boolean thumbnail, int cacheOrder) {
        super();
        this.userPage = userPage;
        this.page = page;
        this.renderedBitmap = renderedBitmap;
        this.pageRelativeBounds = pageRelativeBounds;
        this.thumbnail = thumbnail;
        this.cacheOrder = cacheOrder;
    }

    public int getCacheOrder() {
        return cacheOrder;
    }

    public void setCacheOrder(int cacheOrder) {
        this.cacheOrder = cacheOrder;
    }

    public int getPage() {
        return page;
    }

    public int getUserPage() {
        return userPage;
    }

    public Bitmap getRenderedBitmap() {
        return renderedBitmap;
    }

    public RectF getPageRelativeBounds() {
        return pageRelativeBounds;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PagePart)) {
            return false;
        }

        PagePart part = (PagePart) obj;
        return part.getPage() == page
                && part.getUserPage() == userPage
                && part.getWidth() == width
                && part.getHeight() == height
                && part.getPageRelativeBounds().left == pageRelativeBounds.left
                && part.getPageRelativeBounds().right == pageRelativeBounds.right
                && part.getPageRelativeBounds().top == pageRelativeBounds.top
                && part.getPageRelativeBounds().bottom == pageRelativeBounds.bottom;
    }

}
