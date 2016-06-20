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
package com.joanzapata.pdfview;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;

class DecodingAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "DecodingAsyncTask";
    /**
     * The decode service used for decoding the PDF
     */
    private DecodeService decodeService;

    private boolean cancelled;

    private Uri uri;

    private PDFView pdfView;
    private Context context;

    public DecodingAsyncTask(Uri uri, PDFView pdfView) {
        this.cancelled = false;
        this.pdfView = pdfView;
        this.uri = uri;
        context = pdfView.getContext();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // HACK: 21/04/16 Add try catch in order to prevent PDF corrupted file
        try {
            decodeService = new DecodeServiceBase(new PdfContext());
            decodeService.setContentResolver(context.getContentResolver());
            decodeService.open(uri);
        } catch (RuntimeException rte) {
            decodeService = null;
            Log.e(TAG, "doInBackground: ", rte);
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        if (!cancelled) {
            pdfView.loadComplete(decodeService);
        }
    }

    protected void onCancelled() {
        cancelled = true;
    }
}
