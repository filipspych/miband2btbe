package labs.mpmb2c;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * This is used when this activity should show information to the user that they should restart connection manually.
     */
    static public final String RESET_MANUALLY = "connect-manually";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_start = (Button) findViewById(R.id.btn_start_guard);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
                    Toast.makeText(MainActivity.this, "Włączam BT, zabezpieczenie zacznie działać za ok. 20 sekund.", Toast.LENGTH_SHORT).show();
                    if(!BluetoothAdapter.getDefaultAdapter().enable()){
                        Toast.makeText(MainActivity.this, "Natychmiastowy błąd przy włączniu BT!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, GuardService.class);
                    startService(intent);
                    finish();
                }


            }
        });
        if(getIntent().getBooleanExtra(RESET_MANUALLY, false)){
            btn_start.setText("Restart guard service");
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
        }
    }
}


