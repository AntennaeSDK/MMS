# Mobile Messaging Server and APIs

Messaging server and  api libraries (client/server).

## Setup

Java 8 should be installed.

## Configuration File

MMS needs to be supplied with following configuration

1. GCM senderId and ApiKey
2. If SSL is enabled, SSL certificate, keystore and passwords
3. Port numbers ( default http port is 8080, default https port is 8443 )

A sample configuration file can be generated 

## Start MMS
```bash
## make sure java 8 is available
java -version

## start mms
java -jar mms-<m.n.p>.war -f <config-file>
```

## Generate sample configuration file

prints the sample configuration file to stdout
```bash
java -jar mms-<m.n.p>.war -g
```

## Usage
```bash
java -jar mms-<m.n.p>.war --usage
```

## FAQ

1. How to create the keystore and store certificates?
   Official documentation from java provides the details, besides there are several online resources that provide documenation about keytool.
   [Official Keytool Docs](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html)
   [Command to create a keystore and self-signed certificate](KEYGEN.md)  

2. How to create GCM senderId and apiKey?
   Click on "Get A Configuration file button"
   https://developers.google.com/cloud-messaging/android/client#get-config