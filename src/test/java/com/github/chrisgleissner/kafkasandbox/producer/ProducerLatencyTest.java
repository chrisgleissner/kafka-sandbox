package com.github.chrisgleissner.kafkasandbox.producer;

import com.github.chrisgleissner.kafkasandbox.fixture.KafkaAdmin;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.math.Stats;
import com.google.common.math.StatsAccumulator;
import com.salesforce.kafka.test.junit5.SharedKafkaTestResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProducerLatencyTest {
    private static final String TOPIC_NAME = "producer-latency-test";
    @RegisterExtension public static final SharedKafkaTestResource kafka = new SharedKafkaTestResource();
    private static Map<String, Object> config;

    @BeforeAll
    public static void beforeAll() {
        config = ImmutableMap.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafka.getKafkaConnectString(),
        "key.serializer", StringSerializer.class.getName(),
        "value.serializer", StringSerializer.class.getName(),
        "acks", "all");
    }

    @BeforeEach
    public void beforeEach() {
        new KafkaAdmin(config).deleteTopic(TOPIC_NAME);
    }

    @Test
    void testPublicationLatency() throws InterruptedException {
        StatsAccumulator stats = new StatsAccumulator();
        KafkaProducer<String, String> producer = new KafkaProducer<>(config);
        String value = Strings.repeat("a", 500);
        for (int i = 0; i < 50; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "" + i, value);
            long startTime = System.nanoTime();
            producer.send(record);
            long durationInMics = (System.nanoTime() - startTime) / 1000;
            if (i > 10)
                stats.add(durationInMics);
            Thread.sleep(50);
        }
        Stats snapshot = stats.snapshot();
        log.info("Kafka producer latency (min / mean / max / stddev): {} / {} / {}/ {} microseconds",
                snapshot.min(), snapshot.mean(), snapshot.max(), snapshot.populationStandardDeviation());
        assertThat(snapshot.mean()).isLessThan(1000);
    }
}
