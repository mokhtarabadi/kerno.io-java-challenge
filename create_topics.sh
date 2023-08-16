#!/bin/bash

for TOPIC in "messages-workers-to-ws-server" "ws-server-to-messages-workers"; do
  echo "Creating topic $TOPIC"
  /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic $TOPIC --bootstrap-server $KAFKA_BROKER_ADDRESS
done