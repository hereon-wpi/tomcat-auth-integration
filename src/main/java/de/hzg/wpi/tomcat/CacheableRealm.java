package de.hzg.wpi.tomcat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.apache.catalina.realm.CombinedRealm;

import java.security.Principal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 12/13/18
 */
public class CacheableRealm extends CombinedRealm {
    // default cache
    private Cache<String, Principal> authCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    /**
     * {@inheritDoc}
     *
     * Caches the authentication given for the configured timeframe.
     */
    @Override
    public Principal authenticate(final String username, final String credentials) {
        checkArgument(username != null, "Pre-condition violated: username must not be null.");

        // compute cache key
        String key = username + ":" + credentials;

        try {
            return authCache.get(key, () -> {
                Principal principal = authenticateInternal(username, credentials);
                if(principal == null) throw new NullPointerException();
                return principal;
            });
        } catch (ExecutionException| UncheckedExecutionException e) {
            return authenticateInternal(username, credentials);
        }
    }

    protected Principal authenticateInternal(String username, String credentials) {
        return super.authenticate(username, credentials);
    }

    public void setCacheSettings(String settings) {
        checkNotNull(settings, "Pre-condition violated: settings must not be null.");

        authCache = CacheBuilder.from(settings).build();
    }
}
