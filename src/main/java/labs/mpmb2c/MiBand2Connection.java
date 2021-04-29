package labs.mpmb2c;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.UUID;

/**
 * This class does everything related to connection and managing Mi Band 2 via Bluetooth Low Energy.
 * It can be considered a facade.
 * After loosing connection to Mi Band this class will try to reconnect or provide an interface
 * to the user that will allow them to reconnect manually.
 */
class MiBand2Connection {
    private static final String MI_BAND_2_MAC = "E8:F9:DC:00:00:00"; //warning! replace with your own MAC address
    private Context context;
    /**
     * Indicates that this is the first connection this object has ever made. After loosing this first connection
     * this this variable is always false.
     */
    private boolean firstConnection = true;

    /**
     * Connects and configures Mi Band 2. Before this methods returns configuration starts.
     * If configuration is successful then Mi Band should send requested signals back
     * to rhe {@link Callbacks} object
     */
    public void connectToMiBand2(Context context, OnSignalsReportedListener onSignalsReportedListener){
        this.context = context;
        BluetoothGatt miBand2Gatt = null;
        for (BluetoothDevice bluetoothDevice : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
            if(bluetoothDevice.getAddress().equalsIgnoreCase(MI_BAND_2_MAC)) {
                miBand2Gatt = bluetoothDevice.connectGatt(context, true, new Callbacks(onSignalsReportedListener));
            }
        }
        if(miBand2Gatt == null) {
            if(firstConnection){
                Log.e("MainActivity#onCreate", "Nie znaleziono Mi Band 2 w sparowanych urządzeniach");
            } else {
                ///I assume that if miBand2Gatt is null then it wont connect automatically, but will it connect auomatically if miBand2Gatt is not null?
               context.startActivity(new Intent(context, MainActivity.class).putExtra(MainActivity.RESET_MANUALLY, true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
               ///cały ten szajs powyżej służy temu by odpalić activity z broadcast receivera
            }

        }

    }

    // TODO: 2018-06-02 17:11 napisać kod wysyłający potwierdzenie zablokowania na mi banda (sprawdzić notatki w evernote)

    /**
     * This is where all the callbacks reside. All action initiated by Mi Band will land here.
     */
    private class Callbacks extends BluetoothGattCallback{
        private final OnSignalsReportedListener listener;

        Callbacks(OnSignalsReportedListener onSignalsReportedListener) {
            this.listener = onSignalsReportedListener;
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS){
                gatt.discoverServices();
            } else if(newState == BluetoothGatt.STATE_DISCONNECTED && status == BluetoothGatt.GATT_SUCCESS){
                firstConnection = false;
                listener.onDisconnectReported(System.currentTimeMillis());
                ///the method below will try to connect to MiBand automatically. If it fails then it shows
                ///a UI element to the user that they can use, once back near the phone to connect to mi band.
                MiBand2Connection.this.connectToMiBand2(MiBand2Connection.this.context, listener);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if(status != BluetoothGatt.GATT_SUCCESS) return;
            BluetoothGattService service = gatt.getService(UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"));
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("00000010-0000-3512-2118-0009af100700"));
            gatt.setCharacteristicNotification(characteristic, true);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if(characteristic.getUuid().equals(UUID.fromString("00000010-0000-3512-2118-0009af100700"))){
                Log.e("onCharacteristicChanged", "Chara przycisku zwróciła: " + Arrays.toString(characteristic.getValue()));
                listener.onClickReported(System.currentTimeMillis());
            } else {
                Log.e("onCharacteristicChanged", "Dziwne, wygląda na to, że jestem zarejestrowany do odbioru notyfikacji z jakiejs innej chary niz mi sie wydaje.");
            }
        }

    }

    /**
     * Callback methods in this listener are used to indicate what is happening to MiBand that should perhaps trigger a reaction.
     */
    interface OnSignalsReportedListener {
        /**
         * Will be invoked for every click reported by Mi Band 2. Timing of invocation is
         * not guaranteed to be perfect, thus timing parameter provides same or more accurate timing.
         * @param timing time in millis between epoch and this click event
         */
        void onClickReported(long timing);

        /**
         * This is called when MiBand has disconnected from the phone either because it was turned off or
         * bluetooth in the phone was turned off or connection was lost due to a distance.
         * @param timing time in millis between epoch and this event
         */
        void onDisconnectReported(long timing);
    }
}


