package com.youyi.infra.mvc.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Configuration
public class GsonConverterConfig {

    @Bean(name = "gsonHttpMessageConverters")
    public HttpMessageConverters gsonHttpMessageConverters() {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        Gson gson = new GsonBuilder().create();
        gsonConverter.setGson(gson);
        return new HttpMessageConverters(gsonConverter);
    }
}
