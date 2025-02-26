package com.nicoft.bewise;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class AugmentedActivity extends Activity {

    private static final String TAG = "CameraDemo";
    Camera camera;
    Preview preview;
    Button buttonClick;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented);

        preview = new Preview(this);
        ((FrameLayout) findViewById(R.id.preview)).addView(preview);

                Log.d(TAG, "onCreate'd");
    }


    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };

    /** Handles data for raw picture */
    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    /** Handles data for jpeg picture */
    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            long time = 0;
            try {
                // write to local sandbox file system
//                outStream = CameraDemo.this.openFileOutput(String.format("%d.jpg", System.currentTimeMillis()), 0);
                // Or write to sdcard
                time =  System.currentTimeMillis();
                outStream = new FileOutputStream(String.format("/sdcard/%d.jpg",time));
                outStream.write(data);
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {



            }
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };




    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = "Preview";

        SurfaceHolder mHolder;
        public Camera camera;

        Preview(Context context) {
            super(context);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where
            // to draw.
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(holder);


                camera.setPreviewCallback(new PreviewCallback() {

                    public void onPreviewFrame(byte[] data, Camera arg1) {
                        FileOutputStream outStream = null;
                        try {
                            outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                            outStream.write(data);
                            outStream.close();
                            Log.d(TAG, "onPreviewFrame - wrote bytes: " + data.length);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                        }
                        Preview.this.invalidate();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            // Because the CameraDevice object is not a shared resource, it's very
            // important to release it when the activity is paused.
            camera.stopPreview();
            camera = null;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = camera.getParameters();
//        parameters.setPreviewSize(w, h);
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            Paint p= new Paint(Color.RED);
            Log.d(TAG,"draw");
            canvas.drawText("PREVIEW", canvas.getWidth()/2, canvas.getHeight()/2, p );
        }
    }


}

