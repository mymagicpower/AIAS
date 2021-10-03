/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package ai.djl.examples.detection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ai.djl.ModelException;
import ai.djl.android.core.BitmapImageFactory;
import ai.djl.examples.detection.domain.FaceDetectedObjects;
import ai.djl.examples.detection.utils.ImageUtil;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;

public class FaceDetectionActivity extends AppCompatActivity {
    View progressBar;
    View containerView;
    private static final int SELECT_IMAGE = 1;
    private Bitmap selectedImage = null;
    private Uri selectedImageUri = null;
    private ImageView iv;
    private Button btnSelect, button;
    private TextView tv;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ZooModel<Image, FaceDetectedObjects> model;
    Predictor<Image, FaceDetectedObjects> predictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title);
        }

        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.loading_progress);
        containerView = findViewById(R.id.container);

        if (hasPermission()) {
            activate();
        } else {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        new UnpackTask().execute();
    }

    public void activate() {
        btnSelect = findViewById(R.id.btn_select);
        iv = findViewById(R.id.iv);
        button = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_IMAGE);
            }
        });

        iv.post(() -> iv.setImageBitmap(ImageUtil.getBitmap(iv.getContext(), "img/selfie.jpg")));
        //Face detection
        button.setOnClickListener(v -> {
            String msg = "";
            if (selectedImageUri != null) {
                try {
                    msg = detectSelectedImage(v.getContext(), iv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    msg = detect(v.getContext(), "img/selfie.jpg", iv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv.setText(msg);
        });
    }

    private String detect(Context mContext, String assetPath, ImageView imageView) throws IOException, TranslateException {
        String msg = "";
        Bitmap bitmap = ImageUtil.getBitmap(mContext, assetPath);
        msg = msg + "Image size = " + bitmap.getWidth() + "x" + bitmap.getHeight() + "\n";
        long startTime = System.currentTimeMillis();

        Image img = ImageUtil.getImage(mContext, assetPath);
        DetectedObjects detection = predictor.predict(img);

        msg = msg + "Face detected = " + detection.getNumberOfObjects() + "\n";
        msg = msg + "Time: " + (System.currentTimeMillis() - startTime) + " ms";

        Bitmap drawBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        List<DetectedObjects.DetectedObject> list = detection.items();

        for (DetectedObjects.DetectedObject face : list) {
            BoundingBox box = face.getBoundingBox();
            Rectangle rectangle = box.getBounds();
            int left = (int) (rectangle.getX() * (double) img.getWidth());
            int top = (int) (rectangle.getY() * (double) img.getHeight());
            int right = left + (int) (rectangle.getWidth() * (double) img.getWidth());
            int bottom = top + (int) (rectangle.getHeight() * (double) img.getHeight());
            canvas.drawRect(left, top, right, bottom, paint);
        }
        imageView.post(() -> imageView.setImageBitmap(drawBitmap));
        return msg;
    }

    private String detectSelectedImage(Context mContext, ImageView imageView) throws IOException, TranslateException {
        String msg = "";
        msg = msg + "Image size = " + selectedImage.getWidth() + "x" + selectedImage.getHeight() + "\n";
        long startTime = System.currentTimeMillis();
        String imgPath = ImageUtil.getRealPathFromUri(mContext, selectedImageUri);

        Path facePath = Paths.get(imgPath);
        Image img = BitmapImageFactory.getInstance().fromFile(facePath);
        DetectedObjects detection = predictor.predict(img);

        msg = msg + "Face detected = " + detection.getNumberOfObjects() + "\n";
        msg = msg + "Time: " + (System.currentTimeMillis() - startTime) + " ms";

        Canvas canvas = new Canvas(selectedImage);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        List<DetectedObjects.DetectedObject> list = detection.items();

        for (DetectedObjects.DetectedObject face : list) {
            BoundingBox box = face.getBoundingBox();
            Rectangle rectangle = box.getBounds();
            int left = (int) (rectangle.getX() * (double) img.getWidth());
            int top = (int) (rectangle.getY() * (double) img.getHeight());
            int right = left + (int) (rectangle.getWidth() * (double) img.getWidth());
            int bottom = top + (int) (rectangle.getHeight() * (double) img.getHeight());
            canvas.drawRect(left, top, right, bottom, paint);
        }

        imageView.post(() -> imageView.setImageBitmap(selectedImage));
        return msg;
    }

    @Override
    protected void onDestroy() {
        if (predictor != null) {
            predictor.close();
        }
        if (model != null) {
            model.close();
        }
        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    private class UnpackTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                model = FaceModel.loadModel();
                predictor = model.newPredictor();
                return true;
            } catch (IOException | ModelException e) {
                Log.e("FaceDetectionActivity", null, e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                progressBar.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(FaceDetectionActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Failed to load model");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> finish());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                activate();
            } else {
                requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    private boolean hasPermission() {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Display the selected image
        if (resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            try {
                if (requestCode == SELECT_IMAGE) {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, o);
                    Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    selectedImage = rgba;
                    iv.setImageBitmap(selectedImage);
                }
            } catch (FileNotFoundException e) {
                Log.e("FaceDetectionActivity", e.getMessage());
                return;
            }
        }
    }
}
