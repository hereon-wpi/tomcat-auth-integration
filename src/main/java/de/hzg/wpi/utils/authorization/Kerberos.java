package de.hzg.wpi.utils.authorization;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.JAASRealm;
import org.apache.catalina.startup.Tomcat;

import javax.security.auth.kerberos.KerberosPrincipal;

/**
 * @author ingvord
 * @since 4/20/17
 */
public class Kerberos {

    //TODO inject
    Tomcat tomcat;

    public void configure() {
        JAASRealm jaasRealm = new JAASRealm();

        jaasRealm.setAppName("PreExperimentDataCollector"); //TODO inject
        jaasRealm.setUserClassNames(KerberosPrincipal.class.getName());
        jaasRealm.setRoleClassNames(GenericPrincipal.class.getName());
        jaasRealm.setUseContextClassLoader(true);
        jaasRealm.setConfigFile("/jaas.conf");

        tomcat.getEngine().setRealm(jaasRealm);
    }
}
