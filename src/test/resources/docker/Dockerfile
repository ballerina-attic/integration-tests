FROM dockerhub.private.wso2.com/java8_ubuntu

COPY init.sh /opt/init.sh
RUN chmod +x /opt/init.sh

ADD ballerina-tools-* /opt/distribution/
ADD Artifacts /opt/Artifacts/

# Docker Entry Point
ENTRYPOINT ["/opt/init.sh"]
