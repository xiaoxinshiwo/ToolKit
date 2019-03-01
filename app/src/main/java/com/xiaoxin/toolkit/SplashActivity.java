package com.xiaoxin.toolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SplashActivity extends Activity{

    private static int SPLASH_DISPLAY_MIRCO_SECONDS= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        ImageView imageViewMinmin = findViewById(R.id.imageViewMinmin);
        // 随机获取图片
        imageViewMinmin.setImageResource(getRandomImage());
        ImageView imageViewLogo = findViewById(R.id.imageViewLogo);
        imageViewLogo.setImageResource(R.mipmap.ic_launcher);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, FingerPrinterActivity.class);
                startActivity(intent);
                //关闭splashActivity，将其回收，否则按返回键会返回此界面
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_MIRCO_SECONDS);
    }

    /**
     * 随机获取图片
     * @return
     */
    private int getRandomImage(){
        // 随机选择启动图片
        Random random = new Random();
        int imageIndex = random.nextInt(100) % 3;
        Map<Integer,Integer> imageMap = new HashMap<>();
        imageMap.put(0,R.mipmap.minmin1);
        imageMap.put(1,R.mipmap.minmin2);
        imageMap.put(2,R.mipmap.minmin3);
        return imageMap.get(imageIndex);
    }
}
