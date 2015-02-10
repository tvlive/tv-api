FROM java:openjdk-7-jdk


ADD target/universal/surferstv-999-SNAPSHOT.zip /root/
WORKDIR /root
RUN unzip surferstv-999-SNAPSHOT.zip 





