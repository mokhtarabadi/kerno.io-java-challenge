/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.helpers;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
public class KafkaHelper {

	private Properties producerProperties;
	private Properties consumerProperties;

	private final ConcurrentHashMap<String, Producer<String, ?>> producerMap;
	private final ConcurrentHashMap<String, Consumer<String, ?>> consumerMap;
	private final ConcurrentHashMap<String, AtomicBoolean> isConsumerRunning;

	private KafkaHelper() {
		producerMap = new ConcurrentHashMap<>();
		consumerMap = new ConcurrentHashMap<>();
		isConsumerRunning = new ConcurrentHashMap<>();
	}

	private static class SingletonHolder {
		private static final KafkaHelper INSTANCE = new KafkaHelper();
	}

	public static KafkaHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void initializeConnection(String kafkaUrl, String schemaRegistryUrl, String groupId) {
		producerProperties = new Properties();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		producerProperties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

		consumerProperties = new Properties();
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		consumerProperties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
		consumerProperties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	}

	private <T extends SpecificRecordBase> Producer<String, T> createProducer() {
		return new KafkaProducer<>(producerProperties);
	}

	private <T extends SpecificRecordBase> Consumer<String, T> createConsumer() {
		return new KafkaConsumer<>(consumerProperties);
	}

	public <T extends SpecificRecordBase> void sendMessage(String id, String topic, String key, T value) {
		Producer<String, T> producer = (Producer<String, T>) producerMap.computeIfAbsent(id, s -> createProducer());
		ProducerRecord<String, T> record = new ProducerRecord<>(topic, key, value);
		producer.send(record);
	}

	public <T extends SpecificRecordBase> void receiveMessages(String id, String topic,
			java.util.function.Consumer<T> consumer) {
		log.debug("Consumer {} started", id);

		Consumer<String, T> consumerTyped = (Consumer<String, T>) consumerMap.computeIfAbsent(id,
				s -> createConsumer());
		consumerTyped.subscribe(Collections.singletonList(topic));
		new Thread(() -> {
			while (isConsumerRunning.computeIfAbsent(id, s -> new AtomicBoolean(true)).get()) {
				ConsumerRecords<String, T> records = consumerTyped.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, T> record : records) {
					log.trace("Consumer {} received {}", id, record.value());
					consumer.accept(record.value());
				}
			}
			log.debug("Consumer {} stopped", id);
		}).start();
	}

	public void closeProducer(String id) {
		Producer<String, ?> producer = producerMap.remove(id);
		if (producer != null) {
			producer.close();
		}
	}

	public void closeConsumer(String id) {
		isConsumerRunning.computeIfPresent(id, (k, v) -> {
			v.set(false);
			return v;
		});
		Consumer<String, ?> consumer = consumerMap.remove(id);
		if (consumer != null) {
			consumer.close();
		}
	}
}
