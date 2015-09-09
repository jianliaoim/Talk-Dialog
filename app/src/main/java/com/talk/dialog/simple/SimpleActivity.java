package com.talk.dialog.simple;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.talk.dialog.TalkDialog;

/**
 * Created by wlanjie on 15/9/9.
 */
public class SimpleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new TalkDialog.Builder()
            }
        });
    }
}
