package com.citemenu.mystash.activity.upload_bill_camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.citemenu.mystash.helper.Log;

/**
 * Created by dev.arsalan on 9/28/2016.
 */

public class BillCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;

    public BillCameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BillCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BillCameraPreview(Context context) {
        super(context);
    }

    public void init(Camera camera) {
        this.camera = camera;
        initSurfaceHolder();
    }

    @SuppressWarnings("deprecation") // needed for < 3.0
    private void initSurfaceHolder() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            //set camera to continually auto-focus
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(params);
            camera.startPreview();
        } catch (Exception e) {
            Log.d("Error setting camera", e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
