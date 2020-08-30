package com.github.chrisgleissner.kafkasandbox.avro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AvroReflectTest {

    @Test
    void canGenerateSchemaViaReflection() {
        Schema schema = ReflectData.AllowNull.get().getSchema(Bar.class);
        String jsonSchema = schema.toString(false);
        assertThat(jsonSchema.startsWith("{\"type\":\"record\",\"name\":\"Bar\",\"namespace\":" +
                "\"com.github.chrisgleissner.kafkasandbox.avro.AvroReflectTest$\""));
        assertThat(jsonSchema).contains("stringList");
        assertThat(jsonSchema).doesNotContain("sHidden");
        String avsc = schema.toString(true);

        log.info("Avro schema:\n" + avsc);
        Schema schema2 = new Schema.Parser().parse(avsc);
        assertThat(schema).isEqualTo(schema2);
    }

    @Test
    void handleNullFields() throws IOException {
        ReflectData.AllowNull reflectData = ReflectData.AllowNull.get();

        Bar bar = Bar.builder().s("1").build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
        new ReflectDatumWriter<>(Bar.class, reflectData).write(bar, encoder);
        encoder.flush();
        byte[] bytes = baos.toByteArray();
        log.info("Marshalled {} to {} byte(s)", bar, bytes.length);

        Decoder decoder = DecoderFactory.get().binaryDecoder(new ByteArrayInputStream(bytes), null);
        Schema schema = reflectData.getSchema(Bar.class);
        Bar bar2 = new ReflectDatumReader<Bar>(schema, schema, reflectData).read(Bar.builder().build(), decoder);
        assertThat(bar2.getS()).isEqualTo("1");
    }

    @Data @NoArgsConstructor @SuperBuilder
    public static class Foo {
        @Nullable private int i;
        @Nullable private Integer iWrapper;
    }

    @Value @NoArgsConstructor(force = true) @AllArgsConstructor @SuperBuilder
    public static class Bar extends Foo {
        String s;
        @Nullable byte[] byteArray;
        @Nullable @AvroIgnore String sHidden;
        @Nullable BigDecimal[] bdArray;
        @AvroName("stringList")
        @Nullable List<String> sList;
        @Nullable List<Integer> iList;
        @Nullable Map<Long, String> map;
    }
}
