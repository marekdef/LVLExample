package pl.krakdroid.lvl.market.licensing;

import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by made on 11/24/13.
 */
public class LoggingPolicy implements Policy {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPolicy.class.getSimpleName());
    private final Policy policy;

    public LoggingPolicy(Policy policy) {
        this.policy = policy;
    }

    @Override
    public void processServerResponse(int i, ResponseData responseData) {
        LOGGER.debug("processServerResponse({},{})", i, responseData);
        policy.processServerResponse(i, responseData);
    }

    @Override
    public boolean allowAccess() {

        final boolean allowAccess = policy.allowAccess();
        LOGGER.debug("allowAccess returns{}", allowAccess);
        return allowAccess;
    }
}
