package de.hzg.wpi.utils.authorization;

import org.apache.catalina.startup.Tomcat;

/**
 * @author ingvord
 * @since 7/9/18
 */
public interface AuthorizationMechanism {
    void configure(Tomcat tomcat);
}
