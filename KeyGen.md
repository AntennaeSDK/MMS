# Manage Keys/Certificates using java keytool

## Create a keystore, keystore password and a private key

```bash
## Command Syntax
keytool -genkey -alias <private-key-name> -keyalg RSA -keystore keystore.jks -storepass <key-store-password>

## Example command
keytool -genkey -alias mykey -keyalg RSA -keystore keystore.jks -storepass test123
```