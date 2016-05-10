package com.example.mystashapp.mystashappproject.home.mycards_box;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by tariq on 22/04/2016.
 */
public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    public static String barcodeText = "";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view
        barcodeText = "";
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        // do something with result here
        Log.d(Constant_util.LOG_TAG, "" + result.getBarcodeFormat().toString());
        Log.d(Constant_util.LOG_TAG, "" + result.getText());
        Log.d(Constant_util.LOG_TAG, "" + result.getResultMetadata());
        barcodeText = result.getText();
        mScannerView.stopCamera();
        finish();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
