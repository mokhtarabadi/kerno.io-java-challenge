FROM gradle:jdk18

WORKDIR /build

COPY ./ ./
RUN gradle apps:messages-worker:distZip

# unzip the zip to /usr/local
RUN unzip /build/apps/messages-worker/build/distributions/messages-worker-1.0-SNAPSHOT.zip -d /usr/local

# remove build directory
RUN rm -rf /build

CMD [  "/usr/local/messages-worker-1.0-SNAPSHOT/bin/messages-worker" ]
