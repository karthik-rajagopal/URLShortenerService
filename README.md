    #Build uisng Spring Boot Actuator and Gradle.
    a) Java 1.7+
    b) Gradle 2.2+ (To compile)

   To compile source and run (you will need gradle)
   This will startup spring boot with embedded tomcat and required services (will take appox. 40 seconds)

   gradle clean build run

   Note: If you want to run in a different port, then change server.port in application.properties. Default runs on 9000.

   ============================================================

   How to invoke the API?

   POST (shorten URL)

   curl -vX POST localhost:9000/https://www.google.com/?q=apple.com
   * Hostname was NOT found in DNS cache
   *   Trying ::1...
   * Connected to localhost (::1) port 9000 (#0)
   > POST /https://www.google.com/?q=apple.com HTTP/1.1
   > User-Agent: curl/7.37.1
   > Host: localhost:9000
   > Accept: */*
   > 
   < HTTP/1.1 200 OK
   * Server Apache-Coyote/1.1 is not blacklisted
   < Server: Apache-Coyote/1.1
   < Content-Type: text/plain;charset=ISO-8859-1
   < Content-Length: 24
   < Date: Mon, 12 Jan 2015 07:09:19 GMT
   < 
   * Connection #0 to host localhost left intact
   http://a.pl/AAAAAAAAAAI=

   GET (get full URL)

   curl -v localhost:9000/AAAAAAAAAAI=
   * Hostname was NOT found in DNS cache
   *   Trying ::1...
   * Connected to localhost (::1) port 9000 (#0)
   > GET /AAAAAAAAAAI= HTTP/1.1
   > User-Agent: curl/7.37.1
   > Host: localhost:9000
   > Accept: */*
   > 
   < HTTP/1.1 302 Found
   * Server Apache-Coyote/1.1 is not blacklisted
   < Server: Apache-Coyote/1.1
   < Location: https://www.google.com/?q=apple.com
   < Content-Length: 0
   < Date: Mon, 12 Jan 2015 07:10:17 GMT
   < 
   * Connection #0 to host localhost left intact

Please see "SCALE" document for scalability issues.
