package com.example.mystashapp.mystashappproject.home.flyers_box;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.mystashapp.mystashappproject.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class Flyers_pdfview extends AppCompatActivity implements OnPageChangeListener, OnDrawListener, OnLoadCompleteListener {

    public static final String SAMPLE_FILE = "company_profile.pdf";
    PDFView pdfView;
    int pgNo = 1;
    private ImageView imgBack;
    private String pdfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyers_pdfview);
        init();
        pdfName = SAMPLE_FILE;
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pdfView.fromAsset(pdfName)
                .defaultPage(pgNo)
                .showMinimap(false)
                .enableSwipe(true)
                .onDraw(this)
                .onLoad(this)
                .onPageChange(this)
                .load();
    }

    private void init() {
        imgBack = (ImageView) findViewById(R.id.imageViewToolbarBack);
        pdfView = (PDFView) findViewById(R.id.pdfView);
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

    }
}
