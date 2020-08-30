package com.github.chrisgleissner.kafkasandbox.avro;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroName;
import org.apache.avro.reflect.ReflectData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AvroReflectTest {

    @Test
    void canGenerateSchemaViaReflection() {
        Schema schema = ReflectData.get().getSchema(Bar.class);
        String jsonSchema = schema.toString(false);
        assertThat(jsonSchema.startsWith("{\"type\":\"record\",\"name\":\"Bar\",\"namespace\":" +
                "\"com.github.chrisgleissner.kafkasandbox.avro.AvroReflectTest$\""));
        assertThat(jsonSchema).contains("stringList");
        assertThat(jsonSchema).doesNotContain("sHidden");
        System.out.println("JSON schema:\n" + schema.toString(true));
    }

    @Data
    public static class Foo {
        private int i;
        private Integer iWrapper;
    }

    @Value
    public static class Bar extends Foo {
        byte[] byteArray;
        String s;
        @AvroIgnore String sHidden;
        BigDecimal[] bdArray;
        @AvroName("stringList")
        List<String> sList;
        List<Integer> iList;
        Map<Long, String> map;
    }
}
