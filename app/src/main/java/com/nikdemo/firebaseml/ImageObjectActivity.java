package com.nikdemo.firebaseml;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class ImageObjectActivity extends AppCompatActivity {
    private static final String TAG = "ImageObjectActivity";

    private static final int REQUEST_IMAGE = 1;
    ImageView image_view;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_text);


        image_view = findViewById(R.id.image_view);


        findViewById(R.id.btn_image).setOnClickListener(v->{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,REQUEST_IMAGE);
        });



        findViewById(R.id.btn_getText).setOnClickListener(v->{
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionObjectDetectorOptions options =
                    new FirebaseVisionObjectDetectorOptions.Builder()
                            .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                            .enableMultipleObjects()
                            .enableClassification()  // Optional
                            .build();
// Or, to change the default settings:
            FirebaseVisionObjectDetector objectDetector =
                    FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
            objectDetector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionObject>>() {
                @Override
                public void onSuccess(List<FirebaseVisionObject> firebaseVisionObjects) {
                    for (FirebaseVisionObject obj : firebaseVisionObjects) {
                        Integer id = obj.getTrackingId();
                        Rect bounds = obj.getBoundingBox();

                        // If classification was enabled:
                        int category = obj.getClassificationCategory();
                        Float confidence = obj.getClassificationConfidence();
                        Log.e(TAG, "onSuccess: "+category+" -- "+confidence );
                    }

                }
            });
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            image_view.setImageBitmap(bitmap);
        }
    }
}
