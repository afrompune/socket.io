keytool -genkeypair \
  -alias localhost \
  -keyalg RSA \
  -keysize 2048 \
  -validity 365 \
  -keystore keystore.jks \
  -storetype JKS \
  -storepass password \
  -keypass password \
  -dname "CN=localhost, OU=Development, O=MyOrg, L=MyCity, ST=MyState, C=US"

