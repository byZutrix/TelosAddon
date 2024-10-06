package xyz.telosaddon.yuno.utils.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LongSerializer implements JsonSerializer<Long> {

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
