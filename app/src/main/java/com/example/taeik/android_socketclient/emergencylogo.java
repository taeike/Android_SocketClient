package com.example.taeik.android_socketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.felipecsl.gifimageview.library.GifImageView;


public class emergencylogo extends Activity implements View.OnTouchListener{
    public static int activityCase = 0;
    Handler h;
    float pressedX=0;
    ImageView imageView;
    GifImageView gifView;
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_emergencylogo);

    }
    @Override
    protected void onStart() {
        super.onStart();
        layout = (RelativeLayout) findViewById(R.id.layout1);

        int delay= 2000;
        if( activityCase == 1){
            layout.setBackgroundResource(R.mipmap.firstback);
            delay = 2000;
            h = new Handler();
            h.postDelayed(logo,delay);
        }
        else{
            Log.d("@","fcm으로 들어왔땀");
            imageView = (ImageView) findViewById(R.id.imageView);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
            Glide.with(this).load(R.mipmap.zz).into(imageViewTarget);
            imageView.setOnTouchListener(this);
        }

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

            Intent intent = new Intent(emergencylogo.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_slide_in,R.anim.activity_slide_out);
            return false;
        }

        if (distance > 0) {
// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
            Log.d("어머","안녕true");

        } else {
// 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
            // Intent intent = new Intent(mContext, LeftActivity.class);
            //startActivity(intent);
        }

        finish(); // finish 해주지 않으면 activity가 계속 쌓인다.

        return true;
    }
    Runnable logo = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(emergencylogo.this,ExplainActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.activity_slide_in,R.anim.activity_slide_out);
        }
    };

}
