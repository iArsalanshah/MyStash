package com.example.mystashapp.mystashappproject.home.flyers_box;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.helper.InternetConnectionHelper;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Flyers_pdfview extends AppCompatActivity implements OnPageChangeListener, OnDrawListener, OnLoadCompleteListener {

    public static final String SAMPLE_FILE = "company_profile.pdf";
    PDFView pdfView;
    int pgNo = 1;
    private String pdfUrl;
    private String TAG = "Flyers_PDF_ACTIVITY";
    private Boolean writtenToDisk;
    private File pdfDownloadedFile;
    private ProgressDialog pDialog;
    private ResponseBody respBody;
    private String completePdfPath;
    private String fileExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyers_pdfview);
        init();
        loadPDF();
    }

    private void loadPDF() {
        //first check is it a pdf file to downlaod
        if (fileExtension.contains(".pdf"))
            if (!fileExistance(completePdfPath)) { //checking if file exist or not
                if (InternetConnectionHelper.isNetworkAvailable(this)) { //checking is network available
                    getPdfFile();
                } else {
                    Toast toast = Toast.makeText(Flyers_pdfview.this, "Internet Connection required!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else { // file exist in this path then open file
                pdfView.fromFile(new File(completePdfPath))
                        .defaultPage(pgNo)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .onDraw(Flyers_pdfview.this)
                        .onLoad(Flyers_pdfview.this)
                        .onPageChange(Flyers_pdfview.this)
                        .load();
            }
        else {
            Toast toast = Toast.makeText(Flyers_pdfview.this, "No PDF file found", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        pdfUrl = getIntent().getStringExtra("pdfFile");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        TextView textDone = (TextView) findViewById(R.id.textviewToolbarDone);
        textDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String file = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1, pdfUrl.length());
        fileExtension = pdfUrl.substring(pdfUrl.lastIndexOf("."));
        completePdfPath = this.getFilesDir() + File.separator + file;
    }

    public boolean fileExistance(String fname) {
//        File file = getBaseContext().getFileStreamPath(fname);
        File file = new File(fname);
        return file.exists();
    }

    private void getPdfFile() {
        Call<ResponseBody> call = WebServicesFactory.getInstance().downloadFileWithDynamicUrlAsync(pdfUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    respBody = response.body();
                    Log.d(TAG, "server contacted and has file");
                    new DownloadFilesTask().execute();
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast toast = Toast.makeText(Flyers_pdfview.this, "Something went wrong please try again later", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // file location for each
            pdfDownloadedFile = new File(completePdfPath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(pdfDownloadedFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pgNo = page;
        //setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void loadComplete(int nbPages) {
        if (PDFView.curruptFile) {
            new AlertDialog.Builder(this).setMessage("PDF document is damaged or corrupt")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            writtenToDisk = writeResponseBodyToDisk(respBody);
//                curruptFile = true;
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "onPostExecute: ");
            pDialog.dismiss();
            if (writtenToDisk) {
                pdfView.fromFile(pdfDownloadedFile)
                        .defaultPage(pgNo)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .onDraw(Flyers_pdfview.this)
                        .onLoad(Flyers_pdfview.this)
                        .onPageChange(Flyers_pdfview.this)
                        .load();
            } else {
                Log.d(TAG, "onPostExecute: write to disk error");
                Toast toast = Toast.makeText(Flyers_pdfview.this, "Something went wrong please try again later", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
