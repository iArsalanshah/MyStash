package com.citemenu.mystash.home.upload_bill_camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.FromXML;
import com.citemenu.mystash.helper.Log;

import java.util.ArrayList;

import static com.citemenu.mystash.helper.CameraHelper.cameraAvailable;
import static com.citemenu.mystash.helper.CameraHelper.getCameraInstance;

public class AddBillsCamera extends Activity implements Camera.PictureCallback {
    //    private static Bitmap bmRestrntName, bmAmount;
    public static ArrayList<Bitmap> bmItemList;
    private Camera camera;
    private ProgressDialog dialog;
    private BillCameraPreview cameraPreview;
    private TextView tvSuggestionTop, tvDone, tvCancel;
    private String storeName = "Scan restaurant name here";
    private String scanItemList = "Scan items list";
    private String longReceiptText = "long Receipt? Add the rest on the NEXT Screen";
    private String scanBillAmount = "Scan the total bill amount";
    private String billType;
    private boolean isCaptureAPic = false;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_add_bills);
        billType = getIntent().getStringExtra("billType");
        setResult(RESULT_CANCELED);
        // Camera may be in use by another activity or the system or not available at all
        camera = getCameraInstance();
        if (cameraAvailable(camera)) {
            initCameraPreview();
        } else {
            finish();
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        tvSuggestionTop = (TextView) findViewById(R.id.tv_addBillsCamera_top);
        tvDone = (TextView) findViewById(R.id.tv_addBillsCamera_done);
        tvCancel = (TextView) findViewById(R.id.tv_addBillsCamera_cancel);

        if (billType.equals("top")) {
            tvSuggestionTop.setText(storeName);
        } else if (billType.equals("middle")) {
            tvSuggestionTop.setText(scanItemList);
        } else if (billType.equals("bottom")) {
            tvSuggestionTop.setText(scanBillAmount);
        } else {
            Toast.makeText(this, "Error catching click", Toast.LENGTH_SHORT).show();
        }

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billType.equals("middle") && isCaptureAPic) {
                    setResultStatus(RESULT_OK, null);
                    finish();
                } else {
                    Toast.makeText(AddBillsCamera.this, "Take Picture", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultStatus(RESULT_CANCELED, null);
                finish();
            }
        });
//        bmItemList = new Bitmap[100];
    }

    // Show the camera view on the activity
    private void initCameraPreview() {
        cameraPreview = (BillCameraPreview) findViewById(R.id.surfaceView_addBills);
        cameraPreview.init(camera);
        bmItemList = new ArrayList<>();
    }

    @FromXML
    public void onCaptureBill(View view) {
        try {
            camera.takePicture(null, null, this);
        } catch (Exception ex) {
            Log.d(ex.toString());
        }
    }

    private void setResultStatus(int status, Bitmap bm) {
        Intent intent = new Intent();
//        if (status == RESULT_OK && bm == null)
//            intent.putExtra("bmCapture", bmItemList);
//        else if (status == RESULT_OK)
//            intent.putExtra("bmCapture", bm);
        setResult(status, intent);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        counter = 0;
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        android.util.Log.d("TAG", "Picture taken");
        if (data != null) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
            //Notice that width and height are reversed
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
            int w = scaled.getWidth();
            int h = scaled.getHeight();
            //Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            //Rotating Bitmap
            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
            checkBillType(bm, billType);
        }
    }

    private void checkBillType(Bitmap bm, String billType) {
//        if (billType.equals("top")) {
//            bmRestrntName = bm;
//            setResultStatus(RESULT_OK, bmRestrntName);
//            finish();
//        } else
//        if (billType.equals("middle")) {
        if (!isCaptureAPic) {
            isCaptureAPic = true;
            tvSuggestionTop.setText(longReceiptText);
        }
        bmItemList.add(counter, bm);
        counter++;
        camera.startPreview();
//        }
//        else if (billType.equals("bottom")) {
//            bmAmount = bm;
//            setResultStatus(RESULT_OK, bmAmount);
//            finish();
//        }
    }
}
