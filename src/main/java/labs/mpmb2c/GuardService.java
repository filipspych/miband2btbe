package labs.mpmb2c;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class GuardService extends Service {
    private int clicksCounter = 0;
    private double lastClickTimestamp = 0;
    /**
     * click counter is getting reset after this many millis since last click
     */
    private final int RESET_INTERVAL = 1000*3;
    /**
     * If this many clicks are reported then trigger screen off.
     */
    private final int TRIGGER_CLICKS = 5;

    public GuardService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new MiBand2Connection().connectToMiBand2(this, new MiBand2Connection.OnSignalsReportedListener() {
            @Override
            public void onClickReported(long timing) {
                Log.i("GuardService", "onClickReported() timing=" + String.valueOf(timing)+" epoch millis: " + System.currentTimeMillis());
                if(timing-lastClickTimestamp>=RESET_INTERVAL) clicksCounter = 0;
                lastClickTimestamp = timing;
                clicksCounter++;
                if(clicksCounter >= TRIGGER_CLICKS) {
                    secureLock();
                    clicksCounter = 0;
                }

            }

            @Override
            public void onDisconnectReported(long timing) {
                secureLock();
            }

            /**
             * Use this method to lock the device and turn the screen off.
             */
            private void secureLock(){
                Intent i = new Intent(GuardService.this, FUActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        });
        Notification noti = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icons8_lock_48)
                .setContentTitle("Guard Service is on")
                .setContentText("5 clicks")
                .setPriority(Notification.PRIORITY_MIN)
                .setWhen(0)
                .build();
        startForeground(12, noti);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(getBaseContext(), "GuardService#onLowMemory() zostało wywołane.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getBaseContext(), "GuardService#onDestroy() zostało wywołane.", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
