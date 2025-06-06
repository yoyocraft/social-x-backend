package com.youyi.infra.mvc;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.youyi.common.util.GsonUtil;
import java.io.File;
import java.lang.reflect.Type;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Configuration
public class GsonConverterConfig {

    private final Gson gson = Converters.registerAll(new GsonBuilder())
        .registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter())
        .registerTypeAdapter(File.class, new GsonUtil.FileAdapter())
        .create();

    @Bean(name = "gsonHttpMessageConverters")
    public HttpMessageConverters gsonHttpMessageConverters() {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson);
        return new HttpMessageConverters(gsonConverter);
    }

    // NOTE: https://stackoverflow.com/questions/51951641/swagger-unable-to-render-this-definition-the-provided-definition-does-not-speci
    private static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
            return JsonParser.parseString(json.value());
        }
    }
}
