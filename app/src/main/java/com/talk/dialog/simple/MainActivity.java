package com.talk.dialog.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.talk.dialog.TalkDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TalkDialog.Builder(MainActivity.this)
                        .title("哈哈")
                        .titleColorRes(android.R.color.black)
                        .content("这是一个dialog")
                        .contentColorRes(android.R.color.holo_red_dark)
                        .titleBackgroundColorRes(android.R.color.holo_green_dark)
                        .positiveText("确定")
                        .positiveColorRes(android.R.color.black)
                        .negativeText("取消")
                        .neutralColorRes(android.R.color.holo_red_dark)
                        .backgroundColorRes(android.R.color.holo_blue_bright)
                        .radiusRes(R.dimen.radius)
                        .customView(R.layout.custom, false)
                        .show();
            }
        });

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TalkDialog.Builder(MainActivity.this)
                        .title("哈哈")
                        .titleColorRes(android.R.color.black)
                        .content("这是一个dialog")
                        .contentColorRes(android.R.color.holo_red_dark)
                        .titleBackgroundColorRes(android.R.color.holo_green_dark)
                        .positiveText("确定")
                        .positiveColorRes(android.R.color.black)
                        .negativeText("取消")
                        .negativeColorRes(android.R.color.holo_blue_bright)
                        .neutralText("哈哈")
                        .neutralColorRes(android.R.color.holo_red_dark)
                        .backgroundColorRes(android.R.color.holo_blue_bright)
                        .radiusRes(R.dimen.radius)
                        .items(new CharSequence[]{"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9"})
                        .show();
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TalkDialog.Builder(MainActivity.this)
                        .title("哈哈")
                        .titleColorRes(android.R.color.black)
                        .content("这是一个dialog")
                        .contentColorRes(android.R.color.holo_red_dark)
                        .titleBackgroundColorRes(android.R.color.holo_green_dark)
                        .positiveText("确定")
                        .positiveColorRes(android.R.color.black)
                        .negativeText("取消")
                        .neutralColorRes(android.R.color.holo_red_dark)
                        .backgroundColorRes(android.R.color.holo_blue_bright)
                        .radiusRes(R.dimen.radius)
                        .items(new CharSequence[]{"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9"})
                        .itemsCallbackSingleChoice(2, new TalkDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(TalkDialog dialog, View itemView, int which, CharSequence text) {
                                return false;
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
