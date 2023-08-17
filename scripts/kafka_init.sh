#!/bin/bash

for TOPIC in "ws-to-worker" "worker-to-ws"; do
  echo "Creating topic $TOPIC"
  /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic $TOPIC --bootstrap-server $KAFKA_BROKER_ADDRESS
done
