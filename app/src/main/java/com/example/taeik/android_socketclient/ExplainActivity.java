package com.example.taeik.android_socketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ExplainActivity extends Activity implements View.OnTouchListener {
    float pressedX;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_explain);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float distance = 0;
        Log.d("@","???");
        switch(event.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                pressedX = event.getX();
                Log.d("하하","X받음");
                break;
            case MotionEvent.ACTION_UP:
                distance = pressedX - event.getX();
                Log.d("하하","X2받음");
                break;

        }

// 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
        if (Math.abs(distance) < 10) {
            Log.d("어머","안녕false");
            return false;
        }

        if (distance > 0) {
// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
            Log.d("어머","안녕true");
            Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_slide_in,R.anim.activity_slide_out);
        } else {
// 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
            // Intent intent = new Intent(mContext, LeftActivity.class);
            //startActivity(intent);
        }

        finish(); // finish 해주지 않으면 activity가 계속 쌓인다.

        return true;
    }
}
