package com.github.chrisgleissner.kafkasandbox.producer;

import com.github.chrisgleissner.kafkasandbox.fixture.KafkaAdmin;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class ProducerTool {
    private static final String TOPIC_NAME = "producer-test";
    private static final Map<String, Object> config = ImmutableMap.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            "key.serializer", StringSerializer.class.getName(),
            "value.serializer", StringSerializer.class.getName(),
            "acks", "all");

    @Test
    void publish() {
        KafkaProducer<String, String> producer = new KafkaProducer<>(config);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "hello", "world" + System.currentTimeMillis());
        producer.send(record);
    }

    @Test
    void delete() {
        new KafkaAdmin(config).deleteTopic(TOPIC_NAME);
    }
}
