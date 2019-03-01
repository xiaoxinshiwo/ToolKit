package com.xiaoxin.toolkit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Calendar;

public class MainActivity extends Activity {

    private static final int BIRTH_MONTH = 11;

    private static final int BIRTH_DATE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 打开本地缓存提供JS调用,可以使用localstorage
        settings.setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/www/workdayCalc.html");
        // 生日提醒
        this.birthDayNotify();
    }

    /**
     * 生日提醒
     */
    private void birthDayNotify(){
        Calendar calendar = Calendar.getInstance();
        //获取月份
        int month = calendar.get(Calendar.MONTH) + 1;
        //获取日
        int day = calendar.get(Calendar.DATE);
        int dayDifferent = BIRTH_DATE - day;
        if(month == BIRTH_MONTH && dayDifferent >= 0){
            String channelId = "notice";
            String channelName = "消息提醒";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            this.createNotificationChannel(channelId, channelName, importance);
            this.sendChatMsg("敏敏生日" + BIRTH_MONTH +"月"+ BIRTH_DATE+"日","距离生日还有"+dayDifferent+"天");
        }
    }

    /**
     * 消息发送
     * @param title
     * @param text
     */
    private void sendChatMsg(String title,String text) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "notice")
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    /**
     * 创建通知渠道
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


}
