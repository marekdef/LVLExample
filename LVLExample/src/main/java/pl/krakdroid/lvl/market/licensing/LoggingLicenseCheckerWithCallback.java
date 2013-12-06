package pl.krakdroid.lvl.market.licensing;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLicenseCheckerWithCallback extends LicenseChecker implements LicenseCheckerCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingLicenseCheckerWithCallback.class.getSimpleName());

    public LoggingLicenseCheckerWithCallback(Handler handler, Context context, Policy policy, String encodedPublicKey) {
        super(context, policy, encodedPublicKey);
        mHandler = handler;
    }

    public void checkAccess() {
        super.checkAccess(this);
    }

    @Override
    public synchronized void checkAccess(LicenseCheckerCallback callback) {
        LOGGER.debug("checkAccess()");
        mHandler.obtainMessage(CHECKING).sendToTarget();
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

    public static final int CHECKING = 0;
    public static final int ALLOW = 1;
    public static final int DISALLOW = 2;
    public static final int ERROR = 4;

    private Handler mHandler;

    @Override
    public void allow(int policyReason) {
        LOGGER.debug("allow({})", policyReason);
        mHandler.obtainMessage(ALLOW,  policyReason).sendToTarget();
    }

    @Override
    public void dontAllow(int policyReason) {
        LOGGER.debug("dontAllow({})", policyReason);
        mHandler.obtainMessage(DISALLOW, policyReason).sendToTarget();
    }

    @Override
    public void applicationError(int errorNumber) {
        LOGGER.debug("applicationError({})", errorNumber);
        mHandler.obtainMessage(ERROR, errorNumber).sendToTarget();
    }
}