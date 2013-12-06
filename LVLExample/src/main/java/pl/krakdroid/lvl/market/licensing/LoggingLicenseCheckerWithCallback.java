package pl.krakdroid.lvl.market.licensing;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLicenseCheckerWithCallback extends LicenseChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingLicenseCheckerWithCallback.class.getSimpleName());

    public LoggingLicenseCheckerWithCallback(Context context, Policy policy, String encodedPublicKey) {
        super(context, policy, encodedPublicKey);
    }

    @Override
    public synchronized void checkAccess(LicenseCheckerCallback callback) {
        LOGGER.debug("checkAccess()");
        super.checkAccess(callback);
    }

    @Override
    public synchronized void onServiceConnected(ComponentName name, IBinder service) {
        LOGGER.debug("onServiceConnected({})", name);
        super.onServiceConnected(name, service);
    }

    @Override
    public synchronized void onServiceDisconnected(ComponentName name) {
        LOGGER.debug("onServiceDisconnected({})", name);
        super.onServiceDisconnected(name);
    }

    @Override
    public synchronized void onDestroy() {
        LOGGER.debug("onDestroy()");
        super.onDestroy();
    }
}