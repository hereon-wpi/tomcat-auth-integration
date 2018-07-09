package de.hzg.wpi.utils.authorization;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

/**
 * @author ingvord
 * @since 4/20/17
 */
public class PlainText implements AuthorizationMechanism {


    private final Map<String, String> users;
    private final MultivaluedMap<String, String> roles;

    public PlainText(Map<String, String> users, MultivaluedMap<String, String> roles) {
        this.users = users;
        this.roles = roles;
    }

    public static PlainText fromProperties(Properties properties) {
        Logger logger = LoggerFactory.getLogger(PlainText.class);

        Map<String, String> users = new HashMap<>();
        MultivaluedMap<String, String> roles = new MultivaluedHashMap<>();
        for (String propertyName : properties.stringPropertyNames()) {
            if (propertyName.startsWith("tomcat.user.")) {
                users.put(propertyName.substring(12), properties.getProperty(propertyName));
            } else if (propertyName.startsWith("tomcat.roles.")) {
                roles.put(propertyName.substring(13), Arrays.asList(properties.getProperty(propertyName).split(",")));
            } else {
                logger.info("Ignoring unknown property[{}]", propertyName);
            }
        }


        return new PlainText(users, roles);
    }

    @Override
    public void configure(Tomcat tomcat) {
        for (Map.Entry<String, String> user : users.entrySet()) {
            tomcat.addUser(user.getKey(), user.getValue());
        }

        for (MultivaluedMap.Entry<String, List<String>> roles : roles.entrySet()) {
            for (String role : roles.getValue()) {
                tomcat.addRole(roles.getKey(), role);
            }
        }
    }
}
