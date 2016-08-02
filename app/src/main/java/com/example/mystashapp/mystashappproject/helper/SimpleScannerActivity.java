package com.example.mystashapp.mystashappproject.helper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by tariq on 22/04/2016.
 */
public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    public static final BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
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
        Log.d(Constant_util.LOG_TAG, "" + result.getText() + "" + result.getBarcodeFormat());
        Log.d(Constant_util.LOG_TAG, "" + result.getResultMetadata());
        if (result.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)) {
            mScannerView.resumeCameraPreview(this);
            Toast.makeText(SimpleScannerActivity.this, "QR code does not support", Toast.LENGTH_SHORT).show();
        } else {
            mScannerView.stopCamera();
            finish();
        }
        barcodeText = result.getText();
//        barcodeFormat = result.getBarcodeFormat();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
