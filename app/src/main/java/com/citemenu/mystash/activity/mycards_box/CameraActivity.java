package com.citemenu.mystash.activity.mycards_box;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.citemenu.mystash.helper.FromXML;

import java.io.File;

import static com.citemenu.mystash.helper.CameraHelper.cameraAvailable;
import static com.citemenu.mystash.helper.CameraHelper.getCameraInstance;
import static com.citemenu.mystash.helper.MediaHelper.getOutputMediaFile;
import static com.citemenu.mystash.helper.MediaHelper.saveToFile;

/**
 * Created by dev.arsalan on 8/10/2016.
 */
public class CameraActivity extends Activity implements Camera.PictureCallback {

    ProgressDialog dialog;
    private Camera camera;
    private CameraPreview cameraPreview;

//    private static Bitmap RotateBitmap(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
//    }

    private static String savePictureToFileSystem(Bitmap data) {
        File file = getOutputMediaFile();
        saveToFile(data, file);
        return file.getAbsolutePath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_rectangle_camera);
        setResult(RESULT_CANCELED);
        // Camera may be in use by another activity or the system or not available at all
        camera = getCameraInstance();
        if (cameraAvailable(camera)) {
            initCameraPreview();
        } else {
            finish();
        }
    }

    // Show the camera view on the activity
    private void initCameraPreview() {
        cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        cameraPreview.init(camera);
    }

    @FromXML
    public void onCaptureClick(View button) {
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
        try {
            camera.takePicture(null, null, this);
        } catch (Exception ex) {
            Log.d("OnPictureTaken: ", ex.toString());
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("TAG", "Picture taken");
        if (data != null) {
            dialog.show();
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
            // Notice that width and height are reversed
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
            int w = scaled.getWidth();
            int h = scaled.getHeight();
            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
            String path = savePictureToFileSystem(bm);
            dialog.dismiss();
            setResult(path);
            finish();
        }
//        if (data != null) {
////            int screenWidth = getResources().getDisplayMetrics().widthPixels;
////            int screenHeight = getResources().getDisplayMetrics().heightPixels;
////            BitmapFactory.Options options = new BitmapFactory.Options();
////            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
////            bm = RotateBitmap(bm, 90);
////            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // Notice that width and height are reversed
////                Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
////                int w = scaled.getWidth();
////                int h = scaled.getHeight();
//            // Setting post rotate to 90
////            Matrix mtx = new Matrix();
////            mtx.postRotate(90);
////            // Rotating Bitmap
////            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
////            }
////            else {// LANDSCAPE MODE
////                //No need to reverse width and height
////                Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
////                bm = scaled;
////            }
////            ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////            byte[] byteArray = stream.toByteArray();
////            String path = savePictureToFileSystem(data);
////            setResult(path);
//        }
    }

    private void setResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
