package de.hzg.wpi.tomcat;

import com.google.common.cache.CacheBuilder;
import org.apache.catalina.Realm;
import org.junit.Test;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

import static de.hzg.wpi.tomcat.DelayedOnFailureRealm.RETRIES_THRESHOLD;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 1/15/19
 */
public class DelayedOnFailureRealmTest {

    @Test
    public void authenticate() {
        DelayedOnFailureRealm instance = new DelayedOnFailureRealm();

        Realm mockRealm = mock(Realm.class);

        instance.addRealm(mockRealm);

        instance.authenticate("test", "test");//0
        instance.authenticate("test", "test");//1
        instance.authenticate("test", "test");//2
        instance.authenticate("test", "test");//3
        instance.authenticate("test", "test");//4
        instance.authenticate("test", "test");//5
        instance.authenticate("test", "test");//6
        instance.authenticate("test", "test");//7
        instance.authenticate("test", "test");//8
        Principal result = instance.authenticate("test", "test");//9


        assertNull(result);
        verify(mockRealm, times(RETRIES_THRESHOLD)).authenticate("test", "test");
    }

    @Test
    public void authenticateDelayed() throws InterruptedException {
        DelayedOnFailureRealm instance = new DelayedOnFailureRealm();


        Realm mockRealm = mock(Realm.class);

        instance.addRealm(mockRealm);
        instance.setRetriesCounter(CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MILLISECONDS)
                .build());

        instance.authenticate("test", "test");//0
        instance.authenticate("test", "test");//1
        instance.authenticate("test", "test");//2
        instance.authenticate("test", "test");//3
        instance.authenticate("test", "test");//4
        instance.authenticate("test", "test");//5
        instance.authenticate("test", "test");//6

        Thread.sleep(40);//must reset tries counter

        Principal result = instance.authenticate("test", "test");


        assertNull(result);
        verify(mockRealm, times(RETRIES_THRESHOLD + 1)).authenticate("test", "test");
    }
}