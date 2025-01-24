package com.youyi.common.util;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
public class GsonUtil {

    private static final String EMPTY_JSON = "{}";

    private static final String EMPTY_ARRAY_JSON = "[]";

    private static final Gson GSON;
    private static final Gson PRETTY_GSON;

    static {
        TypeAdapter<Object> customizedAdapter = new CustomizedTypeAdapter();
        GSON = Converters.registerAll(new GsonBuilder())
            .registerTypeAdapter(new TypeToken<Map>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<HashMap>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<HashMap<String, Object>>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<List>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<ArrayList>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<List<Object>>() {
            }.getType(), customizedAdapter)
            .registerTypeAdapter(new TypeToken<ArrayList<Object>>() {
            }.getType(), customizedAdapter)
            .setStrictness(Strictness.LENIENT)
            .create();

        PRETTY_GSON = Converters.registerAll(new GsonBuilder()).setPrettyPrinting().create();
    }

    private GsonUtil() {
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }

        return GSON.toJson(obj);
    }

    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }

        return PRETTY_GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, @Nonnull Type type) {
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, @Nonnull Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, clazz);
    }

    @Nonnull
    public static <E, T extends Collection<E>> T fromJson(String json, @Nonnull Class<? extends Collection> collectionType,
        @Nonnull Class<E> valueType) {
        if (Strings.isNullOrEmpty(json)) {
            json = EMPTY_ARRAY_JSON;
        }

        return GSON.fromJson(json, TypeToken.getParameterized(collectionType, valueType).getType());
    }

    @Nonnull
    public static <K, V, T extends Map<K, V>> T fromJson(String json, @Nonnull Class<? extends Map> mapType, @Nonnull Class<K> keyType,
        @Nonnull Class<V> valueType) {
        if (Strings.isNullOrEmpty(json)) {
            json = EMPTY_JSON;
        }

        return GSON.fromJson(json, TypeToken.getParameterized(mapType, keyType, valueType).getType());
    }

    @Nonnull
    public static Map<String, String> fromJson(String json) {
        return fromJson(json, Map.class, String.class, String.class);
    }

    @Nonnull
    public static <E> E getValueFromJson(String json, @Nonnull String key, @Nonnull Class<E> valueType) {
        Object value = fromJson(json, Map.class, String.class, Object.class).get(key);
        if (value != null) {
            if (valueType.isInstance(value)) {
                return valueType.cast(value);
            } else if (value instanceof String) {
                return fromJson((String) value, valueType);
            } else {
                return fromJson(toJson(value), valueType);
            }
        }
        String errorMsg = "json value is not instance of " + valueType.getName();
        throw new UnsupportedOperationException(errorMsg);
    }

    @Nonnull
    public static <E> E getValueFromJson(String json, @Nonnull Enum<?> key, @Nonnull Class<E> valueType) {
        return getValueFromJson(json, key.name(), valueType);
    }

    public static JsonObject toJsonObject(Object obj) {
        JsonElement jsonElement = GSON.toJsonTree(obj);
        return jsonElement.getAsJsonObject();
    }

}
