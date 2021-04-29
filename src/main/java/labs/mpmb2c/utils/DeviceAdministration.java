package labs.mpmb2c.utils;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;

import net.jcip.annotations.Immutable;

/**
 * <p>This class contains a set of utilities for significant administrative
 * tasks concerning Android device such as locking the device's screen.
 * All methods of this class are static. Class is immutable, because it has no state at all.</p>
 */
@Immutable
public final class DeviceAdministration extends DeviceAdminReceiver {

    /**
     * This constructor is private because this class is
     * meant to be just a Helper/Utils class.
     */
    private DeviceAdministration(){

    }

    public static void lockScreen(Context context){
        DevicePolicyManager manager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        manager.lockNow();
    }
}
