package com.xxx.xxx;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cpa.com.knac.Libs.RequestIO;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, android.hardware.Camera.AutoFocusCallback {

    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private android.hardware.Camera camera;
    private FloatingActionButton btn_takephoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // Camera surface view init
        mCameraView = (SurfaceView) this.findViewById(R.id.CameraView);
        mCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);

        // Take photo button init
        btn_takephoto = (FloatingActionButton) findViewById(R.id.btn_takephoto);
        btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new android.hardware.Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                        //Toast.makeText(getApplicationContext(), "Take Photo !!", Toast.LENGTH_LONG).show();

                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length); // Byte array to bitmap
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Create compress use byte array output stream
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* Ignored for PNGs */, byteArrayOutputStream); // Compress bit map as jpg
                        byte[] resultImageData = byteArrayOutputStream.toByteArray(); // JPG byte array
                        String encodeBase64 = Base64.encodeToString(resultImageData, Base64.DEFAULT); // Encode as base64
                        Toast.makeText(getApplicationContext(), encodeBase64, Toast.LENGTH_LONG).show();

                        try {
                            camera.setPreviewDisplay(mCameraHolder);
                            camera.startPreview();
                        } catch (IOException e) { finish(); }
                    }
                });
            }
        });
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
