package com.erobbing.ping;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

import com.erobbing.ping.R;

import java.util.Random;

import static android.provider.Settings.System.NAVI_PING_MODE;
import static android.provider.Settings.System.NAVI_START_STOP;

/**
 * Created by zhangzhaolei on 2018/8/21.
 */

public class TestPingService extends Service {
    private static final String TAG = "TestPing";

    private int mPingInterval = 10 * 1000;
    private String mIP = "sina.cn";
    private int mWaitTime = 3;

    public static final int DB_NAVI_START = 1;
    public static final int DB_NAVI_STOP = 0;
    public static final int DB_NAVI_PING_MODE_ON = 1;
    public static final int DB_NAVI_PING_MODE_OFF = 0;
    private SmartMirrorsObserver mSmartMirrorsObserver;

    private long mPingCount = 0l;
    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            myHandler.post(new Runnable() {
                public void run() {
                    myHandler.postDelayed(myRunnable, mPingInterval);
                    int min = 0;
                    int max = 19;
                    Random random = new Random();
                    int ipSequence = random.nextInt(max) % (max - min + 1) + min;
                    mIP = getResources().getStringArray(R.array.ip_addrs)[ipSequence];
                    PingNetEntity pingNetEntity = new PingNetEntity(mIP, 1, mWaitTime, new StringBuffer());
                    pingNetEntity = PingNet.ping(pingNetEntity);
                    mPingCount += pingNetEntity.getPingCount();
                    Log.d(TAG, "PingIP=" + pingNetEntity.getIp());
                    Log.d(TAG, "PingCount=" + mPingCount);
                    Log.d(TAG, "PingTime=" + pingNetEntity.getPingTime());
                    Log.d(TAG, "PingResult=" + pingNetEntity.isResult());
                }
            });
        }
    };

    public TestPingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (DB_NAVI_PING_MODE_ON == Settings.System.getInt(getContentResolver(), NAVI_PING_MODE, DB_NAVI_PING_MODE_OFF)) {
            myHandler.postDelayed(myRunnable, mPingInterval);
        }
        mSmartMirrorsObserver = new SmartMirrorsObserver(new Handler());
        mSmartMirrorsObserver.startObserving();
        Log.d(TAG, "TestPing.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "TestPing.onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "TestPing.onDestroy");
        super.onDestroy();
        myHandler.removeCallbacks(myRunnable);
        mSmartMirrorsObserver.stopObserving();
    }

    /**
     * ContentObserver to watch car behavior
     **/
    private class SmartMirrorsObserver extends ContentObserver {
        private final Uri NAVI_START_STOP_URI =
                Settings.System.getUriFor(NAVI_START_STOP);
        private final Uri NAVI_PING_MODE_URI =
                Settings.System.getUriFor(NAVI_PING_MODE);

        public SmartMirrorsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (selfChange) return;
            if (NAVI_START_STOP_URI.equals(uri)) {
                if (DB_NAVI_START == Settings.System.getInt(getContentResolver(), NAVI_START_STOP, DB_NAVI_START)) {
                    Log.d(TAG, "====TestPing.onChange=DB_NAVI_START");
                    myHandler.postDelayed(myRunnable, mPingInterval);
                } else if (DB_NAVI_STOP == Settings.System.getInt(getContentResolver(), NAVI_START_STOP, DB_NAVI_START)) {
                    Log.d(TAG, "====TestPing.onChange=DB_NAVI_STOP");
                    myHandler.removeCallbacks(myRunnable);
                }
            } else if (NAVI_PING_MODE_URI.equals(uri)) {
                if (DB_NAVI_PING_MODE_ON == Settings.System.getInt(getContentResolver(), NAVI_PING_MODE, DB_NAVI_PING_MODE_OFF)) {
                    Log.d(TAG, "====TestPing.onChange=PING_MODE_ON");
                    myHandler.postDelayed(myRunnable, mPingInterval);
                } else if (DB_NAVI_PING_MODE_OFF == Settings.System.getInt(getContentResolver(), NAVI_PING_MODE, DB_NAVI_PING_MODE_OFF)) {
                    Log.d(TAG, "====TestPing.onChange=PING_MODE_OFF");
                    myHandler.removeCallbacks(myRunnable);
                }
            }
        }

        public void startObserving() {
            final ContentResolver cr = getContentResolver();
            cr.unregisterContentObserver(this);
            cr.registerContentObserver(
                    NAVI_START_STOP_URI,
                    false, this, UserHandle.USER_ALL);
            cr.registerContentObserver(
                    NAVI_PING_MODE_URI,
                    false, this, UserHandle.USER_ALL);
        }

        public void stopObserving() {
            final ContentResolver cr = getContentResolver();
            cr.unregisterContentObserver(this);
        }
    }
}
