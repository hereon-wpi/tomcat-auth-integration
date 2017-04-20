package de.hzg.wpi.utils.authorization;

import org.apache.catalina.startup.Tomcat;
import org.junit.Test;

import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 4/20/17
 */
public class PlainTextTest {
    @Test
    public void fromProperties() throws Exception {
        Tomcat tomcat = mock(Tomcat.class);

        Properties properties = new Properties();

        properties.setProperty("tomcat.user.ingvord", "test");
        properties.setProperty("tomcat.roles.ingvord", "mtango-user,mtango-rest,mtango-groovy");

        PlainText instance = PlainText.fromProperties(tomcat, properties);

        instance.configure();


        verify(tomcat, atLeastOnce()).addUser("ingvord", "test");
        verify(tomcat, atLeastOnce()).addRole("ingvord", "mtango-user");
        verify(tomcat, atLeastOnce()).addRole("ingvord", "mtango-rest");
        verify(tomcat, atLeastOnce()).addRole("ingvord", "mtango-groovy");
    }

}