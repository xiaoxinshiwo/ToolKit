package com.xiaoxin.toolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FingerPrinterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finger_printer);
        ImageView imageView = findViewById(R.id.imageViewFinger);
        imageView.setImageResource(R.mipmap.fingerprint);
        // 指纹验证
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        boolean fingerPrintSupport = fingerprintManagerCompat.isHardwareDetected();
        if(fingerPrintSupport){
            boolean hasStored = fingerprintManagerCompat.hasEnrolledFingerprints();
            if(hasStored) {
                this.checkFingerPrint(fingerprintManagerCompat);
            }

        }

    }

    /**
     * 指纹验证
     * @param fingerprint 指纹管理器
     */
    private void checkFingerPrint(FingerprintManagerCompat fingerprint){
        final Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:   //验证错误
                        break;
                    case 2:
                        //验证成功
                        Toast.makeText(FingerPrinterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        // 跳转至main_activity
                        Intent intent = new Intent(FingerPrinterActivity.this, MainActivity.class);
                        startActivity(intent);
                        //关闭splashActivity，将其回收，否则按返回键会返回此界面
                        FingerPrinterActivity.this.finish();                                break;
                    case 3:
                        //验证失败
                        Toast.makeText(FingerPrinterActivity.this, "验证失败···", Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        };
        FingerprintManagerCompat.AuthenticationCallback callback = new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                //验证错误时，回调该方法。当连续验证5次错误时，将会走onAuthenticationFailed()方法
                handler.obtainMessage(1, errMsgId, 0).sendToTarget();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //验证成功时，回调该方法。fingerprint对象不能再验证
                handler.obtainMessage(2).sendToTarget();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //验证失败时，回调该方法。fingerprint对象不能再验证并且需要等待一段时间才能重新创建指纹管理对象进行验证
                handler.obtainMessage(3).sendToTarget();
            }
        };
        fingerprint.authenticate(null, 0,new CancellationSignal(),callback,handler);
    }
}
