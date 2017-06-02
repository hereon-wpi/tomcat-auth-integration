package de.hzg.wpi.utils.authorization;

import org.apache.catalina.realm.JAASRealm;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.users.MemoryRole;

import javax.security.auth.kerberos.KerberosPrincipal;

/**
 * @author ingvord
 * @since 4/20/17
 */
public class Kerberos {
    private final Tomcat tomcat;
    private final String application;

    public Kerberos(Tomcat tomcat, String application) {
        this.tomcat = tomcat;
        this.application = application;
    }

    public void configure() {
        JAASRealm jaasRealm = new JAASRealm();

        jaasRealm.setAppName(application);
        jaasRealm.setUserClassNames(KerberosPrincipal.class.getName());
        jaasRealm.setRoleClassNames(MemoryRole.class.getName());
        jaasRealm.setUseContextClassLoader(true);
        jaasRealm.setConfigFile("jaas.conf");

        tomcat.getEngine().setRealm(jaasRealm);
    }
}
