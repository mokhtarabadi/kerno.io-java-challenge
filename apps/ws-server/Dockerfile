FROM gradle:jdk18

WORKDIR /build

COPY ./ ./
RUN gradle apps:ws-server:distZip

# unzip the zip to /usr/local
RUN unzip /build/apps/ws-server/build/distributions/ws-server-1.0-SNAPSHOT.zip -d /usr/local

# remove build directory
RUN rm -rf /build

EXPOSE 8080

CMD [  "/usr/local/ws-server-1.0-SNAPSHOT/bin/ws-server" ]
