package com.cyrus.lok;

import android.app.Activity;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, android.hardware.Camera.AutoFocusCallback {

    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private android.hardware.Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraView = (SurfaceView) this.findViewById(R.id.CameraView);
        mCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);
    }

    private void autoFocus(){
        camera.autoFocus(this);
    }

    @Override
    public void onAutoFocus(boolean success, android.hardware.Camera camera) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = android.hardware.Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("MYLtagOG","SurfaceView is Change!");
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("tag","SurfaceView is Destroyed!");
        camera.release();
        camera = null;
    }
}
