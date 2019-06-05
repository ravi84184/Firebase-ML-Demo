package com.nikdemo.firebaseml;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nikdemo.firebaseml.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.img_to_text).setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this,ImageToTextActivity.class));
        });

        findViewById(R.id.text_language_change).setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this,TextTranslateActivity.class));
        });

    }
}
