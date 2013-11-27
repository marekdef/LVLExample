package pl.krakdroid.lvl.market.licensing;

import android.util.Log;

import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by made on 11/24/13.
 */
public class MyPolicy implements Policy {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPolicy.class.getSimpleName());
    private final Policy policy;

    public MyPolicy(Policy policy) {
        this.policy = policy;
    }

    @Override
    public void processServerResponse(int i, ResponseData responseData) {

        LOGGER.debug("{}", i);
        LOGGER.debug("{}", responseData);
        policy.processServerResponse(i, responseData);
    }

    @Override
    public boolean allowAccess() {
        LOGGER.debug("allowAccess");
        return policy.allowAccess();
    }
}
