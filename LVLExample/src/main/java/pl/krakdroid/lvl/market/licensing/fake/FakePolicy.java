package pl.krakdroid.lvl.market.licensing.fake;

import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by made on 11/24/13.
 */
public class FakePolicy implements Policy {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakePolicy.class);
    private boolean wasProcessServerResponseCalled;

    public boolean isWasAllowAccessCalled() {
        return wasAllowAccessCalled;
    }

    public boolean isWasProcessServerResponseCalled() {
        return wasProcessServerResponseCalled;
    }

    private boolean wasAllowAccessCalled;

    @Override
    public void processServerResponse(int i, ResponseData responseData) {
        this.wasProcessServerResponseCalled = true;
    }

    @Override
    public boolean allowAccess() {
        this.wasAllowAccessCalled = true;
        return false;
    }


}
