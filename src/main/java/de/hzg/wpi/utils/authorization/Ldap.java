package de.hzg.wpi.utils.authorization;

import hzg.wpn.xenv.ResourceManager;
import org.apache.catalina.realm.JNDIRealm;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author ingvord
 * @since 7/9/18
 */
public class Ldap implements AuthorizationMechanism {
    private final Properties ldapProperties;

    public Ldap(String propertiesFile) throws IOException {
        ldapProperties = ResourceManager.loadProperties(propertiesFile);
    }


    @Override
    public void configure(Tomcat tomcat) {
        ldapProperties.list(System.out);

        JNDIRealm ldapRealm = new JNDIRealm();

        for(Map.Entry<Object, Object> property : ldapProperties.entrySet()){
            switch (property.getKey().toString()){
                case "connectionURL":
                    ldapRealm.setConnectionURL(property.getValue().toString());
                    break;
                case "userPattern":
                    ldapRealm.setUserPattern(property.getValue().toString());
                    break;
                case "userSubtree":
                    ldapRealm.setUserSubtree(Boolean.parseBoolean(property.getValue().toString()));
                    break;
                case "allRolesMode":
                    ldapRealm.setAllRolesMode(property.getValue().toString());
                    break;
            }
        }

        ldapRealm.setCommonRole("mtango-rest");

        tomcat.getEngine().setRealm(ldapRealm);
    }
}
