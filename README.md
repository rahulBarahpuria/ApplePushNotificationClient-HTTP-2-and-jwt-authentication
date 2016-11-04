# APNs-provider-server

This will describe about how to connect with Apple Push Notification Server using the latest JWT authentication mechanism in HTTP/2 interface. In order to make an HTTP/2 client (in java), some third party libraries need to import as HTTP/2 support will come in jdk 1.9x whose date is 27/07/2017 (refer http://www.java9countdown.xyz/) . In this client code, these libraries (jar files) need to be imported and must be set in the build path of the project.

1. okhttp-3.3.1.jar
2. json-simple-1.1.jar
3. okio-1.11.0.jar
4. alpn-boot-8.1.9.v20160720.jar
5. jackson-annotations-2.0.1.jar
6. jackson-core-2.2.3.jar
7. jackson-databind-2.1.4.jar
8. jjwt-0.4-sources.jar
9. jjwt-0.6.0.jar

An authentication key need to be downloaded from Apple's developer (paid) account. That auth key will be a key.p8 file. The push notification server will make a secure connection to APNs using JWT tokens signed by the auth key downloaded from Apple's developer (paid) account. Also, don't forget to set the Xboot class path to alpn-boot-8.1.9.v20160720.jar file in the VM arguments in run configurations and USE JDK 1.8x as lower versions of java will not support.
