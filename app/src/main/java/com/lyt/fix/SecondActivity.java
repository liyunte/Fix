package com.lyt.fix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lyt.fix.library.FixManager;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    public void fix(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FixManager.getInstance().addDex(SecondActivity.this,"classes1.dex");
                FixManager.getInstance().loadFixedDex(SecondActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(SecondActivity.this,"fix成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }
    public void show(View view){
      new Test().div(this);
    }
}
