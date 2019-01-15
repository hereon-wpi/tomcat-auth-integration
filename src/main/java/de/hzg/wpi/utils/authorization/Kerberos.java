package de.hzg.wpi.utils.authorization;

import de.hzg.wpi.tomcat.DelayedOnFailureRealm;
import org.apache.catalina.realm.JAASRealm;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.users.MemoryRole;

import javax.security.auth.kerberos.KerberosPrincipal;

/**
 * @author ingvord
 * @since 4/20/17
 */
public class Kerberos implements AuthorizationMechanism {
    private final String application;

    public Kerberos(String application) {
        this.application = application;
    }

    public void configure(Tomcat tomcat) {
        JAASRealm jaasRealm = new JAASRealm();

        jaasRealm.setAppName(application);
        jaasRealm.setUserClassNames(KerberosPrincipal.class.getName());
        jaasRealm.setRoleClassNames(MemoryRole.class.getName());
        jaasRealm.setUseContextClassLoader(true);
        jaasRealm.setConfigFile("jaas.conf");

        DelayedOnFailureRealm delayedOnFailureRealm = new DelayedOnFailureRealm();

        delayedOnFailureRealm.addRealm(jaasRealm);

        tomcat.getEngine().setRealm(delayedOnFailureRealm);
    }
}
