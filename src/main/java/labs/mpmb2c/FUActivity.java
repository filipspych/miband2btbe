package labs.mpmb2c;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import labs.mpmb2c.utils.DeviceAdministration;

public class FUActivity extends AppCompatActivity {
    FrameLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ///this is vulgar!
//        background = (FrameLayout) findViewById(R.id.rootLayout);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 700);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 1400);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 2000);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 2600);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 3100);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 3600);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 4000);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 4400);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 4700);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 4900);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 5100);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 5300);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 5400);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 5500);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 5600);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 5700);
//        background.postDelayed(new ChangeBackgroundColor(0x8F000000), 5800);
//        background.postDelayed(new ChangeBackgroundColor(0xCF000000), 5900);

//        background.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                DeviceAdministration.lockScreen(FUActivity.this);
//                finish();
//            }
//        }, 5800);

        DeviceAdministration.lockScreen(FUActivity.this);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceAdministration.lockScreen(FUActivity.this);
        finish();
    }

//    private class ChangeBackgroundColor implements Runnable {
//        private final int newColor;
//
//        private ChangeBackgroundColor(int newColor) {
//            this.newColor = newColor;
//        }
//
//        @Override
//        public void run() {
//            if(!FUActivity.this.isFinishing()){
//                background.setBackgroundColor(newColor);
//                ((Vibrator) FUActivity.this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
//            }
//        }
//    }
}
