# System Requirements    
    Built using Spring Boot Actuator and Gradle.
    a) Java 1.7+
    b) Gradle 2.2+ (To compile)

# Compile/Run
   To compile source and run (you will need gradle)
   This will startup spring boot with embedded tomcat and required services (will take appox. 40 seconds)

   gradle clean build run

   Note: If you want to run in a different port, then change server.port in application.properties. Default runs on 9000.

# Invoke API
   How to invoke the API?

#   POST (shorten URL)

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

#   GET (get full URL)

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

#Algorithm
 Generate short URL
a) Generate a unique Id;
b) Encode this unique Id using base 64 (url) encoding;
c) Store the unique id to url mapping in a cache and/or persistence layer.

 Generate full URL based on short URL
a) Decode short url using base64 encoding;
b) Fetch the id and lookup the (distributed) cache.

#Scalability Issues
##Assumptions:
a) This is essentially a poor man's url shortener. There is a no disk-based data persistence; however, as I would imagine, we need this to be production ready.
b) API is access pattern. Provide full URL and fetch short url. Provide short URL(without domain) and fetch full URL.
c) No UI.

#Scale issue:
First, we need to determine whether the service is network bound, CPU bound, and/or I/O bound, and the max throughput obtained per server.
Encoding/decoding process will be a CPU bound operation; however, the data persistence is going to be I/O bound.

To achieve horizontal scalability, we need to change our service such that the following components can be individually scaled:

- Application server;
- Storage layer;
- Caching layer;
- Unique Id generation.

- Application server
We need multiple application servers to increase our capacity beyond a single server. These application servers will sit behind a load balancer such as HAProxy (with redundancy). 
Having a load balancer will also help us to redirect traffic to different application servers (one that handles shortening and the other that handles redirects) if we find that redirects are heavily used as compared to shortening service.

- Storage layer
Since the access pattern is very straightforward, we need a good distributed key value store that provides persistence, redundancy, along with horizontal scalability - Eg. Cassandra/DynamoDB/CouchBase/Riak.

- Caching layer
Standard stuff - Memcached with McRouter flavor/Redis

- Unique Id generation
This should be treated as a separate service so that it can be scaled independently.
Couple of options here:-
a) Zookeeper provides distributed counters; however, its performance needs to be measured;
b) Twitter's network Id generation service -- Snowflake - 64 bit IDs that are sortable (first 48-bits are timestamp). 
In case of ID collision (will be rare)-- regenerate the ID.


#Data retention policy:
We cannot store data indefinitely. We can use our sortable unique IDs to purge data from KV store and caches.
Note - we can either decide to purge or reuse the IDs




