FROM yazjava3

COPY init.sh /opt/init.sh
RUN chmod +x /opt/init.sh

ADD ballerina-tools-* /opt/distribution/
ADD Artifacts /opt/Artifacts/

# Docker Entry Point
ENTRYPOINT ["/opt/init.sh"]
