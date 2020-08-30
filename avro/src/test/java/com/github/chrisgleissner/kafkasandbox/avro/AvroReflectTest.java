package com.github.chrisgleissner.kafkasandbox.avro;

import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class AvroReflectTest {

    @Test
    void canGenerateSchemaViaReflection() {


    }

    @Value
    public static class Foo {
        int i;
        String s;
        List<String> sList;
        List<Integer> iList;
        Map<Long, String> map;
    }
}
