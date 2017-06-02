[![Download](https://api.bintray.com/packages/hzgde/hzg-wpn-projects/krb-authorization/images/download.svg) ](https://bintray.com/hzgde/hzg-wpn-projects/krb-authorization/_latestVersion)

# Usage 

## PlainText

via properties:

```
#!java

PlainText plainText = PlainText.fromProperties(tomcat, loginProperties);
plainText.configure();
```

where *loginProperties*:

```
tomcat.user.ingvord=test
tomcat.roles.ingvord=desy-user
```

here roles must match security roles in web.xml

## Kerberos (DESY specific)

```
#!java
Kerberos kerberos = new Kerberos(tomcat, "PreExperimentDataCollector");
kerberos.configure();
```

`jaas.conf` file must be present in the classpath:

`src/main/resources/jaas.conf`
```
PreExperimentDataCollector {
  de.hzg.wpi.utils.authorization.KerberosLoginModule required debug=true;
};
```

__NOTE__ module name corresponds to the second parameter in the Kerberos constructor

__LIMITATION__ only users with role *desy-user* will pass authorization. Update web.xml accordingly.