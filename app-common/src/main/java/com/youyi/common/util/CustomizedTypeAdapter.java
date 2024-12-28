package com.youyi.common.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
public class CustomizedTypeAdapter extends TypeAdapter<Object> {

    private static final class LazyHolder {
        static final TypeAdapter<Object> LAZY_INSTANCE = new Gson().getAdapter(Object.class);
    }

    @Override
    public void write(JsonWriter writer, Object value) throws IOException {
        LazyHolder.LAZY_INSTANCE.write(writer, value);
    }

    @Override
    public Object read(JsonReader reader) throws IOException {
        return switch (reader.peek()) {
            case BEGIN_ARRAY -> readArray(reader);
            case BEGIN_OBJECT -> readObject(reader);
            case STRING -> reader.nextString();
            case NUMBER -> parseNumber(reader.nextString());
            case BOOLEAN -> reader.nextBoolean();
            case NULL -> {
                reader.nextNull();
                yield null;
            }
            default -> throw new IllegalStateException("Unexpected JSON token: " + reader.peek());
        };
    }

    private List<Object> readArray(JsonReader reader) throws IOException {
        List<Object> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(read(reader));
        }
        reader.endArray();
        return list;
    }

    private Map<String, Object> readObject(JsonReader reader) throws IOException {
        Map<String, Object> map = new HashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            map.put(reader.nextName(), read(reader));
        }
        reader.endObject();
        return map;
    }

    private Object parseNumber(String numberStr) {
        BigDecimal bigDecimal = new BigDecimal(numberStr);
        if (bigDecimal.scale() == 0) {
            try {
                return bigDecimal.intValueExact();
            } catch (ArithmeticException ignored) {
                return bigDecimal.longValue();
            }
        }
        return bigDecimal.doubleValue();
    }

}
