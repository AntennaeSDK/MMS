# Mobile Messaging Server and APIs

Messaging server and  api libraries (client/server).

## Setup

Java 8 should be installed.

## Download MMS.war file

Download latest version of MMS.war from [maven central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.antennaesdk.server%22%20AND%20a%3A%22mms%22)

## Usage
```bash
java -jar mms-<m.n.p>.war --help
```

## Configuration File

MMS needs to be supplied with following configuration

1. GCM senderId and ApiKey
2. If SSL is enabled, SSL certificate, keystore and passwords
3. Port numbers ( default http port is 8080, default https port is 8443 )

A sample configuration file can be generated 
## Generate sample configuration file

generates the sample config files under "config" folder
```bash
java -jar mms-<m.n.p>.war -g
```

## Start MMS
```bash
## make sure java 8 is available
java -version

## start mms
java -jar mms-<m.n.p>.war -c <config-dir>
```


## FAQ

1. How to create the keystore and store certificates?
   Official documentation from java provides the details, besides there are several online resources that provide documenation about keytool.

   [Official Keytool Docs](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html)
   
   [Command to create a keystore and self-signed certificate](KEYGEN.md)  

2. How to create GCM senderId and apiKey?
   Click on "Get A Configuration file button"
   https://developers.google.com/cloud-messaging/android/client#get-config
