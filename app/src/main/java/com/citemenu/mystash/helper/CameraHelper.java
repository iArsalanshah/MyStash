package com.citemenu.mystash.helper;

import android.hardware.Camera;

/**
 * Created by dev.arsalan on 8/10/2016.
 */
public class CameraHelper {
    public static boolean cameraAvailable(Camera camera) {
        return camera != null;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available or doesn't exist
            Log.d("getCamera failed", e);
        }
        return c;
    }
}
