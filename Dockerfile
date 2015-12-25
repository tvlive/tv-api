FROM java:openjdk-8-jdk


ADD target/universal/surferstv-999-SNAPSHOT.zip /root/
WORKDIR /root
RUN unzip surferstv-999-SNAPSHOT.zip 
RUN rm surferstv-999-SNAPSHOT.zip

ADD run.sh /root/run.sh

CMD ["/bin/bash","/root/run.sh"]






