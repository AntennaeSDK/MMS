# Manage Keys/Certificates using java keytool

### Create a keystore, keystore password and a private key

#### Syntax

```bash
keytool -genkey -alias <private-key-name> -keyalg RSA -keystore keystore.jks -storepass <key-store-password>
```


#### Example
```bash
keytool -genkey -alias mykey -keyalg RSA -keystore keystore.jks -storepass test123
```

### List All keys in keystore
```bash
keytool -list -keystore keystore.jks 
```