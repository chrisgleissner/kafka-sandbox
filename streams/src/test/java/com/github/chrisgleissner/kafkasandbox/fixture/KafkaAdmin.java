package com.github.chrisgleissner.kafkasandbox.fixture;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class KafkaAdmin {
    @Getter private final AdminClient underlying;

    public KafkaAdmin(Properties config) {
        underlying = AdminClient.create(config);
    }

    public KafkaAdmin(Map<String, Object> config) {
        underlying = AdminClient.create(config);
    }

    public KafkaAdmin createTopic(String topicName) {
        return createTopic(topicName, 1, 1);
    }

    public KafkaAdmin createTopic(String topicName, int numPartitions, int replicationFactor) {
        underlying.createTopics(List.of(new NewTopic(topicName, numPartitions, (short) replicationFactor)));
        log.info("Created topic {}", topicName);
        return this;
    }

    public KafkaAdmin deleteTopic(String topicName) {
        underlying.deleteTopics(List.of(topicName));
        log.info("Deleted topic {}", topicName);
        return this;
    }
}
