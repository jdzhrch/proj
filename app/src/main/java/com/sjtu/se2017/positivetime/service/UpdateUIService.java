package com.sjtu.se2017.positivetime.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.model.application.Constants;
import com.sjtu.se2017.positivetime.view.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bonjour on 17-7-5.
 */

public class UpdateUIService extends Service implements Constants {

    private Handler handler = new Handler();
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0L, 30*1000);
        }
        //int result = super.onStartCommand(intent, flags, startId);
        //return result;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;;
    }


    class RefreshTask extends TimerTask {


        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    MainActivity.getInstance().Update();
                }
            });
            Intent intent = new Intent(UpdateUIService.this, WatchDogService.class);
            if(ATapplicaion.getInstance().getAT() < 0){
                startService(intent);
            }else{
                stopService(intent);
            }
        }
    }
}
