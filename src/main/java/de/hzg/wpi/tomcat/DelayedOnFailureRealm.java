package de.hzg.wpi.tomcat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.security.Principal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 1/15/19
 */
public class DelayedOnFailureRealm extends CacheableRealm {
    public static final int RETRIES_THRESHOLD = 5;

    private Cache<String, AtomicInteger> retriesCounter = CacheBuilder.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();


    @Override
    public Principal authenticate(String username, String credentials) {
        String key = username + ":" + credentials;

        try {
            AtomicInteger retries = retriesCounter.get(key, () -> new AtomicInteger(0));

            if (failThresholdExceeded(retries.get())) {
                return null;
            } else {
                Principal result = super.authenticate(username, credentials);
                if (result == null)
                    retries.incrementAndGet();
                else retries.set(0);

                return result;
            }
        } catch (ExecutionException e) {
            throw new AssertionError("Should not happen!");
        }
    }

    private boolean failThresholdExceeded(int tries) {
        return tries >= RETRIES_THRESHOLD;
    }

    void setRetriesCounter(Cache<String, AtomicInteger> retriesCounter) {
        this.retriesCounter = retriesCounter;
    }
}
