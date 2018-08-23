package com.erobbing.ping;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zhangzhaolei on 2018/8/21.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
