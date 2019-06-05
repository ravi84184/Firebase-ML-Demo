package com.nikdemo.firebaseml;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateModelManager;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Set;

public class TextTranslateActivity extends AppCompatActivity {
    private static final String TAG = "TextTranslateActivity";
    EditText edt_text;
    TextView txt_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translate);

        edt_text = findViewById(R.id.edt_text);
        txt_result = findViewById(R.id.txt_result);

        // Create an English-German translator:
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build();
        final FirebaseTranslator englishGermanTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseTranslateModelManager modelManager = FirebaseTranslateModelManager.getInstance();

// Get translation models stored on the device.
        modelManager.getAvailableModels(FirebaseApp.getInstance())
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> models) {
                        Log.e(TAG, "onSuccess:1 " );
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                        Log.e(TAG, "onFailure:1 ",e );
                    }
                });

// Delete the German model if it's on the device.
/*        FirebaseTranslateRemoteModel deModel =
                new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.HI).build();
        modelManager.deleteDownloadedModel(deModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Log.e(TAG, "onSuccess:2 " );
                        // Model deleted.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:2 ",e );
                        // Error.
                    }
                });*/

// Download the French model.
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseTranslateRemoteModel frModel =
                new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.HI)
                        .setDownloadConditions(conditions)
                        .build();
        modelManager.downloadRemoteModelIfNeeded(frModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Log.e(TAG, "onSuccess:3 " );
                        // Model downloaded.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:3 ",e );
                        // Error.
                    }
                });


        findViewById(R.id.btn_translate).setOnClickListener(v->{
            englishGermanTranslator.translate(edt_text.getText().toString())
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@NonNull String translatedText) {
                                    Log.e(TAG, "onSuccess: "+translatedText );
                                    // Translation successful.
                                    txt_result.setText(translatedText);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ",e );
                                    // Error.
                                    // ...
                                }
                            });

        });

    }
}
