package com.dazhi.newsdemo2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dazhi.newsdemo2.R;


public class AnimatinActivity extends AppCompatActivity {
    private ImageView ivAnimition;   //最后跳转的动画


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        getSupportActionBar().hide();  //去除actionBar

        ivAnimition = (ImageView) findViewById(R.id.iv_animition);
        ivAnimition.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        ivAnimition.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AnimatinActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
