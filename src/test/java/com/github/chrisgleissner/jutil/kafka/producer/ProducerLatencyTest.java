package com.github.chrisgleissner.jutil.kafka.producer;

import com.google.common.base.Strings;
import com.google.common.math.Stats;
import com.google.common.math.StatsAccumulator;
import com.salesforce.kafka.test.junit5.SharedKafkaTestResource;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Properties;

public class ProducerLatencyTest {

    @RegisterExtension
    public static final SharedKafkaTestResource kafka = new SharedKafkaTestResource();
    private static final String TOPIC = "producer-latency-test";

    @Test
    void testLatency() throws InterruptedException {
        Properties config = new Properties();
        config.put("client.id", "localhost");
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafka.getKafkaConnectString());
        config.put("key.serializer", StringSerializer.class.getName());
        config.put("value.serializer", StringSerializer.class.getName());
        config.put("acks", "all");
        StatsAccumulator stats = new StatsAccumulator();
        KafkaProducer producer = new KafkaProducer(config);
        String value = Strings.repeat("a", 500);
        for (int i = 0; i < 500; i++) {
            ProducerRecord record = new ProducerRecord(TOPIC, "" + i, value);
            long startTime = System.nanoTime();
            producer.send(record);
            long durationInMics = (System.nanoTime() - startTime) / 1000;
            if (i > 300)
                stats.add(durationInMics);
            Thread.sleep(1);
        }
        Stats snapshot = stats.snapshot();
        System.out.println(String.format("Kafka producer latency (min/mean/max/dev): %s/%s/%s/%s microseconds",
                snapshot.min(), snapshot.mean(), snapshot.max(), snapshot.populationStandardDeviation()));
    }
}
